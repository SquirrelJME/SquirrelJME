// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.JavaExec;
import org.gradle.workers.WorkAction;

/**
 * This performs the actual work of running the VM tests.
 *
 * @since 2020/09/07
 */
public abstract class VMTestWorkAction
	implements WorkAction<MultiVMTestParameters>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/09/07
	 */
	@Override
	public void execute()
	{
		MultiVMTestParameters parameters = this.getParameters();
		
		// Debug
		System.err.printf("DEBUG -- Testing %s: %s%n",
			parameters.getTestName().get(),
			parameters.getCommandLine().get());
		
		//throw new Error("TODO");
	}
}
