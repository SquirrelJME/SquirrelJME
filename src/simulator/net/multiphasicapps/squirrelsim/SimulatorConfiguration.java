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
import java.util.ServiceLoader;

/**
 * This class contains a mutable configuration that the simulator will use
 * when it comes to initialization.
 *
 * @since 2016/06/14
 */
public class SimulatorConfiguration
{
	/** CPU providers which are available for usage. */
	private static final ServiceLoader<CPUProvider> _CPU_SERVICES =
		ServiceLoader.<CPUProvider>load(CPUProvider.class);
	
	/** OS providers which may be used. */
	private static final ServiceLoader<OSProvider> _OS_SERVICES =
		ServiceLoader.<OSProvider>load(OSProvider.class);
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The current OS to use. */
	private volatile OSProvider _os;
	
	/** The OS variant. */
	private volatile String _osvar;
	
	/** The current architecture to use. */
	private volatile CPUProvider _cpu;
	
	/** The CPU variant. */
	private volatile String _cpuvar;
	
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
		
		// Split provider and variant
		int vcol = __v.indexOf(':');
		String xpro = (vcol >= 0 ? __v.substring(0, vcol) : __v);
		String xvar = (vcol >= 0 ? __v.substring(vcol + 1) : "");
		
		// Depends on the option
		Object lock = this.lock;
		switch (__k)
		{
				// The architecture to simulate
			case "system.arch":
				{
					// Find service
					CPUProvider cp = null;
					ServiceLoader<CPUProvider> cpusvl = _CPU_SERVICES;
					synchronized (cpusvl)
					{
						for (CPUProvider x : cpusvl)
							if (x.name().equals(xpro))
							{
								cp = x;
								break;
							}
					}
					
					// {@squirreljme.error BV09 Unknown CPU. (The CPU name)}
					if (cp == null)
						throw new IllegalArgumentException(String.format(
							"BV09 %s", __v));
					
					// Set
					synchronized (lock)
					{
						this._cpu = cp;
						this._cpuvar = xvar;
					}
				}
				break;
				
				// The operating system to simulate
			case "system.os":
				{
					// Find service
					OSProvider op = null;
					ServiceLoader<OSProvider> ossvl = _OS_SERVICES;
					synchronized (ossvl)
					{
						for (OSProvider x : ossvl)
							if (x.name().equals(xpro))
							{
								op = x;
								break;
							}
					}
					
					// {@squirreljme.error BV0a Unknown operating system.
					// (The operating system name)}
					if (op == null)
						throw new IllegalArgumentException(String.format(
							"BV0a %s", __v));
					
					// Set
					synchronized (lock)
					{
						this._os = op;
						this._osvar = xvar;
					}
				}
				break;
				
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

