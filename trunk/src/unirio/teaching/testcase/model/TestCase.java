package unirio.teaching.testcase.model;


public class TestCase
{
	private int duration;

	/**
	 * total of duration
	 */	
	public void setDuration(int duration_)
	{
		this.duration = duration_;
	}
	
	public int getDuration()
	{
		return this.duration;
	}
	
	/**
	 * % total of covarage
	 */
	private int[] coverage;

	/**
	 * Initializes the test case
	 */
	public TestCase(int featureCount)
	{
		this.duration = 0;
		this.coverage = new int[featureCount];
	}
	
	/**
	 * Gets the coverage for a given feature
	 */
	public int getCoverage(int featureIndex)
	{
		return this.coverage[featureIndex];
	}
	
	/**
	 * Sets the coverage for a given feature
	 */
	public void setCoverage(int featureIndex, int coverage)
	{
		this.coverage[featureIndex] = coverage;
	}

	public int getTotalCoverage()
	{
		int total = 0;
		
		for (int i = 0; i < coverage.length; i++)
			total += coverage[i];
		
		return total;
	}
}