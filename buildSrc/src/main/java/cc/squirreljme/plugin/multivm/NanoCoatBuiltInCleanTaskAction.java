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
import java.nio.file.Path;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;

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
	
	/** The ROM base path. */
	private final Provider<Path> _romBasePath;
	
	/** The shared path. */
	private final Provider<Path> _sharedPath;
	
	/** The specific path. */
	private final Provider<Path> _specificPath;
	
	/**
	 * Initializes the cleaning action.
	 *
	 * @param __classifier The classifier of what this is cleaning for.
	 * @param __romBasePath The base ROM path.
	 * @param __specificPath The specific module path.
	 * @param __sharedPath The shared path.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	public NanoCoatBuiltInCleanTaskAction(SourceTargetClassifier __classifier,
		Provider<Path> __romBasePath, Provider<Path> __specificPath,
		Provider<Path> __sharedPath)
		throws NullPointerException
	{
		if (__classifier == null || __romBasePath == null ||
			__specificPath == null || __sharedPath == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
		this._romBasePath = __romBasePath;
		this._specificPath = __specificPath;
		this._sharedPath = __sharedPath;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/03
	 */
	@Override
	public void execute(Task __task)
	{
		//throw new Error("TODO");
	}
}
