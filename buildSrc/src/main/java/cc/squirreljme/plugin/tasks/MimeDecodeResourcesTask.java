// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import cc.squirreljme.plugin.util.MIMEFileDecoder;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.inject.Inject;
import org.gradle.api.Task;
import org.gradle.language.jvm.tasks.ProcessResources;

public class MimeDecodeResourcesTask
	extends AbstractResourceTask
{
	/** The extension for MIME files. */
	public static final String EXTENSION =
		".__mime";
	
	/**
	 * Initializes the task.
	 *
	 * @param __sourceSet The source set to adjust.
	 * @param __prTask The processing task.
	 * @since 2020/02/28
	 */
	@Inject
	public MimeDecodeResourcesTask(String __sourceSet,
		ProcessResources __prTask)
	{
		super(MimeDecodeResourcesTask.EXTENSION, "", __sourceSet, __prTask);
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Decodes MIME encoded resource files.");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/04
	 */
	@Override
	protected void doTaskAction(Task __task)
	{
		// Anything here can fail for IO operations
		try
		{
			// Process all outputs
			for (__Output__ output : this.taskOutputs())
			{
				Path outPath = output.output;
				
				// Make sure the output directory exists!
				Files.createDirectories(outPath.getParent());
				
				// Copy decoded MIME data to the output file
				try (MIMEFileDecoder in = new MIMEFileDecoder(
					Files.newInputStream(output.input.absolute,
							StandardOpenOption.READ));
					OutputStream out = Files.newOutputStream(outPath,
							StandardOpenOption.CREATE,
							StandardOpenOption.TRUNCATE_EXISTING,
							StandardOpenOption.WRITE))
				{
					byte[] buf = new byte[4096];
					for (;;)
					{
						int rc = in.read(buf);
						
						if (rc < 0)
							break;
						
						out.write(buf, 0, rc);
					}
				}
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
