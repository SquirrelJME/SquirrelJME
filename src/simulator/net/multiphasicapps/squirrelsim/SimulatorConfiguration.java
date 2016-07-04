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
public final class SimulatorConfiguration
{
	/** The default system hostname. */
	public static final String DEFAULT_HOSTNAME =
		"squirreljme";
	
	/** The default size of memory. */
	public static final long DEFAULT_MEMORY_SIZE =
		Long.MIN_VALUE;
	
	/** Default instructions per second. */
	public static final long DEFAULT_IPS =
		Long.MIN_VALUE;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The target triplet to simulate. */
	private volatile String _triplet;
	
	/** The amount of memory to use. */
	private volatile long _memory =
		DEFAULT_MEMORY_SIZE;
	
	/** The number of instruction that execute per second. */
	private volatile long _ips =
		DEFAULT_IPS;
	
	/** The hostname to use. */
	private volatile String _hostname =
		DEFAULT_HOSTNAME;
	
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
			
			// {@squirreljme.error BV05 Expected equal sign on input line.
			// (The input line)}
			if (eq < 0)
				throw new IOException(String.format("BV05 %s", ln));
			
			// Set key and value pair
			__set(ln.substring(0, eq).trim(), ln.substring(eq + 1).trim());
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
	private void __set(String __k, String __v)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Depends on the option
		Object lock = this.lock;
		switch (__k)
		{
				// The architecture to simulate
			case "system.triplet":
				__setTriplet(__v);
				break;
				
				// The amount of memory which is available
			case "system.memory":
				__setMemorySize((__v.equals("default") ? DEFAULT_MEMORY_SIZE :
					__decodeBytesLong(__v)));
				break;
				
				// The instructions per second for the given system
			case "system.ips":
				__setIPS((__v.equals("default") ? DEFAULT_IPS :
					__decodeSiLong(__v)));
				break;
				
				// The hostname of the simulated system
			case "system.hostname":
				__setHostName(__v);
				break;
			
				// {@squirreljme.error BV06 Cannot set the given key and value
				// because it is not known. (The key; The value)}
			default:
				throw new IllegalArgumentException(String.format("BV06 %s %s",
					__k, __v));
		}
	}
	
	/**
	 * Sets the hostname of the system being simulated.
	 *
	 * @param __v The simulated hostname.
	 * @return The old hostname
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/14
	 */
	private String __setHostName(String __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			String rv = this._hostname;
			
			this._hostname = __v;
			
			return rv;
		}
	}
	
	/**
	 * This sets the logical number of instructions which execute within a
	 * single second.
	 *
	 * @param __v The number of instructions that execute in a second.
	 * @return The former instructions per second.
	 * @throws IllegalArgumentException If the requested number of instructions
	 * to execute in a single second is zero or negative.
	 * @since 2016/06/14
	 */
	private long __setIPS(long __v)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BV0f Cannot execute zero or negative number of
		// instructions per second. (The requested number of instructions per
		// second)}
		if (__v <= 0 && __v != DEFAULT_IPS)
			throw new IllegalArgumentException(String.format("BV0f %d", __v));
		
		// Lock
		synchronized (this.lock)
		{
			long rv = this._ips;
			
			this._ips = __v;
			
			return rv;
		}
	}
	
	/**
	 * Sets the number of bytes to use for programs which are running.
	 *
	 * @param __b The number of bytes to use per program.
	 * @return The former memory size.
	 * @throws IllegalArgumentException If the size of memory is zero or
	 * negative.
	 * @since 2016/06/14
	 */
	private long __setMemorySize(long __b)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BV0e The size of memory cannot be zero or
		// negative. (The requested size of memory)}
		if (__b <= 0 && __b != DEFAULT_MEMORY_SIZE)
			throw new IllegalArgumentException(String.format("BV0e %d", __b));
		
		// Lock
		synchronized (this.lock)
		{
			long rv = this._memory;
			
			this._memory = __b;
			
			return rv;
		}
	}
	
	/**
	 * Sets the triplet which is to be simulated.
	 *
	 * @param __t The triplet system to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/04
	 */
	private void __setTriplet(String __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._triplet = __t;
		}
	}
	
	/**
	 * Decodes a byte quantity that could either use SI or binary prefixes.
	 *
	 * @param __s The string to decode.
	 * @return The input value.
	 * @throws IllegalArgumentException If the specified input is not a number.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/14
	 */
	private static long __decodeBytesLong(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Could be an illegal number
		try
		{
			// The multiplier and fragment
			long mul = 1L;
			String frag;
			
			// The length must be at least 3 for there to be a prefix
			int n = __s.length();
			if (n >= 2)
			{
				// Try prefixes of all 3 sizes (M, Mi, MiB)
				String got = null;
				long ful = Long.MIN_VALUE;
				for (int i = 3; i >= 1; i--)
				{
					// Too small?
					if (n - i <= 0)
						continue;
					
					// Extract prefix
					String pfx = __s.substring(n - i);
					
					// Depends on the prefix
					switch (pfx)
					{
							// Decimal prefixes
						case "B": ful = 1L; break;
						case "KB":
						case "K":
						case "kB":
						case "k": ful = 1_000L; break;
						case "MB":
						case "M": ful = 1_000_000L; break;
						case "GB":
						case "G": ful = 1_000_000_000L; break;
						case "TB":
						case "T": ful = 1_000_000_000_000L; break;
						case "PB":
						case "P": ful = 1_000_000_000_000_000L; break;
						case "EB":
						case "E": ful = 1_000_000_000_000_000_000L; break;
						
							// Binary prefixes
						case "kiB":
						case "KiB": ful = 1_024L; break;
						case "MiB": ful = 1_048_576L; break;
						case "GiB": ful = 1_073_741_824; break;
						case "TiB": ful = 1_099_511_627_776L; break;
						case "PiB": ful = 1_125_899_906_842_624L; break;
						case "EiB": ful = 1_152_921_504_606_846_976L; break;
						
							// Ignore for now
						default:
							break;
					}
					
					// Found multiplier?
					if (ful >= 0)
					{
						got = pfx;
						break;
					}
				}
				
				// {@squirreljme.error BV0d Could not determine the byte
				// prefix that the input uses. (The input value)}
				if (ful < 0)
					throw new IllegalArgumentException(String.format("BV0d %s",
						__s));
				
				// Set
				mul = ful;
				frag = __s.substring(0, n - got.length());
			}
			
			else
				frag = __s;
			
			// Decode and multiply
			return Long.decode(frag) * mul;
		}
		
		// {@squirreljme.error BV0c The input value is not a number. (The
		// input value)}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(String.format("BV0c %s", __s),
				e);
		}
	}
	
	/**
	 * Decode a string which has an SI prefix.
	 *
	 * @param __s The string to decode.
	 * @return The input value.
	 * @throws IllegalArgumentException If the specified input is not a number.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/14
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
				frag = __s;
			
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

