// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.memory;

/**
 * This is thrown when there is an illegal access to memory.
 *
 * @since 2020/03/03
 */
public class MemoryAccessException
	extends RuntimeException
{
	/** The address attempted to be accessed. */
	public final long address;
	
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2020/03/03
	 */
	public MemoryAccessException(long __addr)
	{
		this.address = __addr;
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2020/03/03
	 */
	public MemoryAccessException(long __addr, String __m)
	{
		super(__m);
		
		this.address = __addr;
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @since 2020/03/03
	 */
	public MemoryAccessException(long __addr, String __m, Throwable __t)
	{
		super(__m, __t);
		
		this.address = __addr;
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @since 2020/03/03
	 */
	public MemoryAccessException(long __addr, Throwable __t)
	{
		super(__t);
		
		this.address = __addr;
	}
}
