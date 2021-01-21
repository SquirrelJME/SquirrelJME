// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.io.ChunkForwardedFuture;
import net.multiphasicapps.io.ChunkFuture;
import net.multiphasicapps.io.ChunkFutureInteger;

/**
 * Represents a pointer within the boot JAR, used to refer to where the JAR
 * is located in memory.
 *
 * @since 2021/01/18
 */
public final class BootJarPointer
{
	/** The offset used, which may be a future. */
	protected final ChunkFuture value;
	
	/**
	 * Initializes the Boot Jar Pointer.
	 * 
	 * @param __value The value to set.
	 * @since 2021/01/18
	 */
	public BootJarPointer(int __value)
	{
		this(new ChunkFutureInteger(__value));
	}
	
	/**
	 * Initializes the Boot Jar Pointer.
	 * 
	 * @param __value The value to set.
	 * @param __off Optional offset.
	 * @since 2021/01/20
	 */
	public BootJarPointer(int __value, ChunkFuture __off)
	{
		this(new ChunkFutureInteger(__value), __off);
	}
	
	/**
	 * Initializes the Boot Jar Pointer.
	 * 
	 * @param __value The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/18
	 */
	public BootJarPointer(ChunkFuture __value)
		throws NullPointerException
	{
		this(__value, null);
	}
	
	/**
	 * Initializes the Boot Jar Pointer.
	 * 
	 * @param __value The value to set.
	 * @param __off Optional offset.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/20
	 */
	public BootJarPointer(ChunkFuture __value, ChunkFuture __off)
		throws NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		this.value = (__off == null ? __value :
			new ChunkForwardedFuture(__value, __off));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/18
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/18
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/18
	 */
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
}
