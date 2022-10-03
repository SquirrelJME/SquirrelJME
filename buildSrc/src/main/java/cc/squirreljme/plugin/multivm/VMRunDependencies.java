// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import java.util.concurrent.Callable;

/**
 * This is the set of dependencies for {@link VMRunTask} which takes all
 * of the dependencies directly needed in order to run the program.
 *
 * @since 2020/08/15
 */
public final class VMRunDependencies
	implements Callable<Iterable<VMLibraryTask>>
{
	/** The task executing under. */
	protected final VMExecutableTask task;
	
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the provider.
	 * 
	 * @param __task The task working under.
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public VMRunDependencies(VMExecutableTask __task,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__task == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/15
	 */
	@Override
	public final Iterable<VMLibraryTask> call()
	{
		VMExecutableTask task = this.task;
		return VMHelpers.<VMLibraryTask>resolveProjectTasks(
			VMLibraryTask.class, task.getProject(),
			VMHelpers.runClassTasks(this.task.getProject(),
				this.classifier, true));
	}
}
