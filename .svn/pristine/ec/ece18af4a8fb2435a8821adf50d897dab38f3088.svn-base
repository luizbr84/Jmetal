package unirio.teaching.clustering;

import java.io.OutputStreamWriter;
import java.util.Vector;

import javax.management.modelmbean.XMLParseException;

import jmetal.base.Operator;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.operator.crossover.SinglePointCrossover;
import jmetal.base.operator.mutation.IntUniformMutation;
import jmetal.base.operator.selection.BinaryTournament;
import jmetal.metaheuristics.randomSearch.MonoRandomSearch;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import unirio.experiments.instancegen.FlatPublisherReaderException;
import unirio.experiments.monoobjective.execution.FileMonoExperimentListener;
import unirio.experiments.monoobjective.execution.MonoExperiment;
import unirio.experiments.monoobjective.execution.StreamMonoExperimentListener;
import unirio.teaching.clustering.generic.model.Project;
import unirio.teaching.clustering.generic.reader.CDAReader;

public class MainProgram
{
	private static final int CYCLES = 10;
	
	private static String[] instanceFilenamesReals =
	{
		/*"data\\clustering\\javacc.odem",
		"data\\clustering\\junit-3.8.1 100C.odem",
		"data\\clustering\\servletapi-2.3 74C.odem",
		"data\\clustering\\xml-apis-1.0.b2 W3C-DOM.odem",
		"data\\clustering\\jmetal.odem",
		"data\\clustering\\xml-apis-1.0.b2.odem",
		"data\\clustering\\dom4j-1.5.2 195C.odem",
		"data\\clustering\\seemp.odem",*/
		"data\\clustering\\joda-money 26C.odem",
		/*"data\\clustering\\jxls-reader 27C.odem",
		"data\\clustering\\javaocr 59C.odem",
		"data\\clustering\\jpassword 96C.odem",
		"data\\clustering\\jxls-core 83C.odem",
		"data\\clustering\\tinytim 134C.odem",*/
		""
	};
	
	private Vector<Project> readInstances(String[] filenames) throws FlatPublisherReaderException, XMLParseException
	{
		Vector<Project> instances = new Vector<Project>();
		CDAReader reader = new CDAReader();
		
		for (String filename : filenames)
			if (filename.length() > 0)
				instances.add (reader.execute(filename));
		
		return instances;
	}
	
	public static final void main(String[] args) throws Exception
	{
		MainProgram mp = new MainProgram();
		Vector<Project> instances = new Vector<Project>();
		instances.addAll(mp.readInstances(instanceFilenamesReals));
		
		ExperimentoAlgoritmoGenetico ga = new ExperimentoAlgoritmoGenetico();
       	ga.addListerner(new StreamMonoExperimentListener(new OutputStreamWriter(System.out), true));
       	ga.addListerner(new FileMonoExperimentListener("ga saida.txt", true));
       	ga.run(instances, CYCLES);

       	ExperimentoRandomSearch rs = new ExperimentoRandomSearch();
       	rs.addListerner(new StreamMonoExperimentListener(new OutputStreamWriter(System.out), true));
       	rs.addListerner(new FileMonoExperimentListener("rs saida.txt", true));
       	rs.run(instances, CYCLES);
	}
}

class ExperimentoAlgoritmoGenetico extends MonoExperiment<Project>
{
	@Override
	protected Solution runCycle(Project instance, int instanceNumber) throws Exception
	{
		ClusteringProblem problem = new ClusteringProblem(instance, instance.getClassCount() / 2);
		int classCount = instance.getClassCount();

		double crossoverProbability = (classCount < 100) ? 0.8 : 1.0;
		Operator crossover = new SinglePointCrossover();
		crossover.setParameter("probability", crossoverProbability);
		
		double mutationProbability = 0.004 * Math.log(classCount) / Math.log(2);
		Operator mutation = new IntUniformMutation();
		mutation.setParameter("probability", mutationProbability);

		Operator selection = new BinaryTournament();
		int population = 10 * classCount;
		int evaluations = 20 * classCount * population;
		
		gGA algorithm = new gGA(problem);
		algorithm.setInputParameter("populationSize", population);
		algorithm.setInputParameter("maxEvaluations", evaluations);
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		SolutionSet solutions = algorithm.execute();
		return solutions.get(0);
	}
}

class ExperimentoRandomSearch extends MonoExperiment<Project>
{
	@Override
	protected Solution runCycle(Project instance, int instanceNumber) throws Exception
	{
		ClusteringProblem problem = new ClusteringProblem(instance, instance.getClassCount() / 2);
		int tamanhoPopulacao = 10 * instance.getClassCount();
		int maxEvaluations = 20 * instance.getClassCount() * tamanhoPopulacao;
		
		MonoRandomSearch rs = new MonoRandomSearch(problem, maxEvaluations);		
		SolutionSet solutions = rs.execute();
		return solutions.get(0);
	}
}