package unirio.teaching.testcase.model;

import java.util.ArrayList;

public class Catalog extends ArrayList<TestCase>
{
	private static final long serialVersionUID = 5107386296454130062L;
	private int timeLimit;
	private boolean[] testCases;

	public void setTimeLimit(int timeLimit) 
	{
		this.timeLimit = timeLimit;			
	}
	
	public int getTimeLimit() 
	{
		return this.timeLimit;			
	}
}