// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.AbstractList;

/**
 * An array of pointers to {@link CharStarPointer}.
 *
 * @since 2023/12/16
 */
public final class CharStarPointerArray
	extends AbstractList<CharStarPointer>
	implements Pointer
{
	/** The size of this list. */
	protected final int size;
	
	/** The data link storing the bytes. */
	protected final AllocLink link;
	
	/**
	 * Initializes the pointer array.
	 *
	 * @param __count The number of entries in the array.
	 * @param __link The link to the memory of the pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/16
	 */
	public CharStarPointerArray(int __count, AllocLink __link)
		throws NullPointerException
	{
		if (__link == null)
			throw new NullPointerException("NARG");
		
		this.size = __count;
		this.link = __link;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/16
	 */
	@Override
	public CharStarPointer get(int __dx)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/16
	 */
	@Override
	public long pointerAddress()
	{
		return this.link.pointerAddress();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/16
	 */
	@Override
	public int size()
	{
		return this.size;
	}
}
