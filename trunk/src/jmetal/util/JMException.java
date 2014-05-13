/**
 * JMException.java
 * 
 * @author Antonio J. Nebro
 * @version 1.0
 */
package jmetal.util;


/**
 * jmetal exception class
 */
public class JMException extends Exception
{
	private static final long serialVersionUID = 1808621422472186271L;

	public JMException(String message)
	{
		super(message);
	}
}