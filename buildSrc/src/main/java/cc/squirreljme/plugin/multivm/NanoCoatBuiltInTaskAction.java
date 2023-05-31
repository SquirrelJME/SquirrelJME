// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
		
		// This could fail to write
		try
		{
			// Make sure the target directories exist first
			Files.createDirectories(output);
			
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
					
					// Determine the base file name
					String fileName = entry.getName();
					int lastSlash = fileName.lastIndexOf('/');
					String baseName = (lastSlash >= 0 ?
						fileName.substring(lastSlash + 1) :
						fileName);
					
					// Dump everything into a temporary file first
					Path tempFile = null;
					try
					{
						// Create target file to write to
						tempFile = Files.createTempFile("source",
							".x");
						
						// Dump all the input into the output
						try (OutputStream out = Files.newOutputStream(tempFile,
							StandardOpenOption.TRUNCATE_EXISTING,
							StandardOpenOption.WRITE,
							StandardOpenOption.CREATE))
						{
							// Copy everything over
							for (;;)
							{
								int rc = in.read(buf);
								
								if (rc < 0)
									break;
								
								out.write(buf, 0, rc);
							}
						}
						
						// Copy over to target
						Files.move(tempFile, output.resolve(baseName),
							StandardCopyOption.REPLACE_EXISTING);
					}
					
					// Cleanup after this
					finally
					{
						if (tempFile != null)
							try
							{
								Files.delete(tempFile);
							}
							catch (IOException ignored)
							{
							}
					}
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
