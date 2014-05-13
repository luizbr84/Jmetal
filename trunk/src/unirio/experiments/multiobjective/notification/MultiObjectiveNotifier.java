package unirio.experiments.multiobjective.notification;

import jmetal.base.SolutionSet;

public interface MultiObjectiveNotifier
{
	void generationFinished (int generatioNumber, SolutionSet population);

	void finish (int generatioNumber, SolutionSet population);
}