package unirio.teaching.testcase;

import java.io.OutputStreamWriter;
import java.util.Vector;

import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.SinglePointCrossover;
import jmetal.base.operator.mutation.BitFlipMutation;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.base.visitor.neighborhood.BinaryNeighborVisitor;
import jmetal.base.visitor.neighborhood.NeighborVisitor;
import jmetal.metaheuristics.randomSearch.HillClimbing;
import jmetal.metaheuristics.randomSearch.MonoHillClimbing;
import jmetal.metaheuristics.randomSearch.MonoRandomSearch;
import jmetal.metaheuristics.randomSearch.RandomSearch;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import unirio.experiments.monoobjective.execution.FileMonoExperimentListener;
import unirio.experiments.monoobjective.execution.MonoExperiment;
import unirio.experiments.monoobjective.execution.StreamMonoExperimentListener;
import unirio.teaching.testcase.model.Catalog;
import unirio.teaching.testcase.reader.TestcaseReader;

/**
 * 
 */
public class MainProgram
{
	private static final int CYCLES = 10;
	private static final int timeLimit = 200;
	
	protected static String[] instanciasAleatorias =
	{
 		"C:\\workspace\\OptimizationAlgorithms\\trunk\\data\\Instance.txt",
	};

	/**
	 * start the program
	 */
	public static void main(String[] args) throws Exception
	{
		MainProgram mp = new MainProgram();
		
		System.out.println("timeLimit = " + timeLimit);
		System.out.println("CYCLES = " + CYCLES);
		System.out.println();
		
		for (int i = 0; i < instanciasAleatorias.length; i++)
			if (instanciasAleatorias[i].length() > 0)
				mp.run(instanciasAleatorias[i], timeLimit);
	}
	
	/**
	 * Prepara e executa os algoritmos do estudo
	 * 
	 * @param instancia parâmetro que indica a origem dos dados de entrada (arquivo TXT)
	 */
	public void run(String instancia, int timeLimit) throws Exception
	{
		// Carrega os dados da instância desejada
		TestcaseReader reader = new TestcaseReader();
		Catalog catalog = reader.execute(instancia);
		catalog.setTimeLimit(timeLimit);
		// verificar pq o array catalog é criado com duas posições a mais?
		
		// Cria o vetor de instâncias
		Vector<Catalog> instancias = new Vector<Catalog>();
		instancias.add(catalog);

		System.out.println("GA");
		
       	// Executa os experimentos com algoritmos genéticos
		ExperimentoAlgoritmoGenetico ga = new ExperimentoAlgoritmoGenetico();
       	ga.addListerner(new FileMonoExperimentListener("saida ga.txt", true));
       	ga.addListerner(new StreamMonoExperimentListener(new OutputStreamWriter(System.out), true));
       	ga.run(instancias, CYCLES);
       	
       	
       	System.out.println("RS");
       	
		// Executa os experimentos com Random Search
       	ExperimentoRandomSearch rs = new ExperimentoRandomSearch();
       	rs.addListerner(new FileMonoExperimentListener("saida rs.txt", true));
       	rs.addListerner(new StreamMonoExperimentListener(new OutputStreamWriter(System.out), true));
       	rs.run(instancias, CYCLES);
       	
       	System.out.println("HC");
       	
     // Executa os experimentos com HIll Climbing
       	ExperimentoHillClimbing hc = new ExperimentoHillClimbing();
       	hc.addListerner(new FileMonoExperimentListener("saida hc.txt", true));
       	hc.addListerner(new StreamMonoExperimentListener(new OutputStreamWriter(System.out), true));
       	hc.run(instancias, CYCLES);
	}
}


abstract class ExperimentoGenerico extends MonoExperiment<Catalog>
{
	protected TestcaseProblem problem;

	protected double[] getSolutionData(Solution solution)
	{
		double[] data = new double[3];
		data[0] = solution.getObjective(0);
		data[1] = problem.calcCoverage(solution);
		data[2] = problem.calcExecutionTime(solution);
		return data;
	}
	
	protected TestcaseProblem createProblem(Catalog instance) throws ClassNotFoundException
	{
		problem = new TestcaseProblem(instance);
		return problem;
	}
}

class ExperimentoAlgoritmoGenetico extends ExperimentoGenerico
{
	@Override
	protected Solution runCycle(Catalog instance, int instanceNumber) throws Exception
	{
		TestcaseProblem problem = createProblem(instance);
		int variableSize = instance.size();

		int populationSize = 3 * variableSize;
		int maxEvaluations = 50 * populationSize;// * populationSize;

		Operator crossover = new SinglePointCrossover();
		crossover.setParameter("probability", 0.8);

		Operator mutation = new BitFlipMutation();
		mutation.setParameter("probability", 0.02);

		Operator selection = new BinaryTournament();
				
		Algorithm ga = new gGA(problem);		
		ga.setInputParameter("populationSize", populationSize);
		ga.setInputParameter("maxEvaluations", maxEvaluations);
		ga.addOperator("crossover", crossover);
		ga.addOperator("mutation", mutation);
		ga.addOperator("selection", selection);
		
		SolutionSet solutions = ga.execute();
		return solutions.get(0);
	}
}

class ExperimentoRandomSearch extends ExperimentoGenerico
{
	@Override
	protected Solution runCycle(Catalog instance, int instanceNumber) throws Exception
	{
		TestcaseProblem problem = createProblem(instance);
		int variableSize = instance.size();

		int populationSize = 3 * variableSize;
		int maxEvaluations = 50 * populationSize;// * populationSize;
		//RandomSearch rs = new RandomSearch(problem, maxEvaluations);
		RandomSearch rs = new RandomSearch(problem);
		rs.setInputParameter("populationSize", populationSize);
		rs.setInputParameter("maxEvaluations", maxEvaluations);
		
		SolutionSet solutions = rs.execute();
		return solutions.get(0);
	}
}


class ExperimentoHillClimbing extends ExperimentoGenerico
{
	@Override
	protected Solution runCycle(Catalog instance, int instanceNumber) throws Exception
	{
		TestcaseProblem problem = createProblem(instance);
		int variableSize = instance.size();

		int populationSize = 3 * variableSize;
		int maxEvaluations = 50 * populationSize;
		NeighborVisitor visitor = new BinaryNeighborVisitor(problem);  
		HillClimbing hc = new HillClimbing(problem, visitor, maxEvaluations);
		
		SolutionSet solutions = hc.execute();
		return solutions.get(0);
	}
}