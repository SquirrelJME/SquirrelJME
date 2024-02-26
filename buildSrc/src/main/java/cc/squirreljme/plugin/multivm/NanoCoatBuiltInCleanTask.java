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
import java.io.IOException;
import java.nio.file.Files;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;

/**
 * Cleans the NanoCoat built-in extracted code.
 *
 * @since 2023/09/03
 */
public class NanoCoatBuiltInCleanTask
	extends DefaultTask
	implements VMBaseTask
{
	/** The classifier used. */
	@Getter
	private final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the full suite task.
	 * 
	 * @param __classifier The classifier used.
	 * @param __builtInTask The task used to create the ROM.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	@Inject
	public NanoCoatBuiltInCleanTask(SourceTargetClassifier __classifier, 
		NanoCoatBuiltInTask __builtInTask)
		throws NullPointerException
	{
		if (__classifier == null || __builtInTask == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
		
		// Set details of this task
		this.setGroup("squirreljmeGeneral");
		this.setDescription(String.format("Cleans the built-in outputs of %s.",
			__builtInTask.getName()));
		
		// This is up-to-date if the target directory is missing 
		this.getOutputs().upToDateWhen((__task) ->
			{
				return !Files.exists(__builtInTask.specificPath().get());
			});
		
		// Performs the cleaning accordingly
		this.doLast(new NanoCoatBuiltInCleanTaskAction(__classifier,
			__builtInTask.romBasePath(), __builtInTask.specificPath(),
			__builtInTask.sharedPath()));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/03
	 */
	@Override
	public SourceTargetClassifier getClassifier()
	{
		throw new Error("TODO");
	}
}
