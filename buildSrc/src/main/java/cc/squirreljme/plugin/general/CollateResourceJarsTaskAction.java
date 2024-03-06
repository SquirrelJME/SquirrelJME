// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.language.jvm.tasks.ProcessResources;

/**
 * Performs the task action of {@link CollateResourceJarsTask}.
 *
 * @since 2024/03/04
 */
public class CollateResourceJarsTaskAction
	implements Action<Task>
{
	/** The resource processing task. */
	protected final ProcessResources processResources;
	
	/** The output root for the Jar. */
	protected final String jarRoot;
	
	/**
	 * Initializes the task action.
	 *
	 * @param __processResources The resource processing task.
	 * @param __jarRoot The Jar Root used.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/04
	 */
	public CollateResourceJarsTaskAction(ProcessResources __processResources,
		String __jarRoot)
		throws NullPointerException
	{
		if (__processResources == null ||
			__jarRoot == null)
			throw new NullPointerException("NARG");
		
		this.processResources = __processResources;
		this.jarRoot = __jarRoot;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/04
	 */
	@Override
	public void execute(Task __task)
	{
		try
		{
			this.executeMayFail(__task);
		}
		catch (IOException __e)
		{
			throw new RuntimeException(__e);
		}
	}
	
	/**
	 * Executes task that may fail.
	 *
	 * @param __task The task to execute within.
	 * @throws IOException On write errors.
	 * @since 2024/03/05
	 */
	public void executeMayFail(Task __task)
		throws IOException
	{
		Path outBase = this.processResources.getOutputs().
			getFiles().getSingleFile().toPath().resolve(this.jarRoot);
		
		// Delete old directory set first since it will have a bunch of
		// old files in it and such...
		if (Files.isDirectory(outBase))
		{
			Set<Path> deleteFiles = new LinkedHashSet<>();
			Set<Path> deleteDirs = new LinkedHashSet<>();
			
			try (Stream<Path> walk = Files.walk(outBase))
			{
				walk.forEach((__it) -> {
						if (Files.isDirectory(__it))
							deleteDirs.add(__it);
						else
							deleteFiles.add(__it);
					});
			}
			
			for (Set<Path> rawByes : Arrays.asList(deleteFiles, deleteDirs))
			{
				List<Path> byes = new ArrayList<>(rawByes);
				Collections.reverse(byes);
				
				for (Path bye : byes)
				{
					__task.getLogger().lifecycle(
						String.format("Cleaning %s...", bye));
					
					try
					{
						Files.deleteIfExists(bye);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		
		// Make sure it exists
		Files.createDirectories(outBase);
		
		// JAR suite list
		List<String> suiteList = new ArrayList<>();
		
		// Go through all the files
		byte[] buf = new byte[524288];
		for (File jarFile : __task.getInputs().getFiles())
		{
			// Where is everything?
			Path jarPath = jarFile.toPath();
			Path jarName = jarPath.getFileName();
			
			// Add to the suite list for later
			suiteList.add(jarName.toString());
			
			// Where does the exploded JAR content go?
			Path jarOut = outBase.resolve(jarName);
			
			// Setup base dir
			Files.createDirectories(jarOut);
			
			// Read the ZIP and process
			List<String> jarContent = new ArrayList<>();
			try (InputStream jarIn = Files.newInputStream(jarPath,
					StandardOpenOption.READ);
				 ZipInputStream jar = new ZipInputStream(jarIn))
			{
				for (;;) {
					ZipEntry entry = jar.getNextEntry();
					if (entry == null)
						break;
					
					if (entry.isDirectory())
						continue;
					
					// Record for later
					String name = entry.getName();
					jarContent.add(name);
					
					// Find where it goes
					Path diskFile = CollateResourceJarsTaskAction
						.nameToDiskFile(jarOut, name);
					
					// Setup parent
					Files.createDirectories(diskFile.getParent());
					
					// Note it
					__task.getLogger().lifecycle(
						String.format("Adding %s...",
							outBase.relativize(diskFile)));
					
					// Copy down file
					try (OutputStream out = Files.newOutputStream(diskFile,
							StandardOpenOption.WRITE,
							StandardOpenOption.TRUNCATE_EXISTING,
							StandardOpenOption.CREATE))
					{
						for (;;)
						{
							int rc = jar.read(buf);
							
							if (rc < 0)
								break;
							
							out.write(buf, 0, rc);
						}
					}
				}
			}
			
			// Write resource list
			Path rcListBase =
				jarOut.resolve("META-INF").resolve("squirreljme");
			Path rcListPath =
				rcListBase.resolve("resources.list");
			Files.createDirectories(rcListBase);
			Files.write(rcListPath, jarContent,
				StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.CREATE);
		}
		
		// Write the suite list
		Files.write(outBase.resolve("suites.list"), suiteList,
			StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING,
			StandardOpenOption.CREATE);
	}
	
	/**
	 * Converts a name to a file on the disk.
	 *
	 * @param __jarOut The output Jar.
	 * @param __name The name of the entry.
	 * @return The resultant path for the file.
	 * @since 2024/03/04
	 */
	public static Path nameToDiskFile(Path __jarOut, String __name)
	{
		Path diskFile = __jarOut;
		
		for (String splice : __name.split("[\\\\/]"))
			diskFile = diskFile.resolve(splice);
			
		return diskFile;
	}
}
