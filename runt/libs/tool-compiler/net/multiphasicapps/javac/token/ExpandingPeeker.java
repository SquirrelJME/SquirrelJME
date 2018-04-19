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

import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * This is essentially a fork of an {@link ExpandingSource} which uses the
 * forward peek operation to obtain tokens as needed. This allows this to be
 * split multiple times.
 *
 * When this is closed the base token source is not closed.
 *
 * @since 2018/04/11
 */
@Deprecated
public final class ExpandingPeeker
	extends ExpandingSource
{
	/** The base source to peek from. */
	protected final ExpandingSource base;
	
	/** The number of tokens returned from the base. */
	protected final int basecount;
	
	/** The number of tokens to forward. */
	int _forward;
	
	/**
	 * Initializes the expanding peeker using the given as a base.
	 *
	 * @param __base The base source.
	 * @param __forward The number of base tokens to peek to start with.
	 * @throws IllegalArgumentException If the forward token count is
	 * negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/11
	 */
	public ExpandingPeeker(ExpandingSource __base, int __forward)
		throws IllegalArgumentException, NullPointerException
	{
		if (__base == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ2v Cannot forward a negative number of
		// tokens.}
		if (__forward < 0)
			throw new IllegalArgumentException("AQ2v");
		
		this.base = __base;
		this.basecount = __base.count();
		this._forward = __forward;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/11
	 */
	@Override
	public final void close()
		throws TokenizerException
	{
		// This does not close the base
	}
	
	/**
	 * Commits all of the tokens which have been peeked to the base tokenizer
	 * so that they are fully consumed.
	 *
	 * @throws ConcurrentModificationException If the source has already been
	 * committed.
	 * @since 2018/04/17
	 */
	public final void commit()
		throws ConcurrentModificationException
	{
		// {@squirreljme.error AQ2w The expanding tokenizer split is not valid
		// because the source
		ExpandingSource base = this.base;
		if (this.basecount != base.count())
			throw new ConcurrentModificationException("AQ2w");
		
		// Consume all of the tokens
		int target = this.basecount + this._forward;
		while (base.count() < target)
			base.next();
	}
	
	/**
	 * {@inheritDoc}
	 * @throws ConcurrentModificationException If the base source returned a
	 * next token when it is not valid to use splits after that has happened.
	 * @since 2018/04/11
	 */
	@Override
	protected final ExpandedToken readNext()
		throws ConcurrentModificationException, TokenizerException
	{
		// {@squirreljme.error AQ2w The expanding tokenizer split is not valid
		// because the source
		ExpandingSource base = this.base;
		if (this.basecount != base.count())
			throw new ConcurrentModificationException("AQ2w");
		
		// Always peek
		return base.peek(this._forward++);
	}
}

