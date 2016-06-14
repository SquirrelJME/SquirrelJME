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
	 * @throws IllegalArgumentException If the option is not known.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/14
	 */
	public void set(String __k, String __v)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Depends on the option
		switch (__k)
		{
				// The architecture to simulate
			case "system.arch":
				throw new Error("TODO");
				
				// The operating system to simulate
			case "system.os":
				throw new Error("TODO");
				
				// The amount of memory which is available
			case "system.memory":
				throw new Error("TODO");
				
				// The instructions per second for the given system
			case "system.ips":
				throw new Error("TODO");
				
				// The hostname of the simulated system
			case "system.hostname":
				throw new Error("TODO");
			
				// {@squirreljme.error BV06 Cannot set the given key and value
				// because it is not known. (The key; The value)}
			default:
				throw new IllegalArgumentException(String.format("BV06 %s %s",
					__k, __v));
		}
	}
	
	/**
	 * Decode a string which has an si suffix.
	 *
	 * @param __s The string to decode.
	 * @return The input value.
	 * @throws IllegalArgumentException If the specified input is not a number.
	 * @throws NullPointerException On null arguments.
	 */
	private static long __decodeSiLong(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Could be a bad number
		try
		{
			// Multiplier
			long mul = 1L;
			
			// Input fragment
			String frag;
			
			// The length must be at least 2 for there to be a prefix
			int n = __s.length();
			if (n >= 2)
			{
				// Keep only the numerical part
				frag = __s.substring(0, n - 1);
				
				// Depends on the suffix
				char x;
				switch ((x = __s.charAt(n - 1)))
				{
					case 'h': mul = 10L; break;
					case 'k': mul = 1_000L; break;
					case 'M': mul = 1_000_000L; break;
					case 'G': mul = 1_000_000_000L; break;
					case 'T': mul = 1_000_000_000_000L; break;
					case 'P': mul = 1_000_000_000_000_000L; break;
					case 'E': mul = 1_000_000_000_000_000_000L; break;
					
						// {@squirreljme.error BV08 Unknown integral SI suffix.
						// (The suffix)}
					default:
						throw new IllegalArgumentException(
							String.format("BV08 %c", x));
				}
			}
			
			// A plain number with no prefix
			else
			{
				mul = 1L;
				frag = __s;
			}
			
			// Decode and multiply
			return Long.decode(frag) * mul;
		}
		
		// {@squirreljme.error BV07 The input value is not a number. (The
		// input value)}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(String.format("BV07 %s", __s),
				e);
		}
	}
}

