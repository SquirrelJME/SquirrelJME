// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * The class provides factory access to the WinterCoat functionalities such
 * as a simulaed virtual environment or exported ROM for user environments.
 *
 * It is also used as a base for developing the JIT before targetting real
 * systems, it is intended to make things much easier in the long run.
 *
 * @since 2018/02/21
 */
public class WinterCoatFactory
	implements Runnable
{
	/** The binary manager since they need to be accessed for building. */
	protected final BinaryManager binarymanager;
	
	/** The command to execute. */
	protected final String command;
	
	/** Arguments to the task command. */
	private final String[] _args;
	
	/**
	 * Initializes the factory.
	 *
	 * @param __bl The binary manager.
	 * @param __args factory arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/21
	 */
	public WinterCoatFactory(BinaryManager __bm, String... __args)
		throws NullPointerException
	{
		if (__bm == null)
			throw new NullPointerException("NARG");
		
		this.binarymanager = __bm;
		
		// Copy arguments for processing
		Deque<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.addLast(a);
		
		// {@squirreljme.error AU10 Expected command for WinterCoat operation.}
		String command = args.pollFirst();
		if (command == null)
			throw new IllegalArgumentException("AU10");
		this.command = command;
		
		// Use remaining arguments as input
		this._args = args.<String>toArray(new String[args.size()]);
	}
	
	/**
	 * Builds the winter coat ROM file.
	 *
	 * @return The bytes which make up the ROM file.
	 * @throws IOException If there was a read error or write error.
	 * @since 2018/02/21
	 */
	public byte[] build()
		throws IOException
	{
		BinaryManager binarymanager = this.binarymanager;
		
		// The ROM is basically just a ZIP file
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Write library entries for each compiled library
			try (ZipStreamWriter zsw = new ZipStreamWriter(baos))
			{
				// The ROM contains every single project compiled into a single
				// binary and as such is fully featured
				for (Binary bin : binarymanager)
				{
					// Debug to indicate where things are
					System.err.printf("AU15 %s%n", bin.name());
					
					// Compile binary and get all the needed dependencies
					Binary deps[] = binarymanager.compile(bin);
					int numdeps = deps.length;
					
					// Split off active binary and the dependencies
					bin = deps[numdeps - 1];
					deps = Arrays.copyOf(deps, numdeps - 1);
					
					throw new todo.TODO();
				}
			}
			
			// This is the generated ROM file.
			return baos.toByteArray();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/21
	 */
	@Override
	public void run()
	{
		// Load arguments into a queue
		Deque<String> args =
			new ArrayDeque<>(Arrays.<String>asList(this._args));
		
		// Depends on the command
		String command = this.command;
		switch (command)
		{
				// Build winter coat ROM
			case "build":
				{
					// {@squirreljme.error AU14 Expected a path to which the
					// target ROM will be stored.}
					String spath = args.removeFirst();
					if (spath == null)
						throw new IllegalArgumentException("AU14");
					Path path = Paths.get(spath);
					
					// Generate ROM, or try to
					Path temp = null;
					try
					{
						// Generate ROM
						byte[] rom = this.build();
						
						// Write ROM to file
						temp = Files.createTempFile("wintercoat", ".rom");
						try (OutputStream os = Files.newOutputStream(temp,
							StandardOpenOption.WRITE,
							StandardOpenOption.TRUNCATE_EXISTING))
						{
							os.write(rom);
						}
						
						// Replace real file
						Files.move(temp, path,
							StandardCopyOption.REPLACE_EXISTING);
					}
				
					// {@squirreljme.error AU13 Could not generate the
					// wintercoat ROM file.}
					catch (IOException e)
					{
						throw new RuntimeException("AU13", e);
					}
					
					// Cleanup temporary file
					finally
					{
						if (temp != null)
							try
							{
								Files.delete(temp);
							}
							catch (IOException e)
							{
							}
					}
				}
				break;
				
				// {@squirreljme.error AU11 The specified wintercoat command is
				// not valid. Valid commands are:
				// build
				// .(The command)}
			case "help":
			default:
				throw new IllegalArgumentException(String.format("AU11 %s",
					command));
		}
	}
}

