// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelsim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * This class contains a mutable configuration that the simulator will use
 * when it comes to initialization.
 *
 * @since 2016/06/14
 */
public class SimulatorConfiguration
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/**
	 * Initializes the simulator configuration which uses all defaults.
	 *
	 * @since 2016/06/14
	 */
	public SimulatorConfiguration()
	{
		throw new Error("TODO");
	}
	
	/**
	 * This initializes the simulator configuration using the given
	 * configuration file.
	 *
	 * @param __r The configuration file to source from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/14
	 */
	public SimulatorConfiguration(Reader __r)
		throws IOException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Read all input line
		BufferedReader br = new BufferedReader(__r);
		String ln;
		while (null != (ln = br.readLine()))
		{
			// Remove comment (# character that is not preceded by an escape)
			int n = ln.length();
			boolean esc = false;
			for (int i = 0; i < n; i++)
			{
				// Read
				char c = ln.charAt(i);
				
				// Treat escaped character as input
				if (esc)
				{
					esc = false;
					continue;
				}
				
				// Escape?
				else if (c == '\\')
					esc = true;
				
				// Comment?
				else if (c == '#')
				{
					ln = ln.substring(0, i);
					break;
				}
			}
				
			// Trim any extra whitespace at the end
			ln = ln.trim();
			
			// Ignore blank lines
			if (ln.length() <= 0)
				continue;
			
			// Find equal sign
			int eq = ln.indexOf('=');
			
			// {@squirreljme.error Expected equal sign on input line.
			// (The input line)}
			if (eq < 0)
				throw new IOException(String.format("BV05 %s", ln));
			
			// Set key and value pair
			set(ln.substring(0, eq).trim(), ln.substring(eq + 1).trim());
		}
	}
	
	/**
	 * Sets the given setting to the specified value.
	 *
	 * @param __k The key to use.
	 * @param __v The value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/14
	 */
	public void set(String __k, String __v)
		throws NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

