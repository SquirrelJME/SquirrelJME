// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.util.TestDetection;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Setter;
import org.gradle.api.internal.file.RelativeFile;
import org.gradle.api.internal.tasks.testing.DefaultTestClassRunInfo;
import org.gradle.api.internal.tasks.testing.TestClassProcessor;
import org.gradle.api.internal.tasks.testing.detection.TestFrameworkDetector;

/**
 * Detects tests in the framework.
 *
 * @since 2022/09/11
 */
public class VMTestFrameworkDetector
	implements TestFrameworkDetector
{
	/** The test classes within. */
	@Setter
	protected Set<File> testClasses;
	
	/** The classpath to use for tests. */
	@Setter
	protected Set<File> testClasspath;
	
	/** The tests which are available. */
	protected final Map<String, CandidateTestFiles> tests;
	
	/**
	 * Initializes the framework detector.
	 * 
	 * @param __tests The tests that are available.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/11
	 */
	public VMTestFrameworkDetector(Map<String, CandidateTestFiles> __tests)
		throws NullPointerException
	{
		if (__tests == null)
			throw new NullPointerException("NARG");
		
		this.tests = __tests;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void startDetection(TestClassProcessor __processor)
	{
		// Go through the available tests and register each one individually
		Set<String> didBaseTest = new HashSet<>();
		for (String test : this.tests.keySet())
		{
			// If this is a variant of a test, include the base test so
			// Gradle actually knows it exists
			int atSign = test.lastIndexOf('@');
			if (atSign >= 0)
			{
				String baseTest = test.substring(0, atSign);
				
				// If not added already, put it in
				if (!didBaseTest.contains(baseTest))
				{
					__processor.processTestClass(
						new DefaultTestClassRunInfo(baseTest));
					didBaseTest.add(baseTest);
				}
			}
			
			// Add test
			else
				__processor.processTestClass(
					new DefaultTestClassRunInfo(test));
		}
	}
	
	/**
	 * This is asking if the given test is something that would be run by
	 * this framework, in which case TAC is simple and is just prefixes and
	 * such.
	 * 
	 * The actual output of this we probably do not really care for at all.
	 * 
	 * @param __testClassFile The test class.
	 * @return If this is a test.
	 * @since 2022/09/11
	 */
	@Override
	public boolean processTestClass(RelativeFile __testClassFile)
	{
		// This will give for example io/MarkableInputStreamTest.class
		String path = __testClassFile.getRelativePath().getPathString();
		
		// Remove the suffix if there is any
		if (path.endsWith(".class"))
			path = path.substring(0, path.length() - ".class".length());
		
		// If there is an at sign in this test then remove it since we
		// only care for the base name
		int lastSlash = path.lastIndexOf('/');
		int atSign = path.lastIndexOf('@');
		if (atSign >= 0 && atSign > lastSlash)
			path = path.substring(0, atSign);
		
		// Remove the suffix if there is any, after @ removal since it may
		// potentially appear twice
		if (path.endsWith(".class"))
			path = path.substring(0, path.length() - ".class".length());
		
		// Normalize name for SquirrelJME, then check if this is something
		// we care about
		return TestDetection.isTest(path.replace('/', '.'));
	}
}
