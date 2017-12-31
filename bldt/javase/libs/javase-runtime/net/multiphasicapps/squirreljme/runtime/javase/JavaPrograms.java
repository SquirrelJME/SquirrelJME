// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.squirreljme.runtime.kernel.InstallErrorCodes;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgram;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgramInstallInfo;
import net.multiphasicapps.squirreljme.runtime.kernel.
	KernelProgramInstallReport;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelPrograms;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This manages programs on the Java host.
 *
 * @since 2017/12/14
 */
public class JavaPrograms
	extends KernelPrograms
{
	/** The run-time installation directory. */
	protected final Path runtimepath;
	
	/**
	 * Initializes the Java program manager.
	 *
	 * @since 2017/12/27
	 */
	public JavaPrograms()
	{
		// Always register the system program
		registerProgram(new JavaSystemProgram());
		
		// Determine the run-time path
		Path runtimepath = Paths.get("installed");
		this.runtimepath = runtimepath;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	protected KernelProgramInstallReport accessJitAndInstall(
		KernelProgramInstallInfo __info)
		throws NullPointerException
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		// Determine output path
		Path outjar = runtimepath.resolve(
			Paths.get(String.format("%d.jar", __info.index())));
		
		// Operation may fail
		Path tempfile = null;
		try
		{
			// Write to a temporary JAR first
			tempfile = Files.createTempFile("squirreljme-",
				"install.jar");
			
			// Create parent output directories as needed
			Files.createDirectories(outjar.getParent());
			Files.createDirectories(tempfile.getParent());
			
			// Just copy the contents of the JAR file to the output JAR
			// regardless
			try (ZipStreamWriter out = new ZipStreamWriter(
				Files.newOutputStream(tempfile,
					StandardOpenOption.TRUNCATE_EXISTING,
					StandardOpenOption.WRITE)))
			{
				byte[] buf = new byte[512];
				for (ZipBlockEntry e : __info.zip())
					try (InputStream in = e.open();
						OutputStream os = out.nextEntry(e.name()))
					{
						for (;;)
						{
							int rc = in.read(buf);
							
							if (rc < 0)
								break;
							
							os.write(buf, 0, rc);
						}
					}
			}
			
			// Replace any JAR which was previously installed
			Files.move(tempfile, outjar, StandardCopyOption.REPLACE_EXISTING);
		}
		
		// Failed read/write
		catch (IOException e)
		{
			e.printStackTrace();
			
			return new KernelProgramInstallReport(
				InstallErrorCodes.IO_FILE_ERROR, e.getMessage());
		}
		
		// Cleanup temp file when possible
		finally
		{
			if (tempfile != null)
				try
				{
					Files.delete(tempfile);
				}
				catch (IOException e)
				{
				}
		}
		
		throw new todo.TODO();
	}
}

