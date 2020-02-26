package cc.squirreljme.emulator;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.Factory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;

/**
 * The factory for SquirrelJME Tests.
 *
 * @since 2020/02/23
 */
public class SquirrelJMETestFactory
{
	/** The name of the service. */
	public static final String SERVICE_NAME =
		"META-INF/services/net.multiphasicapps.tac.TestInterface";
	
	/**
	 * Produces the tests to run.
	 *
	 * @return The tests to run.
	 * @since 2020/02/23
	 */
	@Factory
	public Object[] createInstance(ITestContext __context)
		throws Exception
	{
		// Poke the native bindings class so it gets initializes and setup
		new NativeBinding();
		
		// Collect tests
		Collection<Object> tests = new LinkedList<>();
		for (String testName : SquirrelJMEAlterSuiteListener.findTestClasses())
			try
			{
				tests.add(Class.forName(testName));
			}
			catch (ClassNotFoundException e)
			{
				// Ignore
			}
		
		return tests.<Object>toArray(new Object[tests.size()]);
	}
}
