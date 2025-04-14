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
import org.jetbrains.annotations.NotNull;

/**
 * A {@link CharStar} which is backed by a {@link AllocLink}.
 *
 * @since 2023/12/16
 */
public final class LinkedCharStar
	implements CharStarPointer
{
	/** Allocation link. */
	protected final AllocLink link;
	
	/** Char star. */
	protected final CharStar charStar;
	
	/**
	 * Initializes a linked {@link CharStar}.
	 *
	 * @param __link The allocation link used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/16
	 */
	public LinkedCharStar(AllocLink __link)
		throws NullPointerException
	{
		if (__link == null)
			throw new NullPointerException("NARG");
		
		this.link = __link;
		this.charStar = new CharStar(__link.pointerAddress());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/16
	 */
	@Override
	public char charAt(int __dx)
	{
		return this.charStar.charAt(__dx);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/16
	 */
	@Override
	public int length()
	{
		return this.charStar.length();
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
	public CharSequence subSequence(int __start, int __end)
	{
		return this.charStar.subSequence(__start, __end);
	}
}
