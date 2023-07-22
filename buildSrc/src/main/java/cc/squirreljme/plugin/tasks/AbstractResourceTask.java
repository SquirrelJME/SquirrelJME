// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import cc.squirreljme.plugin.util.FileLocation;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.DirectoryTree;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileCopyDetails;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.SourceSetOutput;
import org.gradle.language.jvm.tasks.ProcessResources;

/**
 * Base class for resource processing as needed.
 *
 * @since 2020/04/04
 */
public abstract class AbstractResourceTask
	extends DefaultTask
{
	/** The file extension. */
	@Internal
	@Getter
	protected final String extension;
	
	/** The source set to modify. */
	@Internal
	@Getter
	protected final String sourceSet;
	
	/** The output extension. */
	@Internal
	@Getter
	protected final String outputExtension;
	
	/**
	 * Initializes the base resource processing task.
	 *
	 * @param __ext The extension to use.
	 * @param __outExt Output extension.
	 * @param __sourceSet The source set.
	 * @param __prTask The process resources task.
	 * @param __cleanTask The task for cleaning.
	 * @since 2020/04/04
	 */
	@Inject
	public AbstractResourceTask(String __ext, String __outExt,
		String __sourceSet, ProcessResources __prTask, Task __cleanTask)
		throws NullPointerException
	{
		if (__ext == null || __outExt == null ||
			__sourceSet == null || __prTask == null || __cleanTask == null)
			throw new NullPointerException("NARG");
		
		this.extension = __ext;
		this.outputExtension = __outExt;
		this.sourceSet = __sourceSet;
		
		// Configure tasks and such
		Project project = this.getProject();
		this.getInputs().files(
			project.provider(this::taskInputsAsFileCollection));
		this.getOutputs().files(
			project.provider(this::taskOutputsAsFileCollection));
		this.onlyIf(this::__onlyIf);
		
		// The action to do
		this.doLast(new __ActionTask__());
		
		// Clean must happen first
		this.mustRunAfter(__cleanTask);
		
		// The process task depends on this task
		__prTask.dependsOn(this);
		
		// Exclude files that this processes
		__prTask.eachFile((FileCopyDetails __fcd) ->
			{
				// Exclude all MIME files from this task
				if (this.isMatchingExtension(__fcd.getFile().toPath()))
					__fcd.exclude();
			});
	}
	
	/**
	 * Performs the task's action.
	 *
	 * @param __task The current task.
	 * @since 2020/04/04
	 */
	protected abstract void doTaskAction(Task __task);
	
	/**
	 * Is this a matching extension?
	 *
	 * @param __path The path to check.
	 * @return If the path matches.
	 * @since 2020/04/04
	 */
	protected final boolean isMatchingExtension(Path __path)
	{
		if (__path == null)
			throw new NullPointerException("No path specified.");
		
		return __path.getFileName().toString().endsWith(this.extension);
	}
	
	/**
	 * Returns the output path.
	 *
	 * @return The output path.
	 * @since 2020/02/28
	 */
	protected final Path outputPath()
	{
		// We might need to set an output potentially
		Project project = this.getProject();
		SourceSetOutput outSet = this.getProject().getConvention()
			.getPlugin(JavaPluginConvention.class)
			.getSourceSets().getByName(this.sourceSet).getOutput();
		
		// The base output directory
		File outDirFile = outSet.getResourcesDir();
		if (outDirFile == null)
			outSet.setResourcesDir((outDirFile = project.getBuildDir()
			.toPath().resolve(this.getClass().getName() + "-" + this.sourceSet)
			.toFile()));
		
		return outDirFile.toPath();
	}
	
	/**
	 * Removes the extension from the file.
	 *
	 * @param __fn The file name to remove.
	 * @return The file name without the extension.
	 * @throws IllegalArgumentException If the file does not have the
	 * extension.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/28
	 */
	protected final Path removeExtension(Path __fn)
		throws IllegalArgumentException, NullPointerException
	{
		if (__fn == null)
			throw new NullPointerException("No file name specified.");
		
		String extension = this.extension;
		
		// Must be a MIME file
		String fileName = __fn.getFileName().toString();
		if (!fileName.endsWith(extension))
			throw new IllegalArgumentException("File does not end in MIME.");
		
		// The output will be a sibling of the path
		return __fn.resolveSibling(fileName.substring(0,
			fileName.length() - extension.length()));
	}
	
	/**
	 * Gets the inputs for the task as fully informed files.
	 *
	 * @return The task inputs.
	 * @since 2020/02/28
	 */
	protected final Iterable<FileLocation> taskInputs()
	{
		Project project = this.getProject();
		
		// Discover all the input files
		Collection<FileLocation> result = new LinkedList<>();
		for (DirectoryTree dir : this.getProject().getConvention().
			getPlugin(JavaPluginConvention.class).getSourceSets().
			getByName(this.sourceSet).getResources().getSrcDirTrees())
		{
			Path baseDir = dir.getDir().toPath();
			
			// Process all files in each directory
			for (File file : project.files(dir))
			{
				Path path = file.toPath();
				
				// Only consider MIME files
				if (!this.isMatchingExtension(path))
					continue;
				
				result.add(new FileLocation(path, baseDir.relativize(path)));
			}
		}
		
		return result;
	}
	
	/**
	 * Gets the inputs for the task.
	 *
	 * @return The task inputs.
	 * @since 2020/02/28
	 */
	protected final FileCollection taskInputsAsFileCollection()
	{
		Collection<File> result = new LinkedList<>();
		for (FileLocation file : this.taskInputs())
			result.add(file.getAbsolute().toFile());
		
		return this.getProject().files(result);
	}
	
	/**
	 * Gets the outputs for the task.
	 *
	 * @return The task outputs.
	 * @since 2020/02/28
	 */
	protected final Iterable<__Output__> taskOutputs()
	{
		// The output path to process
		String outputExtension = this.outputExtension;
		Path outDir = this.outputPath();
		
		// Store output files accordingly
		Collection<__Output__> result = new LinkedList<>();
		for (FileLocation input : this.taskInputs())
			result.add(new __Output__(input, outDir.resolve(
				this.removeExtension(input.getRelative()) + outputExtension)));
		
		return result;
	}
	
	/**
	 * Gets the outputs for the task.
	 *
	 * @return The task outputs.
	 * @since 2020/02/28
	 */
	protected final FileCollection taskOutputsAsFileCollection()
	{
		Collection<File> result = new LinkedList<>();
		for (__Output__ output : this.taskOutputs())
			result.add(output.output.toFile());
		
		return this.getProject().files(result);
	}
	
	/**
	 * Only run the task if certain conditions are met.
	 *
	 * @param __task The task to run.
	 * @return The result if it should run or not.
	 * @since 2020/02/28
	 */
	private boolean __onlyIf(Task __task)
	{
		// There is no point in running this, if nothing can be done
		return !this.taskInputsAsFileCollection().isEmpty();
	}
	
	/**
	 * The action to perform.
	 *
	 * @since 2020/02/29
	 */
	private final class __ActionTask__
		implements Action<Task>
	{
		__ActionTask__()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/02/29
		 */
		@Override
		public void execute(Task __task)
		{
			AbstractResourceTask.this.doTaskAction(__task);
		}
	}
}
