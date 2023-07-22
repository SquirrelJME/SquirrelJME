// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.TargetClassifier;
import java.util.concurrent.Callable;
import org.gradle.api.Task;

/**
 * Dependencies for the VM library building task.
 *
 * @since 2020/11/21
 */
public class VMLibraryTaskDependencies
	implements Callable<Iterable<Task>>
{
	/** The task this is for. */
	protected final VMLibraryTask task;
	
	/** The classifier used. */
	protected final TargetClassifier classifier;
	
	/**
	 * Initializes the task dependencies.
	 * 
	 * @param __task The task owning this.
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/21
	 */
	public VMLibraryTaskDependencies(VMLibraryTask __task,
		TargetClassifier __classifier)
		throws NullPointerException
	{
		if (__task == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/21
	 */
	@Override
	public Iterable<Task> call()
	{
		return this.classifier.getVmType().processLibraryDependencies(
			this.task, this.classifier.getBangletVariant());
	}
}
