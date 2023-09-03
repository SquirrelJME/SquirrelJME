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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * Copies over the source file for NanoCoat.
 *
 * @since 2023/05/31
 */
public class NanoCoatBuiltInTaskAction
	implements Action<Task>
{
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the task.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public NanoCoatBuiltInTaskAction(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/31
	 */
	@Override
	public void execute(Task __task)
	{
		// Where is the ROM going?
		Path input = __task.getInputs().getFiles().getSingleFile().toPath();
		Path output = __task.getOutputs().getFiles().getSingleFile().toPath();
		
		// Shared file output
		Path moduleOutput = output.getParent().resolve("specific")
			.resolve(output.getFileName());
		Path sharedOutput = output.getParent();
		
		// This could fail to write
		Path sourceTemp = null;
		try
		{
			// We will be writing to a file then moving it over, so we only
			// need a single temporary file for everything
			sourceTemp = Files.createTempFile("source", ".x");
			
			// Make sure the target directories exist first, since we just
			// deleted them
			Files.createDirectories(moduleOutput);
			Files.createDirectories(sharedOutput);
			
			// The ROM is just a ZIP of sources which get copied over
			try (InputStream in = Files.newInputStream(input,
					StandardOpenOption.READ);
				ZipInputStream zip = new ZipInputStream(in))
			{
				byte[] buf = new byte[65536];
				for (;;)
				{
					// Load in next entry
					ZipEntry entry = zip.getNextEntry();
					if (entry == null)
						break;
					
					// Ignore directories
					if (entry.isDirectory())
						continue;
					
					// Debug ticker
					System.err.print(".");
					
					// Split as slashes to get directories and whatnot
					String name = entry.getName();
					String[] fragments = name.split(Pattern.quote("/"));
					int numFragments = fragments.length;
					
					// Is this shared data?
					boolean isShared = name.startsWith("shared/");
					
					// Determine what our file is called and whatnot
					Path targetParent = (isShared ? sharedOutput :
						moduleOutput);
					for (int i = 0; i < numFragments - 1; i++)
						targetParent = targetParent.resolve(fragments[i]);
					
					// Make sure directories exist
					Files.createDirectories(targetParent);
					
					// Which file are we writing to?
					String baseName = fragments[numFragments - 1];
					Path targetFile = targetParent.resolve(baseName);
					
					// Dump all the input into the output
					try (OutputStream out = Files.newOutputStream(
						sourceTemp, StandardOpenOption.TRUNCATE_EXISTING,
						StandardOpenOption.WRITE,
						StandardOpenOption.CREATE))
					{
						VMHelpers.copy(zip, out);
					}
					
					// Move over to target, replace if it exists since
					// we want to update it
					Files.move(sourceTemp, targetFile,
						StandardCopyOption.REPLACE_EXISTING);
				}
			}
					
			// Cleanup after this
			finally
			{
				if (sourceTemp != null)
					try
					{
						Files.delete(sourceTemp);
					}
					catch (IOException ignored)
					{
					}
			}
		}
		
		// It did fail to write
		catch (IOException e)
		{
			throw new RuntimeException("Could not copy ROM: " + output, e);
		}
	}
}
