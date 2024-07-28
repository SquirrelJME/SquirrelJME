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
import cc.squirreljme.plugin.util.GradleLoggerOutputStream;
import cc.squirreljme.plugin.util.UnassistedLaunchEntry;
import cc.squirreljme.plugin.util.GradleJavaExecSpecFiller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.stream.Stream;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.SourceSet;
import org.gradle.process.ExecResult;

/**
 * This is the action for running the full entire suite in the emulator.
 *
 * @since 2020/10/17
 */
public class VMFullSuiteTaskAction
	implements Action<Task>
{
	/** The additional libraries to load. */
	public static final String LIBRARIES_PROPERTY =
		"full.libraries";
	
	/** The source target classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the task.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	public VMFullSuiteTaskAction(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/17
	 */
	@Override
	public void execute(Task __task)
	{
		Project root = __task.getProject().getRootProject();
		
		// We need all the libraries to load and to be available
		Collection<Path> libPath = VMHelpers.fullSuiteLibraries(__task);
		
		// Additional items onto the library set?
		String exLib = System.getProperty(
			VMFullSuiteTaskAction.LIBRARIES_PROPERTY);
		if (exLib != null)
			for (Path p : VMHelpers.classpathDecode(exLib))
			{
				// Add contents of a given directory
				if (Files.isDirectory(p))
					try (Stream<Path> s = Files.list(p))
					{
						libPath.addAll(Arrays.asList(s.toArray(Path[]::new)));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				
				// Add single path
				else
					libPath.add(p);
			}
		
		// Determine the initial classpath of the launcher, which is always
		// ran first
		SourceTargetClassifier withMain = this.classifier
			.withSourceSet(SourceSet.MAIN_SOURCE_SET_NAME);
		Collection<Path> classPath = new LinkedHashSet<>();
		classPath.addAll(Arrays
			.asList(VMHelpers.runClassPath((VMExecutableTask)root.project(
				":modules:launcher").getTasks().getByName(TaskInitialization
					.task("lib", withMain)),
				withMain)));
		
		// Debug these, just to ensure they work
		__task.getLogger().debug("LibPath: {}", libPath);
		__task.getLogger().debug("Classpath: {}", classPath);
		
		// Run the virtual machine with everything
		ExecResult exitResult = __task.getProject().javaexec(__spec ->
			{
				// Use filled JVM arguments
				this.classifier.getVmType().spawnJvmArguments(
					__task.getProject(),
					this.classifier,
					true,
					new GradleJavaExecSpecFiller(__spec),
					UnassistedLaunchEntry.MIDLET_MAIN_CLASS,
					"fullSuite",
					new LinkedHashMap<String, String>(),
					libPath.<Path>toArray(new Path[libPath.size()]),
					classPath.<Path>toArray(new Path[classPath.size()]),
					"cc.squirreljme.runtime.launcher.ui.MidletMain");
				
				// Use these streams directly
				__spec.setStandardOutput(new GradleLoggerOutputStream(
					__task.getLogger(), LogLevel.LIFECYCLE, -1, -1));
				__spec.setErrorOutput(new GradleLoggerOutputStream(
					__task.getLogger(), LogLevel.ERROR, -1, -1));
			});
		
		// Did the task fail?
		int exitValue = exitResult.getExitValue();
		if (exitValue != 0)
			throw new RuntimeException("Task exited with: " + exitValue);
	}
}
