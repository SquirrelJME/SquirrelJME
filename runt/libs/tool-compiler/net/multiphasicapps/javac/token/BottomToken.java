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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a single token which contains a type and the characters
 * which make up the token.
 *
 * @since 2017/09/04
 */
public final class BottomToken
{
	/** The type of token this is, */
	protected final BottomTokenType type;
	
	/** The token string data. */
	protected final String chars;
	
	/** The line the token is on. */
	protected final int line;
	
	/** The column the token is on. */
	protected final int column;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the token.
	 *
	 * @param __t The type of token this is.
	 * @param __c The characters which make up the token.
	 * @param __l The line the token is on.
	 * @param __o The column the token is on.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/06
	 */
	public BottomToken(BottomTokenType __t, String __c, int __l, int __o)
		throws NullPointerException
	{
		// Check
		if (__t == null || __c == null)
			throw new NullPointerException("NARG");
		
		this.type = __t;
		this.chars = __c;
		this.line = __l;
		this.column = __o;
	}
	
	/**
	 * Returns the token characters.
	 *
	 * @return The token characters.
	 * @since 2017/09/06
	 */
	public String characters()
	{
		return this.chars;
	}
	
	/**
	 * Returns the column this token is on.
	 *
	 * @return The column the token is on.
	 * @since 2017/09/09
	 */
	public int column()
	{
		return this.column;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/04
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof BottomToken))
			return false;
		
		BottomToken o = (BottomToken)__o;
		return this.type.equals(o.type) &&
			this.chars.equals(o.chars) &&
			this.line == o.line &&
			this.column == o.column;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/04
	 */
	@Override
	public int hashCode()
	{
		return this.type.hashCode() ^ this.chars.hashCode() ^
			this.line ^ (~this.column);
	}
	
	/**
	 * Returns the line this token is on.
	 *
	 * @return The line the token is on.
	 * @since 2017/09/09
	 */
	public int line()
	{
		return this.line;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/04
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"%s@%d,%d: %s", this.type, this.line, this.column,
				this.chars)));
		
		return rv;
	}
	
	/**
	 * Returns the type of token this is.
	 *
	 * @return The token type.
	 * @since 2017/09/06
	 */
	public BottomTokenType type()
	{
		return this.type;
	}
}

