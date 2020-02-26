package net.multiphasicapps.tac;

/**
 * This is used to throw the test execution.
 *
 * @since 2020/02/26
 */
public class ThrownTestExecution
	extends RuntimeException
{
	/** The tossed execution. */
	public final TestExecution execution;
	
	/**
	 * Initializes the exception.
	 *
	 * @param __exec The execution to trace.
	 * @param __cause The cause of it.
	 * @since 2020/02/26
	 */
	public ThrownTestExecution(TestExecution __exec, Throwable __cause)
	{
		super((__exec == null ? "NULL" : __exec.toString()), __cause);
		
		this.execution = __exec;
	}
}
