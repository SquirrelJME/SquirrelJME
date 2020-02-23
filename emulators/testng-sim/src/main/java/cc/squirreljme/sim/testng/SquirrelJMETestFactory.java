package cc.squirreljme.sim.testng;

import net.multiphasicapps.tac.TestInteger;
import org.testng.ITestContext;
import org.testng.annotations.Factory;

import java.util.Collection;
import java.util.LinkedList;

/**
 * The factory for SquirrelJME Tests.
 *
 * @since 2020/02/23
 */
public class SquirrelJMETestFactory
{
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
		Collection<Object> rv = new LinkedList<>();
		
		rv.add(new SquirrelJMETest(TestInteger.class));
		
		return rv.<Object>toArray(new Object[rv.size()]);
	}
}
