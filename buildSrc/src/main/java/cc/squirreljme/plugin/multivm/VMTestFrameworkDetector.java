// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.File;
import java.util.Set;
import lombok.Setter;
import org.gradle.api.internal.file.RelativeFile;
import org.gradle.api.internal.tasks.testing.DefaultTestClassRunInfo;
import org.gradle.api.internal.tasks.testing.TestClassProcessor;
import org.gradle.api.internal.tasks.testing.TestClassRunInfo;
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
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void startDetection(TestClassProcessor __processor)
	{
		// Do we have to do anything here?
		__processor.processTestClass(
			new DefaultTestClassRunInfo("Okay"));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public boolean processTestClass(RelativeFile __testClassFile)
	{
		throw new Error("TODO " + __testClassFile);
	}
}
