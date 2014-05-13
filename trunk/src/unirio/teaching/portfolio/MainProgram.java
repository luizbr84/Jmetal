package unirio.teaching.portfolio;

import java.io.OutputStreamWriter;
import java.util.Vector;

import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.SinglePointCrossover;
import jmetal.base.operator.mutation.BitFlipMutation;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.metaheuristics.randomSearch.MonoRandomSearch;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import unirio.experiments.monoobjective.execution.FileMonoExperimentListener;
import unirio.experiments.monoobjective.execution.MonoExperiment;
import unirio.experiments.monoobjective.execution.StreamMonoExperimentListener;
import unirio.teaching.portfolio.general.model.ProjetosCandidatos;
import unirio.teaching.portfolio.general.reader.PortfolioReader;

/**
 * Programa principal
 */
public class MainProgram
{
	private static final int CYCLES = 10;
	
	protected static String[] instanciasAleatorias =
	{
			
 		"C:\\workspace\\Jmetal\\trunk\\data\\portfolio\\Instancia_13P_10R.txt",
 		
		/*"data\\portfolio\\Instancia_14P_10R.txt",
		"data\\portfolio\\Instancia_15P_10R.txt",
		"data\\portfolio\\Instancia_25P_3R.txt",
		"data\\portfolio\\Instancia_25P_5R.txt",
		"data\\portfolio\\Instancia_25P_7R.txt",
		"data\\portfolio\\Instancia_25P_10R.txt",
		"data\\portfolio\\Instancia_50P_3R.txt",
		"data\\portfolio\\Instancia_50P_5R.txt",
		"data\\portfolio\\Instancia_50P_7R.txt",
		"data\\portfolio\\Instancia_50P_10R.txt",
		"data\\portfolio\\Instancia_75P_3R.txt",
		"data\\portfolio\\Instancia_75P_5R.txt",
		"data\\portfolio\\Instancia_75P_7R.txt",
		"data\\portfolio\\Instancia_75P_10R.txt",
		"data\\portfolio\\Instancia_100P_3R.txt",
		"data\\portfolio\\Instancia_100P_5R.txt",
		"data\\portfolio\\Instancia_100P_7R.txt",
		"data\\portfolio\\Instancia_100P_10R.txt",*/
 		""
	};

	/**
	 * Programa principal
	 */
	public static void main(String[] args) throws Exception
	{
		MainProgram mp = new MainProgram();
		
		for (int i = 0; i < instanciasAleatorias.length; i++)
			if (instanciasAleatorias[i].length() > 0)
				mp.run(instanciasAleatorias[i], 0.60);
	}
	
	/**
	 * Prepara e executa os algoritmos do estudo
	 * 
	 * @param instancia parâmetro que indica a origem dos dados de entrada (arquivo TXT)
	 */
	public void run(String instancia, double percLimiteOrcamento) throws Exception
	{
		// Carrega os dados da instância desejada
		PortfolioReader reader = new PortfolioReader();
		reader.execute(instancia);
		ProjetosCandidatos candidatos = reader.getCandidatos();
		candidatos.setPercentualLimiteOrcamento(percLimiteOrcamento);

		// Cria o vetor de instâncias
		Vector<ProjetosCandidatos> instancias = new Vector<ProjetosCandidatos>();
		instancias.add(candidatos);

       	// Executa os experimentos com algoritmos genéticos
       	ExperimentoAlgoritmoGenetico ga = new ExperimentoAlgoritmoGenetico();
       	ga.addListerner(new FileMonoExperimentListener("saida ga.txt", true));
       	ga.addListerner(new StreamMonoExperimentListener(new OutputStreamWriter(System.out), true));
       	ga.run(instancias, CYCLES);
       	
		// Executa os experimentos com Random Search
       	ExperimentoRandomSearch rs = new ExperimentoRandomSearch();
       	rs.addListerner(new FileMonoExperimentListener("saida rs.txt", true));
       	rs.addListerner(new StreamMonoExperimentListener(new OutputStreamWriter(System.out), true));
       	rs.run(instancias, CYCLES);
	}
}

abstract class ExperimentoGenerico extends MonoExperiment<ProjetosCandidatos>
{
	protected PortfolioProblem problem;

	protected double[] getSolutionData(Solution solution)
	{
		double[] data = new double[3];
		data[0] = solution.getObjective(0);
		data[1] = problem.calculaRetornoPortfolio(solution);
		data[2] = problem.calculaRiscoPortfolio(solution);
		return data;
	}
	
	protected PortfolioProblem criaProblema(ProjetosCandidatos instance) throws ClassNotFoundException
	{
		problem = new PortfolioProblem(instance);
		return problem;
	}
}

class ExperimentoAlgoritmoGenetico extends ExperimentoGenerico
{
	@Override
	protected Solution runCycle(ProjetosCandidatos instance, int instanceNumber) throws Exception
	{
		PortfolioProblem problem = criaProblema(instance);
		int numeroProjetos = instance.pegaNumeroProjetos();

		int tamanhoPopulacao = 2 * numeroProjetos;
		int maxEvaluations = 100 * tamanhoPopulacao * tamanhoPopulacao;

		Operator crossover = new SinglePointCrossover();
		crossover.setParameter("probability", 0.8);

		Operator mutation = new BitFlipMutation();
		mutation.setParameter("probability", 0.02);

		Operator selection = new BinaryTournament();
				
		Algorithm ga = new gGA(problem);		
		ga.setInputParameter("populationSize", tamanhoPopulacao);
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
	protected Solution runCycle(ProjetosCandidatos instance, int instanceNumber) throws Exception
	{
		PortfolioProblem problem = criaProblema(instance);
		int numeroProjetos = instance.pegaNumeroProjetos();

		int tamanhoPopulacao = numeroProjetos + (numeroProjetos % 2);
		int maxEvaluations = 100 * tamanhoPopulacao * tamanhoPopulacao;
		MonoRandomSearch rs = new MonoRandomSearch(problem, maxEvaluations);
		
		SolutionSet solutions = rs.execute();
		return solutions.get(0);
	}
}