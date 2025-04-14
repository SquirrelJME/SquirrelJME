// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.multivm.VMHelpers;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
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
		VMHelpers.deleteDirTree(__task, outBase);
		
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
			CollateResourceJarsTaskAction.writeList(rcListPath, jarContent);
		}
		
		// Write the suite list
		CollateResourceJarsTaskAction.writeList(
			outBase.resolve("suites.list"), suiteList);
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
	
	/**
	 * Writes a list file.
	 *
	 * @param __target The target file.
	 * @param __input The input string list.
	 * @throws IOException On write errors.
	 * @since 2024/09/07
	 */
	public static void writeList(Path __target, List<String> __input)
		throws IOException
	{
		if (__target == null || __input == null)
			throw new NullPointerException("NARG");
		
		// Write out strings
		byte[] data;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 DataOutputStream dos = new DataOutputStream(baos))
		{
			dos.writeInt(__input.size());
			for (int i = 0, n = __input.size(); i < n; i++)
				dos.writeUTF(__input.get(i));
			
			dos.flush();
			data = baos.toByteArray();
		}
		
		Files.write(__target, data,
			StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING,
			StandardOpenOption.CREATE);
	}
}
