package unirio.teaching.testcase;

import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.solutionType.BinarySolutionType;
import jmetal.base.variable.Binary;
import jmetal.util.JMException;
import unirio.teaching.testcase.model.Catalog;
import unirio.teaching.testcase.model.CatalogSolution;

/**
 * Classe que representa um problema de otimização de portfolio 
 */
public class TestcaseProblem extends Problem
{
	private static final double MinimumValue = 0;
	
	private Catalog catalog;
	private int evaluations;

	/**
	 * Prepara o problema de otimização dos portfolios
	 * 
	 * @param candidatos		Testcase candidatos ao portfolio
	 */
	public TestcaseProblem(Catalog catalog_) throws ClassNotFoundException
	{
		
		this.catalog = catalog_;
		this.evaluations = 0;

		numberOfObjectives_ = 1;
		numberOfVariables_ = 1;
		solutionType_ = new BinarySolutionType(this);
		length_ = new int[numberOfVariables_];
		length_[0] = catalog.size();
	}

	/**
	 * Converte uma solução para um array indicando a presença dos Testcase
	 * 
	 * @param solution		Solução que representa o portfolio
	 */
	public boolean[] convertToArray(Solution solution)
	{
		Binary catalog = (Binary) solution.getDecisionVariables()[0];
		boolean[] catalog_ = new boolean[this.catalog.size()];
		
		for (int i = 0; i < this.catalog.size(); i++)
			catalog_[i] = catalog.bits_.get(i);
		
		return catalog_;
	}
	
	/**
	 * Calcula o retorno do portfolio representado por uma solução
	 */
	public int calcCoverage(Solution solution)
	{
		boolean[] solution_ = convertToArray(solution);
		
		CatalogSolution CS = new CatalogSolution(this.catalog);
		CS.setTestCases(solution_);
		
		return CS.getCoverage();
	}
	
	public int calcExecutionTime(Solution solution)
	{
		boolean[] solution_ = convertToArray(solution);
		CatalogSolution CS = new CatalogSolution(this.catalog);
		CS.setTestCases(solution_);
		
		return CS.getExecutionTime();
	}
	
		/**
	 * Avalia o portfólio de acordo com os critérios da técnica estudada.
	 * 
	 * @param solution		Solução que representa o portfolio
	 */
	@Override
	public void evaluate(Solution solution) throws JMException
	{
		int duration = calcExecutionTime(solution);

		if (duration <= this.catalog.getTimeLimit())
		{
			solution.setObjective(0, -calcCoverage(solution));
			//solution.setObjective(0, -duration);
			solution.setLocation(evaluations);
		}	
		else
			solution.setObjective(0, MinimumValue);
		
		evaluations++;
	}
}