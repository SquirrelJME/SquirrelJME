// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
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
 * Performs the actual cleaning of the NanoCoat Built-In ROM generated sources.
 *
 * @since 2023/09/03
 */
public class NanoCoatBuiltInCleanTaskAction
	implements Action<Task>
{
	/** The classifier for the cleaning. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the cleaning action.
	 *
	 * @param __classifier The classifier of what this is cleaning for.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	public NanoCoatBuiltInCleanTaskAction(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/03
	 */
	@Override
	public void execute(Task __task)
	{
		throw new Error("TODO");
	}
}
