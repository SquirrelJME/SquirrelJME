// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.Serializable;
import org.gradle.api.internal.tasks.testing.TestClassProcessor;
import org.gradle.api.internal.tasks.testing.WorkerTestClassProcessorFactory;
import org.gradle.internal.service.ServiceRegistry;

/**
 * Factory for creating test class processors?
 *
 * @since 2022/09/11
 */
public class VMTestFrameworkWorkerTestClassProcessorFactory
	implements WorkerTestClassProcessorFactory, Serializable
{
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public TestClassProcessor create(ServiceRegistry __serviceRegistry)
	{
		throw new Error("TODO");
	}
}
