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
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.Delete;
import org.gradle.api.tasks.TaskContainer;

/**
 * Support for native ports.
 *
 * @since 2023/05/31
 */
public enum NativePortSupport
{
	/** NanoCoat. */
	NANOCOAT
	{
	},
	
	/* End. */
	;
	
	/**
	 * Initializes a task to clean the ROM output.
	 *
	 * @param __nativeTask The native task for output.
	 * @param __classifier The classifier for the output.
	 * @return The task used for cleaning.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	public Task cleanTask(Task __nativeTask,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__nativeTask == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		return NativePortSupport.simpleCleanTask(__nativeTask, __classifier);
	}
	
	/**
	 * Is sequential clean required for this?
	 *
	 * @return If sequential clean is required.
	 * @since 2023/09/03
	 */
	public boolean isSequentialClean()
	{
		return this == NativePortSupport.NANOCOAT;
	}
	
	/**
	 * Initializes a task to clean the ROM output.
	 *
	 * @param __nativeTask The native task for output.
	 * @param __classifier The classifier for the output.
	 * @return The task used for cleaning.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	public static Task simpleCleanTask(Task __nativeTask,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__nativeTask == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		Project project = __nativeTask.getProject();
		TaskContainer tasks = project.getTasks();
		
		// Setup clean task from the output of the ROM task
		Delete cleanTask = tasks.create("clean" +
				TaskInitialization.uppercaseFirst(__nativeTask.getName()),
			Delete.class);
		cleanTask.setGroup("squirreljmegeneral");
		cleanTask.setDescription("Cleans the ROM output from " +
			__nativeTask.getName() + ".");
		cleanTask.delete(project.provider(() -> 
			__nativeTask.getOutputs().getFiles().getSingleFile()));
		
		// The clean task is up-to-date if the files were already
		// deleted or do not exist
		cleanTask.getOutputs().upToDateWhen((__task) -> 
			!__nativeTask.getOutputs().getFiles().getSingleFile()
				.exists());
		
		return cleanTask;
	}
}
