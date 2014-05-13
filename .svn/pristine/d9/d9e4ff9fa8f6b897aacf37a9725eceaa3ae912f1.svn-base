package jmetal.metaheuristics.randomSearch;

import java.util.ArrayList;
import java.util.List;
import jmetal.base.Algorithm;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.visitor.neighborhood.NeighborVisitor;
import jmetal.util.JMException;
import jmetal.util.NonDominatedSolutionList;

public class HillClimbing extends Algorithm
{
	private Problem problem;
	private NeighborVisitor visitor;
	private int maxEvaluations;

	/**
	 * Constructor
	 * 
	 * @param problem Problem to solve
	 */
	public HillClimbing(Problem problem, NeighborVisitor visitor, int maxEvaluations)
	{
		this.problem = problem;
		this.visitor = visitor;
		this.maxEvaluations = maxEvaluations;
	}
	
	/**
	 * Runs the algorithm.
	 */
	public SolutionSet execute() throws JMException, ClassNotFoundException
	{
		NonDominatedSolutionList ndl = new NonDominatedSolutionList();
		List<Solution> pending = new ArrayList<Solution>();
		int pendingCount = 0;
		int pendingWalker = 0;
		int evaluations = 0;

		while (evaluations < maxEvaluations)
		{
			if (pendingCount <= pendingWalker)
			{
				Solution solution = new Solution(problem);
				problem.evaluate(solution);
				evaluations++;
				
				if (ndl.add(solution))
				{
					pending.add(solution);
					pendingCount++;
				}
			}
			
			if (pendingCount > pendingWalker)
			{
				Solution current = pending.get(pendingWalker);
				pendingWalker++;
				
				int neighborCount = visitor.neighborCount(current);
	
				for (int i = 0; i < neighborCount; i++)
				{
					Solution neighbor = visitor.getNeighbor(current, i);
					problem.evaluate(neighbor);
					evaluations++;
					
					if (ndl.add(neighbor))
					{
						pending.add(neighbor);
						pendingCount++;
					}
				}
			}
		}

		return ndl;
	}
}