/**
 * SolutionComparator.java
 * 
 * @author Juan J. Durillo
 * @version 1.0
 */
package jmetal.base.operator.comparator;

import java.util.Comparator;
import jmetal.base.Solution;
import jmetal.base.solutionType.BinarySolutionType;
import jmetal.util.Configuration;
import jmetal.util.Distance;
import jmetal.util.JMException;

/**
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on the values of the variables.
 */
public class SolutionComparator implements Comparator<Solution>
{
	/**
	 * Establishes a value of allowed dissimilarity
	 */
	private static final double EPSILON = 1e-10;

	/**
	 * Compares two solutions.
	 * 
	 * @param o1 Object representing the first <code>Solution</code>.
	 * @param o2 Object representing the second <code>Solution</code>.
	 * @return 0, if both solutions are equals with a certain dissimilarity, -1
	 *         otherwise.
	 * @throws JMException
	 * @throws JMException
	 */
	public int compare(Solution o1, Solution o2)
	{
		Solution solution1, solution2;
		solution1 = o1;
		solution2 = o2;

		if (solution1.numberOfVariables() != solution2.numberOfVariables())
			return -1;
		
		if (solution1.getType().getClass().getName().compareTo(BinarySolutionType.class.getName()) == 0)
			return solution1.getDecisionVariables()[0].toString().compareTo(solution2.getDecisionVariables()[0].toString());

		try
		{
			if ((new Distance()).distanceBetweenSolutions(solution1, solution2) < EPSILON)
				return 0;
		}
		catch (JMException e)
		{
			Configuration.logger_.severe("SolutionComparator.compare: JMException ");
		}

		return -1;
	}
}