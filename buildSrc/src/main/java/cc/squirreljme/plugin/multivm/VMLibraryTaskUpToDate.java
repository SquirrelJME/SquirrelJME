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
import java.io.File;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.gradle.api.Task;
import org.gradle.api.specs.Spec;

/**
 * This is a check to determine if a task is up-to-date or not for the library
 * compiler.
 *
 * @since 2020/11/28
 */
public class VMLibraryTaskUpToDate
	implements Spec<Task>
{
	/** The classifier used. */
	protected final TargetClassifier classifier;
	
	/**
	 * Initializes the task dependencies.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/28
	 */
	public VMLibraryTaskUpToDate(TargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/28
	 */
	@Override
	public boolean isSatisfiedBy(Task __task)
	{
		// Get the times the output was last changed
		Set<Instant> taskOuts = new HashSet<>(); 
		for (File f : __task.getOutputs().getFiles().getFiles())
			taskOuts.add(Instant.ofEpochMilli(f.lastModified()));
		
		// Determine if any part of the compiler was not considered up-to-date
		for (Task dep : this.classifier.getVmType().processLibraryDependencies(
			(VMExecutableTask)__task, this.classifier.getBangletVariant()))
		{
			for (File f : dep.getOutputs().getFiles().getFiles())
			{
				Instant fileTime = Instant.ofEpochMilli(f.lastModified());
				
				// One of the JARs the compiler uses is newer than the JAR we
				// made, so this is not up-to-date!
				for (Instant taskOut : taskOuts)
					if (fileTime.compareTo(taskOut) > 0)
						return false;
			}
		}
		
		// Considered up to date
		return true;
	}
}
