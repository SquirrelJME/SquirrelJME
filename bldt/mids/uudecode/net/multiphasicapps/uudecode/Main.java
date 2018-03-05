// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.uudecode;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Deque;
import java.util.LinkedList;
import net.multiphasicapps.io.Base64Alphabet;
import net.multiphasicapps.io.Base64Decoder;
import net.multiphasicapps.io.MIMEFileDecoder;

/**
 * Main entry point class.
 *
 * @since 2018/03/05
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2018/03/05
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Roll into queue
		Deque<String> args = new LinkedList<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.offerLast(a);
		
		// No arguments passed?
		if (args.isEmpty())
		{
			__printHelp();
			System.exit(-1);
		}
		
		// Try to parse command line options
		boolean ignorepadding = false,
			rawdata = true;
__outer:
		for (;;)
		{
			String peek = args.peekFirst();
			switch (peek)
			{
					// Ignore padding
				case "-n":
					ignorepadding = true;
					args.pollFirst();
					break;
					
					// Treat as raw
				case "-r":
					rawdata = true;
					args.pollFirst();
					break;
				
					// Treat rest as files
				case "--":
					args.pollFirst();
					break __outer;
				
					// {@squirreljme.error AZ01 Unknown command line switch.
					// (The switch)}
				default:
					if (peek.startsWith("-"))
						throw new IllegalArgumentException(String.format(
							"AZ01 %d", peek));
					break __outer;
			}
		}
		
		// Go through and handle each file
		OutputStream out = System.out;
		while (!args.isEmpty())
			try (Reader file = new InputStreamReader(Files.newInputStream(
				Paths.get(args.pollFirst()), StandardOpenOption.READ));
				InputStream in = __wrap(file, ignorepadding, rawdata))
			{
				byte[] buf = new byte[512];
				for (;;)
				{
					int rc = in.read(buf);
					
					if (rc < 0)
						break;
					
					out.write(buf, 0, rc);
				}
			}
	}
	
	/**
	 * Prints help.
	 *
	 * @since 2018/03/05
	 */
	private static final void __printHelp()
	{
		PrintStream out = System.err;
		
		out.println("Usage: [-n] [-r] [--] (files...)");
		out.println("  -n: Ignore padding.");
		out.println("  -r: Ignore header, treat as raw base64 MIME data.");
	}
	
	/**
	 * Wraps the input stream.
	 *
	 * @param __in The stream to read from.
	 * @param __ip Ignore padding?
	 * @param __rd Treat as raw base64 data.
	 * @return The wrapped input stream.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/05
	 */
	private static final InputStream __wrap(Reader __in, boolean __ip,
		boolean __rd)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Read of raw data?
		if (__rd)
			return new Base64Decoder(__in, Base64Alphabet.MIME, __ip);
		
		// Read MIME file
		return new MIMEFileDecoder(__in);
	}
}

