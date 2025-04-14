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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.gradle.language.jvm.tasks.ProcessResources;

/**
 * Calculates the outputs for the resource collation.
 *
 * @since 2024/03/04
 */
public class CollateResourceJarsTaskOutputs
	implements Callable<Set<Path>>
{
	/** The collation task. */
	protected final CollateResourceJarsTask collateTask;
	
	/** The resource processing task. */
	protected final ProcessResources processResources;
	
	/** The output root for the Jar. */
	protected final String jarRoot;
	
	/**
	 * Initializes the task outputs.
	 *
	 * @param __collateTask The collation task.
	 * @param __processResources The resource processing task.
	 * @param __jarRoot The Jar Root used.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/04
	 */
	public CollateResourceJarsTaskOutputs(
		CollateResourceJarsTask __collateTask,
		ProcessResources __processResources, String __jarRoot)
		throws NullPointerException
	{
		if (__collateTask == null || __processResources == null ||
			__jarRoot == null)
			throw new NullPointerException("NARG");
		
		this.collateTask = __collateTask;
		this.processResources = __processResources;
		this.jarRoot = __jarRoot;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/04
	 */
	@Override
	public Set<Path> call()
		throws Exception
	{
		Path outBase = this.processResources.getOutputs().
			getFiles().getSingleFile().toPath().resolve(this.jarRoot);
			
		Set<Path> result = new LinkedHashSet<>();
		
		// Suites list
		result.add(outBase.resolve("suites.list"));
		
		// Process each JAR
		for (File jarFile : this.collateTask.getInputs().getFiles().getFiles())
		{
			Path jarPath = jarFile.toPath();
			Path jarName = jarPath.getFileName();
			Path jarOut = outBase.resolve(jarName);
			
			// JAR contents
			result.add(jarOut.resolve("META-INF")
				.resolve("squirreljme").resolve("resources.list"));
			
			// Contents of the JAR
			try (InputStream jarIn = Files.newInputStream(jarPath,
				StandardOpenOption.READ);
				ZipInputStream jar = new ZipInputStream(jarIn))
			{
				for (;;)
				{
					ZipEntry entry = jar.getNextEntry();
					if (entry == null)
						break;
					
					if (entry.isDirectory())
						continue;
					
					// Where is this file going?
					result.add(CollateResourceJarsTaskAction.nameToDiskFile(
						jarOut, entry.getName()));
				}
			}
		}
		
		return result;
	}
}
