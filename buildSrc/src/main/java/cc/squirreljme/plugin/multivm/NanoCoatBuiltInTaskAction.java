// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
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
 * Copies over the Jar and generates a source file for NanoCoat.
 *
 * @since 2023/05/31
 */
public class NanoCoatBuiltInTaskAction
	implements Action<Task>
{
	/** The input Jar. */
	protected final NanoCoatBuiltInTaskInput input;
	
	/** The output Jar. */
	protected final NanoCoatBuiltInTaskOutput outJar;
	
	/** The output source. */
	protected final NanoCoatBuiltInTaskOutput outSrc;
	
	/** Output tests. */
	protected final NanoCoatBuiltInTaskOutput outTest;
	
	/**
	 * Initializes the task action.
	 *
	 * @param __input The input Jar.
	 * @param __outJar The output Jar.
	 * @param __outSrc The output source.
	 * @param __outTest Optional output test list.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/12
	 */
	public NanoCoatBuiltInTaskAction(NanoCoatBuiltInTaskInput __input,
		NanoCoatBuiltInTaskOutput __outJar, NanoCoatBuiltInTaskOutput __outSrc,
		NanoCoatBuiltInTaskOutput __outTest)
		throws NullPointerException
	{
		if (__input == null || __outJar == null || __outSrc == null)
			throw new NullPointerException("NARG");
		
		this.input = __input;
		this.outJar = __outJar;
		this.outSrc = __outSrc;
		this.outTest = __outTest;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/31
	 */
	@Override
	public void execute(Task __task)
	{
		NanoCoatBuiltInTask task = (NanoCoatBuiltInTask)__task;
		
		// This could fail to write
		Path sourceTemp = null;
		try
		{
			// Copy the Jar over
			Files.copy(this.input.call(), this.outJar.call(),
				StandardCopyOption.REPLACE_EXISTING);
			
			// We will be writing to a file then moving it over, so we only
			// need a single temporary file for everything
			sourceTemp = Files.createTempFile("source", ".x");
			
			// Output tests list?
			NanoCoatBuiltInTaskOutput outTest = this.outTest;
			if (outTest != null)
			{
				// Build list first
				try (OutputStream outRaw = Files.newOutputStream(sourceTemp,
					StandardOpenOption.WRITE,
					StandardOpenOption.TRUNCATE_EXISTING,
					StandardOpenOption.CREATE); 
					PrintStream out = new PrintStream(outRaw,
						true, "utf-8");
					ZipInputStream zip = new ZipInputStream(
						Files.newInputStream(this.input.call(),
							StandardOpenOption.READ)))
				{
					// Grab and copy all the tests
					byte[] buf = new byte[1048576];
					for (;;)
					{
						// Nothing left?
						ZipEntry entry = zip.getNextEntry();
						if (entry == null)
							break;
						
						// Not a test list?
						String name = entry.getName();
						if (!name.endsWith("/META-INF/services/" +
							"net.multiphasicapps.tac.TestInterface"))
							continue;
						
						// Determine Jar name
						String jarName = name.split(Pattern.quote("/"))[1];
						
						// Read in list data
						byte[] listData;
						try (ByteArrayOutputStream baos = 
							new ByteArrayOutputStream())
						{
							for (;;)
							{
								int rc = zip.read(buf);
								if (rc < 0)
									break;
								
								baos.write(buf, 0, rc);
							}
							
							// Take it all
							listData = baos.toByteArray();
						}
						
						// Translate and map tests
						try (BufferedReader br = new BufferedReader(
							new InputStreamReader(
							new ByteArrayInputStream(listData),
								"utf-8")))
						{
							for (;;)
							{
								// EOF?
								String ln = br.readLine();
								if (ln == null || ln.trim().isEmpty())
									break;
								
								// Write out test list
								out.print(jarName);
								out.print(':');
								out.print(ln);
								out.print('\n');
							}
						}
					}
				}
				
				// Move it over
				Files.move(sourceTemp, this.outTest.call(),
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		
		// It did fail to write
		catch (Exception e)
		{
			throw new RuntimeException("Could not copy ROM.", e);
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
}
