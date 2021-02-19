// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.ld.mem;

/**
 * This is thrown when there is an error accessing memory.
 *
 * @since 2021/02/18
 */
public class MemoryAccessException
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 * 
	 * @param __addr The address.
	 * @since 2021/02/18
	 */
	public MemoryAccessException(long __addr)
	{
		super(Long.toString(__addr, 16));
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __addr The address.
	 * @param __c The cause.
	 * @since 2021/02/18
	 */
	public MemoryAccessException(long __addr, Throwable __c)
	{
		super(Long.toString(__addr, 16), __c);
	}
}
