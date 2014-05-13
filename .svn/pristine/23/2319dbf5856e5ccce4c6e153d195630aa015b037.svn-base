package jmetal.qualityIndicator;

import jmetal.qualityIndicator.util.MetricsUtil;

public class MaximumDistance
{
	private MetricsUtil utils_;

	public MaximumDistance()
	{
		utils_ = new jmetal.qualityIndicator.util.MetricsUtil();
	}

	public double calculate(double[][] front, double[][] trueParetoFront, int numberOfObjectives)
	{
		// STEP 1. Obtain the maximum and minimum values of the Pareto front
		double[] maximumValue = utils_.getMaximumValues(trueParetoFront, numberOfObjectives);
		double[] minimumValue = utils_.getMinimumValues(trueParetoFront, numberOfObjectives);

		// STEP 2. Get the normalized front and true Pareto fronts
		double[][] normalizedFront = utils_.getNormalizedFront(front, maximumValue, minimumValue);
		double[][] normalizedParetoFront = utils_.getNormalizedFront(trueParetoFront, maximumValue, minimumValue);

		// STEP 3. Get the maximum distance between each point of the front and the nearest point in the true Pareto front
		double maximum = 0;

		for (int i = 0; i < front.length; i++)
		{
			double distance = utils_.distanceToClosedPoint(normalizedFront[i], normalizedParetoFront);
			
			if (distance > maximum)
				maximum = distance;
		}

		return maximum;
	}
}