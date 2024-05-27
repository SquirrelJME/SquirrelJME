// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rafoces;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Iterator;
import java.util.List;

/**
 * This is a list of chain codes which can be used in a table to repeat
 * duplicated shapes in glyphs.
 *
 * @since 2024/05/27
 */
public final class ChainList
	implements Iterable<ChainCode>
{
	/**
	 * Initializes the chain list.
	 *
	 * @param __codes The chain codes.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/27
	 */
	public ChainList(ChainCode... __codes)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Initializes the chain list.
	 *
	 * @param __codes The chain codes.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/27
	 */
	public ChainList(List<ChainCode> __codes)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/27
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/27
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/27
	 */
	@Override
	public Iterator<ChainCode> iterator()
	{
		throw Debugging.todo();
	}
}
