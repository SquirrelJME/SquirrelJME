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
import cc.squirreljme.plugin.util.UnassistedLaunchEntry;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;

/**
 * This performs the actual work that is needed to build the ROM.
 *
 * @since 2020/08/23
 */
public class VMRomTaskAction
	implements Action<Task>
{
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the task.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	public VMRomTaskAction(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/23
	 */
	@Override
	public void execute(Task __task)
		throws NullPointerException
	{
		String sourceSet = this.classifier.getSourceSet();
		VMSpecifier vmType = this.classifier.getVmType();
		
		// Is this a single source set ROM?
		boolean isSingleSourceSet = vmType.isSingleSourceSetRom(
			this.classifier.getBangletVariant());
		boolean bootLoaderEnabled = !isSingleSourceSet ||
			(isSingleSourceSet &&
				SourceSet.MAIN_SOURCE_SET_NAME.equals(sourceSet));
		
		Path tempFile = null;
		try
		{
			// We need somewhere safe to store the file
			tempFile = Files.createTempFile(
				vmType.vmName(VMNameFormat.LOWERCASE), sourceSet);
			
			// ROM building parameters
			RomBuildParameters build = new RomBuildParameters();
			
			// The boot libraries which will be placed first always
			Set<Path> bootPaths = new LinkedHashSet<>();
			
			// Get all of the libraries to translate
			Set<Path> normalPaths = new LinkedHashSet<>();
			for (VMLibraryTask task : VMRomDependencies.libraries(__task,
				this.classifier))
			{
				// If we are single source set and this library is another
				// source set, then do not include it here
				String taskSourceSet = task.getSourceSet();
				if (isSingleSourceSet &&
					!sourceSet.equals(taskSourceSet))
					continue;
				
				// Determine the path set
				Set<Path> pathSet = new LinkedHashSet<>();
				for (File f : task.getOutputs().getFiles().getFiles())
					pathSet.add(f.toPath());
				
				// Is this the boot loader library?
				SquirrelJMEPluginConfiguration config =
					SquirrelJMEPluginConfiguration.configurationOrNull(
						task.getProject());
				boolean isMain = SourceSet.MAIN_SOURCE_SET_NAME
					.equals(taskSourceSet);
				boolean isBootLoader = (isMain && config != null &&
					config.isBootLoader);
				boolean isMainLauncher = (isMain && config != null &&
					config.isMainLauncher);
				
				// If this is the boot loader, add our paths
				if (bootLoaderEnabled)
				{
					if (isBootLoader)
					{
						build.bootLoaderMainClass = config.bootLoaderMainClass;
						build.bootLoaderClassPath = pathSet.toArray(
							new Path[pathSet.size()]);
					}
					
					// If this is the launcher, set the information needed to
					// make sure it can actually be launched properly
					if (isMainLauncher)
					{
						UnassistedLaunchEntry entry = config.primaryEntry();
						
						build.launcherMainClass = entry.mainClass;
						build.launcherArgs = entry.args();
						build.launcherClassPath = VMHelpers.runClassPath(task,
							this.classifier);
					}
				}
				
				// Add to the correct set of paths
				if (bootLoaderEnabled && isBootLoader)
					bootPaths.addAll(pathSet);
				else
					normalPaths.addAll(pathSet);
			}
			
			// Make sure the boot libraries are always first
			Set<Path> libPaths = new LinkedHashSet<>();
			if (bootLoaderEnabled)
				libPaths.addAll(bootPaths);
			libPaths.addAll(normalPaths);
			
			// Setup output file for writing
			try (OutputStream out = Files.newOutputStream(tempFile,
				StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.CREATE))
			{
				// Run the ROM processing
				vmType.processRom((VMBaseTask)__task,
					this.classifier.getBangletVariant(), out, build,
					new ArrayList<>(libPaths));
			}
			
			// Recompress it
			VMHelpers.recompressZip(tempFile);
			
			// Move the file over
			Files.move(tempFile,
				__task.getOutputs().getFiles().getSingleFile().toPath(),
				StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e)
		{
			throw new RuntimeException("I/O Error linking ROM for " +
				__task.getProject().getName(), e);
		}
		
		// Always try to cleanup the temporary file
		finally
		{
			if (tempFile != null)
				try
				{
					Files.deleteIfExists(tempFile);
				}
				catch (IOException ignored)
				{
				}
		}
	}
}
