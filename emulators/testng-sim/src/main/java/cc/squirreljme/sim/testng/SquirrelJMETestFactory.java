package cc.squirreljme.sim.testng;

import net.multiphasicapps.tac.TestInteger;
import net.multiphasicapps.tac.TestInterface;
import org.testng.ITestContext;
import org.testng.annotations.Factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ServiceLoader;

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
		// Collect and drop bindings
		JNANativeBind.bind();
		
		// Collect tests
		Collection<Object> tests = new LinkedList<>();
		
		// The list of tests hides in the services file
		try (InputStream in = this.getClass().getClassLoader().
				getResourceAsStream(SERVICE_NAME))
		{
			// No tests available
			if (in == null)
				return new Object[0];
			
			// Read all tests that exist
			try (BufferedReader br = new BufferedReader(
				new InputStreamReader(in, "utf-8")))
			{
				for (;;)
					try
					{
						String ln = br.readLine();
						
						// EOF?
						if (ln == null)
							break;
						
						// Ignore blank lines
						if (ln.isEmpty())
							continue;
						
						tests.add(new SquirrelJMETest(Class.forName(ln)));
					}
					catch (ClassNotFoundException e)
					{
						e.printStackTrace();
					}
			}
		}
		
		return tests.<Object>toArray(new Object[tests.size()]);
	}
}
