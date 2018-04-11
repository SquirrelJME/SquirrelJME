// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is an expanded token source which can be given an input queue of
 * tokens which have already been read.
 *
 * @since 2018/03/22
 */
public final class ExpandingStacker
	extends ExpandingSource
{
	/** The iterator for input expanded tokens. */
	protected final Iterator<ExpandedToken> iterator;
	
	/**
	 * Initializes the stacking expander.
	 *
	 * @param __i The input iterable.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/22
	 */
	public ExpandingStacker(Iterable<ExpandedToken> __i)
		throws NullPointerException
	{
		this(__i.iterator());
	}
	
	/**
	 * Initializes the stacking expander.
	 *
	 * @param __i The input iterator.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/22
	 */
	public ExpandingStacker(Iterator<ExpandedToken> __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.iterator = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/22
	 */
	@Override
	public final void close()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/22
	 */
	@Override
	protected final ExpandedToken readNext()
		throws TokenizerException
	{
		try
		{
			return this.iterator.next();
		}
		catch (NoSuchElementException e)
		{
			return null;
		}
	}
}

