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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.gradle.api.Task;
import org.gradle.api.specs.Spec;

/**
 * This is used to check if a test is considered invalid because one of the
 * inputs is no longer up to date.
 *
 * @since 2021/02/23
 */
public class VMRunUpToDateWhen
	implements Spec<Task>
{
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the handler.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/02/23
	 */
	public VMRunUpToDateWhen(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/23
	 */
	@Override
	public boolean isSatisfiedBy(Task __task)
	{
		// Get the times the output was last changed
		Set<Instant> taskOuts = new HashSet<>(); 
		for (File f : __task.getOutputs().getFiles().getFiles())
			taskOuts.add(Instant.ofEpochMilli(f.lastModified()));
		
		// Determine if any of our parent dependencies were out of date
		for (Path dep : VMHelpers.runClassPath(__task, this.classifier,
			true))
		{
			Instant fileTime;
			try
			{
				fileTime = Files.getLastModifiedTime(dep).toInstant();
			}
			catch (IOException ignored)
			{
				continue;
			}
			
			// One of our runtime libraries is newer than our own outputs
			for (Instant taskOut : taskOuts)
				if (fileTime.compareTo(taskOut) > 0)
					return false;
		}
		
		// Considered up to date
		return true;
	}
}
