// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.gradle.api.Action;
import org.gradle.api.internal.tasks.testing.TestFramework;
import org.gradle.api.internal.tasks.testing.WorkerTestClassProcessorFactory;
import org.gradle.api.internal.tasks.testing.detection.TestFrameworkDetector;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.testing.TestFrameworkOptions;
import org.gradle.process.internal.worker.WorkerProcessBuilder;

/**
 * Test framework for SquirrelJME.
 *
 * @since 2022/09/11
 */
public class VMTestFramework
	implements TestFramework
{
	/** The name of this framework. */
	public final String declaredDisplayName =
		"SquirrelJME Test Framework";
	
	/** Is this framework immutable? */
	@Internal
	public final boolean immutable =
		false;
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public TestFrameworkDetector getDetector()
	{
		return new VMTestFrameworkDetector();
	}
	
	/**
	 * Is this an immutable test framework?
	 * 
	 * @return If this is immutable.
	 * @since 2022/09/11
	 */
	public boolean getImmutable()
	{
		return this.immutable;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public TestFrameworkOptions getOptions()
	{
		return new TestFrameworkOptions();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public WorkerTestClassProcessorFactory getProcessorFactory()
	{
		return new VMTestFrameworkWorkerTestClassProcessorFactory();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public Action<WorkerProcessBuilder> getWorkerConfigurationAction()
	{
		return new VMTestFrameworkWorkerConfigurationAction();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public List<String> getTestWorkerImplementationModules()
	{
		// No modules are used
		return Collections.emptyList();
	}
}
