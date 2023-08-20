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
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.Callable;
import org.gradle.api.Project;

/**
 * Output test directories.
 *
 * @since 2023/08/20
 */
public class VMTestOutputDirs
	implements Callable<Iterable<Path>>
{
	/** The task executing under. */
	protected final VMExecutableTask task;
	
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the handler.
	 * 
	 * @param __task The task testing under.
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/20
	 */
	public VMTestOutputDirs(VMExecutableTask __task,
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
	 * @since 2023/08/20
	 */
	@Override
	public Iterable<Path> call()
	{
		Collection<Path> result = new LinkedHashSet<>();
		
		Project project = this.task.getProject();
		
		// Manual test outputs
		result.add(VMHelpers.testResultsDir(project, this.classifier,
			"manual").get());
		
		return result;
	}
}
