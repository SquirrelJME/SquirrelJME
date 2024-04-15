// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * This performs the action of dumping a compiled library glob.
 *
 * @since 2021/05/16
 */
public class VMDumpLibraryTaskAction
	implements Action<Task>
{
	/** The classifier used. */
	public final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the task action.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/16
	 */
	public VMDumpLibraryTaskAction(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/05/16
	 */
	@Override
	public void execute(Task __task)
	{
		VMLibraryTaskAction.execute((VMBaseTask)__task, this.classifier,
			this.classifier.getVmType()::dumpLibrary);
	}
}
