package unirio.teaching.portfolio;

import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.solutionType.BinarySolutionType;
import jmetal.base.variable.Binary;
import jmetal.util.JMException;
import unirio.teaching.portfolio.general.calculation.PortfolioCalculator;
import unirio.teaching.portfolio.general.model.ProjetosCandidatos;

/**
 * Classe que representa um problema de otimização de portfolio 
 */
public class PortfolioProblem extends Problem
{
	private static final double INFINITO = 1000000.0;
	
	private PortfolioCalculator calculador;
	private ProjetosCandidatos candidatos;
	private int evaluations;

	/**
	 * Prepara o problema de otimização dos portfolios
	 * 
	 * @param candidatos		Projetos candidatos ao portfolio
	 */
	public PortfolioProblem(ProjetosCandidatos candidatos) throws ClassNotFoundException
	{
		this.calculador = new PortfolioCalculator(candidatos);
		this.candidatos = candidatos;
		this.evaluations = 0;

		numberOfObjectives_ = 1;
		numberOfVariables_ = 1;
		solutionType_ = new BinarySolutionType(this);
		length_ = new int[numberOfVariables_];
		length_[0] = candidatos.pegaNumeroProjetos();
	}

	/**
	 * Converte uma solução para um array indicando a presença dos projetos
	 * 
	 * @param solution		Solução que representa o portfolio
	 */
	public boolean[] converteSolucaoArray(Solution solution)
	{
		Binary portfolio = (Binary) solution.getDecisionVariables()[0];
		boolean[] projetos = new boolean[candidatos.pegaNumeroProjetos()];
		
		for (int i = 0; i < candidatos.pegaNumeroProjetos(); i++)
			projetos[i] = portfolio.bits_.get(i);
		
		return projetos;
	}
	
	/**
	 * Calcula o retorno do portfolio representado por uma solução
	 */
	public double calculaRetornoPortfolio(Solution solution)
	{
		boolean[] projetos = converteSolucaoArray(solution);
		return calculador.calculaRetornoPortfolio(projetos);
	}
	
	/**
	 * Calcula o risco do portfolio representado por uma solução
	 */
	public double calculaRiscoPortfolio(Solution solution)
	{
		boolean[] projetos = converteSolucaoArray(solution);
		return calculador.calculaRiscoPortfolio(projetos);
	}
	
	/**
	 * Avalia o portfólio de acordo com os critérios da técnica estudada.
	 * 
	 * @param solution		Solução que representa o portfolio
	 */
	@Override
	public void evaluate(Solution solution) throws JMException
	{
		boolean[] projetos = converteSolucaoArray(solution);
		double custo = -calculador.calculaCustoPortfolio(projetos);

		if (custo <= candidatos.getLimiteOrcamento())
		{
			double retorno = calculador.calculaRetornoPortfolio(projetos);
			double risco = calculador.calculaRiscoPortfolio(projetos);
			double ie = (risco == 0) ? retorno : retorno / risco;
			solution.setObjective(0, -ie);
			solution.setLocation(evaluations);
		}	
		else
			solution.setObjective(0, INFINITO);
		
		evaluations++;
	}
}