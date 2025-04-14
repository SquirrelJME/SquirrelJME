// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Deque;
import net.multiphasicapps.tac.TestConsumer;
import net.multiphasicapps.tac.TestRunnable;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * Base class for running compile tests.
 *
 * @since 2023/10/14
 */
public abstract class BaseRunRoute
	extends TestConsumer<String>
{
	/** The number of libraries used. */
	private static final int _NUM_LIBRARIES =
		3;
	
	/**
	 * Allows for a different backend selection.
	 *
	 * @param __compiler The compiler to use.
	 * @return The returned compiler.
	 * @since 2023/10/15
	 */
	protected Backend backend(String __compiler)
	{
		return Main.findBackend(__compiler);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/10/14
	 */
	@Override
	public void test(String __compiler)
		throws IOException
	{
		// Find backend to use
		Backend backend = this.backend(__compiler);
		
		// Temporary files are created for the JARs for the ROM stage
		Path[] tempFiles = new Path[BaseRunRoute._NUM_LIBRARIES];
		try
		{
			// Setup temporary files
			for (int i = 0; i < BaseRunRoute._NUM_LIBRARIES; i++)
				tempFiles[i] = Files.createTempFile("temp", "" + i);
			
			// Run for multiple libraries
			for (int i = 0; i < BaseRunRoute._NUM_LIBRARIES; i++)
			{
				// Setup AOT Settings
				AOTSettings aotSettings = new AOTSettings(
					__compiler,
					"test" + i,
					"compile",
					(i == 1 ? "test" : "main"),
					"debug",
					"hash" + i,
					"abcd", "abcd");
				
				// Setup arguments
				Deque<String> args = new ArrayDeque<>();
				
				// Build ZIP of entries
				byte[] rawZip;
				try (InputStream rawList = BaseRunRoute.class.
						getResourceAsStream(
							String.format("v-lib/%d.list", i));
					 Reader rawListReader = new InputStreamReader(rawList,
						 "utf-8");
					 BufferedReader list = new BufferedReader(rawListReader);
					 ByteArrayOutputStream zipBaos =
						 new ByteArrayOutputStream();
					 ZipStreamWriter zip = new ZipStreamWriter(zipBaos))
				{
					// Handle all files in the list
					for (;;)
					{
						String ln = list.readLine();
						
						// EOF?
						if (ln == null)
							break;
						
						// Ignore blank lines
						ln = ln.trim();
						if (ln.isEmpty())
							continue;
						
						// Copy entry into the ZIP
						try (InputStream from = BaseRunRoute.class.
							getResourceAsStream(
								String.format("v-lib/%d/%s", i, ln));
							OutputStream into = zip.nextEntry(ln))
						{
							StreamUtils.copy(from, into);
						}
					}
					
					// Ensure the ZIP is actually finished
					zip.close();
					zip.flush();
					
					// Get the output ZIP data
					rawZip = zipBaos.toByteArray();
				}
				
				// Run compilation step
				try (OutputStream out = Files.newOutputStream(tempFiles[i],
					StandardOpenOption.CREATE,
					StandardOpenOption.WRITE,
					StandardOpenOption.TRUNCATE_EXISTING);
					InputStream zip = new ByteArrayInputStream(rawZip))
				{
					// Run compilation step
					Main.mainCompile(aotSettings,
						backend,
						zip,
						out,
						args);
					
					// Note
					this.secondary(String.format("compile-%d", i),
						Files.size(tempFiles[i]) > 0);
				}
			}
			
			// Run final linking stage for the ROM
			try (ByteArrayOutputStream out = new ByteArrayOutputStream())
			{
				// Setup AOT Settings
				AOTSettings aotSettings = new AOTSettings(
					__compiler,
					"test",
					"compile",
					"main",
					"debug",
					"hash",
					"abcd", "abcd");
				
				// Setup arguments
				Deque<String> args = new ArrayDeque<>();
				
				// Arguments are full paths to the temporary files
				for (int i = 0; i < BaseRunRoute._NUM_LIBRARIES; i++)
					args.addLast(tempFiles[i].toAbsolutePath().toString());
				
				// Perform ROM step
				Main.mainRom(aotSettings,
					backend,
					out,
					args);
				
				// Get compiled ROM
				byte[] rom = out.toByteArray();
				
				// Should have size
				this.secondary("rom", rom.length > 0);
			}
		}
		
		// Cleanup temp files
		finally
		{
			for (int i = 0; i < BaseRunRoute._NUM_LIBRARIES; i++)
				if (tempFiles[i] != null)
					try
					{
						Files.delete(tempFiles[i]);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
		}
	}
}
