package cc.squirreljme.sim.testng;

import net.multiphasicapps.tac.TestExecution;
import net.multiphasicapps.tac.TestInterface;
import org.testng.*;
import org.testng.annotations.Test;
import org.testng.internal.BaseTestMethod;
import org.testng.internal.ConstructorOrMethod;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

/**
 * Used to wrap SquirrelJME tests.
 *
 * @since 2020/02/23
 */
public class SquirrelJMETest
{
	/** The test class to execute. */
	protected final Class<?> testClass;
	
	/**
	 * Initializes the test information.
	 *
	 * @param __test The test class.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/23
	 */
	public SquirrelJMETest(Class<?> __test)
		throws NullPointerException
	{
		if (__test == null)
			throw new NullPointerException("No test class specified.");
		
		this.testClass = __test;
	}
	
	/**
	 * Runs the SquirrelJME test.
	 *
	 * @since 2020/02/23
	 */
	@Test
	public void test()
	{
		// Create instance of test
		TestInterface instance;
		try
		{
			instance = (TestInterface)this.testClass.newInstance();
		}
		catch (ClassCastException|InstantiationException|
			IllegalAccessException e)
		{
			throw new RuntimeException("Failed to run test.", e);
		}
		
		// Print name of test as debug
		System.err.printf("Running test %s...%n",
			instance.getClass().getName());
		
		// Run the test with no parameters passed
		TestExecution execution = instance.runExecution();
		
		// Depends on the result
		switch (execution.status)
		{
			case SUCCESS:
				System.err.println(execution.result);
				break;
				
			case FAILED:
				execution.expected.printComparison(System.err,
					execution.result);
				Assert.fail(execution.result.toString());
				break;
				
			case TEST_EXCEPTION:
				throw new TestException("Error in test.",
					((execution.tossed instanceof Throwable) ?
						(Throwable)execution.tossed : (Throwable)null));
			
			case UNTESTABLE:
				throw new SkipException("Skipping test.",
					((execution.tossed instanceof Throwable) ?
						(Throwable)execution.tossed : (Throwable)null));
		}
	}
}
