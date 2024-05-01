// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import cc.squirreljme.plugin.util.GradleJavaExecSpecFiller;
import cc.squirreljme.plugin.util.GradleLoggerOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.logging.LogLevel;
import org.gradle.process.ExecResult;

/**
 * Runs the program within the virtual machine.
 *
 * @since 2020/08/07
 */
public class VMRunTaskAction
	implements Action<Task>
{
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the task action.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/16
	 */
	public VMRunTaskAction(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public void execute(Task __task)
	{
		// The task owning this
		VMRunTask runTask = (VMRunTask)__task;
		
		// Need this to get the program details
		SquirrelJMEPluginConfiguration config =
			SquirrelJMEPluginConfiguration.configuration(__task.getProject());
			
		// Gather the class path to use for target execution, this is all the
		// SquirrelJME modules this depends on
		VMSpecifier vmType = this.classifier.getVmType();
		Path[] classPath = VMHelpers.runClassPath(__task,
			this.classifier, true);
		
		// Debug
		__task.getLogger().debug("Classpath: {}", Arrays.asList(classPath));
		
		// Determine the main entry class or MIDlet to use
		JavaMEMidlet midlet = runTask.midlet;
		String mainClass = VMHelpers.mainClass(config, midlet);
		
		// Debug
		__task.getLogger().debug("MIDlet: {}", midlet);
		__task.getLogger().debug("MainClass: {}", mainClass);
		
		// If executing a MIDlet, then the single main argument is the actual
		// name of the MIDlet to execute
		List<String> args = new ArrayList<>();
		if (midlet != null)
			args.add(midlet.mainClass);
		
		// Debug
		__task.getLogger().debug("Target Working Dir: {}",
			System.getProperty("user.dir"));
		
		// Execute the virtual machine, if the exit status is non-zero then
		// the task execution will be considered as a failure
		ExecResult exitResult = __task.getProject().javaexec(__spec ->
			{
				// Use filled JVM arguments
				vmType.spawnJvmArguments((VMBaseTask)__task, true,
					new GradleJavaExecSpecFiller(__spec), mainClass,
					(midlet != null ? midlet.mainClass : mainClass),
					Collections.<String, String>emptyMap(),
					classPath, classPath,
					args.<String>toArray(new String[args.size()]));
				
				// Use these streams directly
				__spec.setStandardOutput(new GradleLoggerOutputStream(
					__task.getLogger(), LogLevel.LIFECYCLE,
					-1, -1));
				__spec.setErrorOutput(new GradleLoggerOutputStream(
					__task.getLogger(), LogLevel.ERROR,
					-1, -1));
			});
		
		// Did the task fail?
		int exitValue = exitResult.getExitValue();
		if (exitValue != 0)
			throw new RuntimeException("Task exited with: " + exitValue);
	}
}
