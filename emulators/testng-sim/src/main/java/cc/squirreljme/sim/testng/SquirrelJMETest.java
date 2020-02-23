package cc.squirreljme.sim.testng;

import net.multiphasicapps.tac.TestInterface;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
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
	protected final Class<? extends TestInterface> testClass;
	
	/**
	 * Initializes the test information.
	 *
	 * @param __test The test class.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/23
	 */
	public SquirrelJMETest(Class<? extends TestInterface> __test)
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
		throw new Error("TODO");
	}
}
