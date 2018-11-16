// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vmshader;

import cc.squirreljme.builder.support.Binary;
import cc.squirreljme.builder.support.BinaryManager;
import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.TimeSpaceType;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Queue;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This class builds a shaded JAR of the virtual machine which contains the
 * classes for every JAR in SquirrelJME. This just uses the build system to
 * produce a JAR that is combined as one.
 *
 * @since 2018/11/16
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Main entry points.
	 * @since 2018/11/16
	 */
	public static void main(String... __args)
	{
		// Copy arguments for processing
		Queue<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.add(a);
		
		// Setup project manager
		ProjectManager pm = ProjectManager.fromArguments(args);
		
		// Determine the path where our shaded JAR will be built
		Path output = Paths.get((args.isEmpty() ? "squirreljme.jar" :
			args.remove()));
		
		// Build the output JAR at this path
		Path tempfile = null;
		try
		{
			// Build from the test system so we have access to all of those
			// JARs including the tests
			BinaryManager bm = pm.binaryManager(TimeSpaceType.TEST);
			
			// Create temporary file to place the JAR at
			tempfile = Files.createTempFile("squirreljme-", ".ja_");
			
			// Open output ZIP in that spot
			try (ZipStreamWriter zsw = new ZipStreamWriter(
				Files.newOutputStream(tempfile,
					StandardOpenOption.CREATE,
					StandardOpenOption.WRITE,
					StandardOpenOption.TRUNCATE_EXISTING)))
			{
				Main.__shadeJar(zsw, bm);
			}
			
			throw new todo.TODO();
		}
		
		// {@squirreljme.error AE01 Could not build the output shaded JAR.}
		catch (IOException e)
		{
			throw new IllegalArgumentException("AE01", e);
		}
		
		// No matter what, always try to clear the temp file
		finally
		{
			if (tempfile != null)
				try
				{
					Files.delete(tempfile);
				}
				catch (IOException e)
				{
					// Ignore this
				}
		}
	}
	
	/**
	 * Shades the JAR to the specified output.
	 *
	 * @param __zsw The output ZIP to write to.
	 * @param __bm The binary manager with classes.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/16
	 */
	private static final void __shadeJar(ZipStreamWriter __zsw,
		BinaryManager __bm)
		throws IOException, NullPointerException
	{
		if (__zsw == null || __bm == null)
			throw new NullPointerException("NARG");
		
		// Raw suiite
		ByteArrayOutputStream rawsuites = new ByteArrayOutputStream();
		PrintStream suitelist = new PrintStream(rawsuites, true, "utf-8");
		
		// Go through all the binaries and just place them in the shaded JAR
		// with their associated names and prefixes
		for (Binary bin : __bm)
		{
			// Compile the binary
			__bm.compile(bin);
			
			// Use the name for this JAR
			String name = bin.name().toString();
			
			// Write it to the suite list
			suitelist.println(name);
			
			// Base prefix for this JAR
			String base = "__squirreljme/" + name + "/";
			
			// Will be copying every single entry to the output
			byte[] buf = new byte[512];
			try (ZipBlockReader zbr = bin.zipBlock())
			{
				// Copy every single entry to the output
				for (ZipBlockEntry e : zbr)
					try (InputStream is = e.open();
						OutputStream os = __zsw.nextEntry(base + e.name()))
					{
						for (;;)
						{
							int rc = is.read(buf);
							
							if (rc < 0)
								break;
							
							os.write(buf, 0, rc);
						}
					}
			}
		}
		
		// Write the raw suite list to the JAR since it is needed for shading
		try (OutputStream os = __zsw.nextEntry("__squirreljme/suites.list"))
		{
			rawsuites.writeTo(os);
		}
	}
}

