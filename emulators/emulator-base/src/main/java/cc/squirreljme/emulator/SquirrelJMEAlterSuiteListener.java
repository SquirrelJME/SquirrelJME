// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

/**
 * Alters suite names.
 *
 * @since 2020/02/26
 */
public class SquirrelJMEAlterSuiteListener
	implements IAlterSuiteListener
{
	/** The service resource file. */
	public static final String SERVICE_RESOURCE =
		"META-INF/services/net.multiphasicapps.tac.TestInterface";
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/26
	 */
	@Override
	public void alter(List<XmlSuite> suites)
	{
		// Poke the native bindings class so it gets initializes and setup
		new NativeBinding();
		
		// Go through our test suites
		for (XmlSuite suite : suites)
		{
			// Only adjust SquirrelJME suites
			if (!"squirreljme".equals(suite.getName()))
				continue;
			
			// Get pre-existing test group
			XmlTest test = null;
			for (XmlTest sub : suite.getTests())
				if ("tests".equals(sub.getName()))
				{
					test = sub;
					break;
				}
			
			// Create one?
			if (test == null)
			{
				test = new XmlTest();
				test.setSuite(suite);
				test.setName("tests");
				
				suite.addTest(test);
			}
			
			// Add tests for every test class (each class has just one)
			List<XmlClass> classies = new LinkedList<>();
			for (String testClass : findTestClasses())
				try
				{
					XmlClass classy = new XmlClass();
					
					// It needs to point to our class!
					classy.setName(testClass);
					classy.setClass(Class.forName(testClass));
					
					// Use this test
					classies.add(classy);
				}
				catch (ClassNotFoundException e)
				{
					// Ignore
				}
			
			// Use these classes
			test.setClasses(classies);
		}
	}
	
	/**
	 * Returns the base name for the class.
	 *
	 * @param __name The name of the class.
	 * @return The base name.
	 * @since 2020/02/26
	 */
	public static String baseClassName(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("No name specified.");
		
		int ld = __name.lastIndexOf('.');
		return (ld < 0 ? __name : __name.substring(ld + 1));
	}
	
	/**
	 * Returns the base package for the class.
	 *
	 * @param __name The name of the class.
	 * @return The base package.
	 * @since 2020/02/26
	 */
	public static String basePackage(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("No name specified.");
		
		int ld = __name.lastIndexOf('.');
		return (ld < 0 ? __name : __name.substring(0, ld));
	}
	
	/**
	 * Collects all of the test classes that are known about.
	 *
	 * @return The test classes.
	 * @since 2020/02/26
	 */
	public static Iterable<String> findTestClasses()
	{
		Collection<String> rv = new LinkedList<>();
		
		// Process resources
		try
		{
			// Get all resource files
			Enumeration<URL> urls = SquirrelJMEAlterSuiteListener.class.
				getClassLoader().getResources(SERVICE_RESOURCE);
			
			// Process them all
			while (urls.hasMoreElements())
			{
				URL url = urls.nextElement();
				
				// Read in files
				try (BufferedReader br = new BufferedReader(
					new InputStreamReader(url.openStream(),
					"utf-8")))
				{
					for (;;)
					{
						String ln = br.readLine();
						
						// EOF?
						if (ln == null)
							break;
						
						// Ignore blank lines
						if (ln.isEmpty())
							continue;
						
						// Store test name
						rv.add(ln.replace('/', '.'));
					}
				}
				catch (IOException e)
				{
					// Ignore
				}
			}
		}
		catch (IOException e)
		{
			// Ignore
		}
		
		return rv;
	}
}
