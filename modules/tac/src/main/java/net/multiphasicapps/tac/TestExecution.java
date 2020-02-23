package net.multiphasicapps.tac;

/**
 * This represents the expected and resultant results of a test.
 *
 * @since 2020/02/23
 */
public final class TestExecution
{
	/** The status of the test. */
	public final TestStatus status;
	
	/** The class being tested. */
	public final Class<?> testClass;
	
	/** The result of the test. */
	public final TestResult result;
	
	/** The expected results of the test. */
	public final TestResult expected;
	
	/** The exception tossed, if there is one. */
	public final Object tossed;
	
	/**
	 * Initializes the test execution.
	 *
	 * @param __ts The test status.
	 * @param __tc The test class.
	 * @param __res The results of the test.
	 * @param __exp The expected results.
	 * @param __tossed Any exception that was tossed.
	 * @since 2020/02/23
	 */
	public TestExecution(TestStatus __ts, Class<?> __tc,
		TestResult __res, TestResult __exp, Object __tossed)
	{
		this.status = __ts;
		this.testClass = __tc;
		this.result = __res;
		this.expected = __exp;
		this.tossed = __tossed;
	}
}
