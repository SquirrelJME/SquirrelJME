// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Utilities for handling ADF files.
 *
 * @since 2023/04/10
 */
final class __AdfUtils__
{
	/**
	 * Not used.
	 * 
	 * @since 2023/04/10
	 */
	private __AdfUtils__()
	{
	}
	
	/**
	 * Parses an ADF binary descriptor.
	 * 
	 * @param __outProps The output ADF properties. 
	 * @param __in The input data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/04/10
	 */
	static void __parseAdfBinary(Map<String, String> __outProps,
		InputStream __in)
		throws IOException, NullPointerException
	{
		if (__outProps == null || __in == null)
			throw new NullPointerException("NARG");
		
		// This is binary data
		DataInputStream in = new DataInputStream(__in);
		
		throw Debugging.todo();
	}
	
	/**
	 * Parses text based ADF properties.
	 * 
	 * @param __outProps The output properties.
	 * @param __in The input data stream.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/04/10
	 */
	static void __parseAdfText(Map<String, String> __outProps,
		InputStream __in)
		throws IOException, NullPointerException
	{
		if (__outProps == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Load in the JAM (Is encoded in Japanese)
		try (BufferedReader jamBr = new BufferedReader(
			new InputStreamReader(__in, "shift-jis")))
		{
			for (;;)
			{
				// EOF?
				String ln = jamBr.readLine();
				if (ln == null)
					break;
				
				// No equal sign, ignore
				int eq = ln.indexOf('=');
				if (eq < 0)
					continue;
				
				// Split into key and value form
				String key = ln.substring(0, eq).trim();
				String val = ln.substring(eq + 1).trim();
				
				// Store into if the key is valid
				if (!key.isEmpty())
					__outProps.put(key, val);
			}
		}
	}
}
