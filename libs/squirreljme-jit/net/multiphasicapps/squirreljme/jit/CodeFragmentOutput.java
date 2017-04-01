// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.util.datadeque.ByteDeque;

/**
 * This is a code fragment output which is where generated machine code
 * instructions are placed during method generation.
 *
 * @since 2017/03/18
 */
@Deprecated
public class CodeFragmentOutput
{
	/** The byte deque containing machine code. */
	protected final ByteDeque deque =
		new ByteDeque();
	
	/** The configuration for the output. */
	protected final JITConfig config;
	
	/** Debug counter. */
	private volatile int _debug;
	
	/**
	 * Initializes the code fragment output.
	 *
	 * @param __conf The configuration used for the JIT.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/18
	 */
	public CodeFragmentOutput(JITConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * Appends a single byte to the end of the fragment.
	 *
	 * @param __b The byte to append.
	 * @since 2017/03/18
	 */
	public final void appendByte(byte __b)
	{
		this.deque.offerLast(__b);
		
		System.err.printf("%02x ", __b);
		if (++this._debug >= 4)
		{
			System.err.println();
			this._debug = 0;
		}
	}
	
	/**
	 * Appends a single integer to the end of the fragment.
	 *
	 * @param __v The integer to append.
	 * @since 2017/03/18
	 */
	public final void appendInteger(int __v)
	{
		switch (this.config.endianess())
		{
			case BIG:
				appendByte((byte)((__v >>> 24) & 0xFF));
				appendByte((byte)((__v >>> 16) & 0xFF));
				appendByte((byte)((__v >>> 8) & 0xFF));
				appendByte((byte)((__v) & 0xFF));
				break;
			
			case LITTLE:
				appendByte((byte)((__v) & 0xFF));
				appendByte((byte)((__v >>> 8) & 0xFF));
				appendByte((byte)((__v >>> 16) & 0xFF));
				appendByte((byte)((__v >>> 24) & 0xFF));
				break;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Appends a single long to the end of the fragment.
	 *
	 * @param __v The long to append.
	 * @since 2017/03/18
	 */
	public final void appendLong(long __v)
	{
		switch (this.config.endianess())
		{
			case BIG:
				appendByte((byte)((__v >>> 56) & 0xFF));
				appendByte((byte)((__v >>> 48) & 0xFF));
				appendByte((byte)((__v >>> 40) & 0xFF));
				appendByte((byte)((__v >>> 32) & 0xFF));
				appendByte((byte)((__v >>> 24) & 0xFF));
				appendByte((byte)((__v >>> 16) & 0xFF));
				appendByte((byte)((__v >>> 8) & 0xFF));
				appendByte((byte)((__v) & 0xFF));
				break;
			
			case LITTLE:
				appendByte((byte)((__v) & 0xFF));
				appendByte((byte)((__v >>> 8) & 0xFF));
				appendByte((byte)((__v >>> 16) & 0xFF));
				appendByte((byte)((__v >>> 24) & 0xFF));
				appendByte((byte)((__v >>> 32) & 0xFF));
				appendByte((byte)((__v >>> 40) & 0xFF));
				appendByte((byte)((__v >>> 48) & 0xFF));
				appendByte((byte)((__v >>> 56) & 0xFF));
				break;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Appends a single short to the end of the fragment.
	 *
	 * @param __v The short to append.
	 * @since 2017/03/18
	 */
	public final void appendShort(short __v)
	{
		switch (this.config.endianess())
		{
			case BIG:
				appendByte((byte)((__v >>> 8) & 0xFF));
				appendByte((byte)((__v) & 0xFF));
				break;
			
			case LITTLE:
				appendByte((byte)((__v) & 0xFF));
				appendByte((byte)((__v >>> 8) & 0xFF));
				break;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
}

