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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Deque;
import net.multiphasicapps.tac.TestConsumer;
import net.multiphasicapps.tac.TestRunnable;

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
	 * {@inheritDoc}
	 * @since 2023/10/14
	 */
	@Override
	public void test(String __compiler)
		throws IOException
	{
		// Find backend to use
		Backend backend = Main.findBackend(__compiler);
		
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
					"main",
					"debug",
					"hash" + i);
				
				// Setup arguments
				Deque<String> args = new ArrayDeque<>();
				
				// Build ZIP of entries
				if (true)
					throw Debugging.todo();
				
				// Run compilation step
				try (OutputStream out = Files.newOutputStream(tempFiles[i],
					StandardOpenOption.CREATE_NEW,
					StandardOpenOption.WRITE,
					StandardOpenOption.TRUNCATE_EXISTING))
				{
					// Run compilation step
					Main.mainCompile(aotSettings,
						backend,
						null/*zip*/,
						out,
						args);
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
					"hash");
				
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
