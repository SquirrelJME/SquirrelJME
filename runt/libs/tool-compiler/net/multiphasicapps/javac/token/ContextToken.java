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
 * This represents a context sensitive token.
 *
 * @since 2018/03/07
 */
public final class ContextToken
	implements LineAndColumn
{
	/** The area the token was read from. */
	protected final ContextArea area;
	
	/** The type of token that was read. */
	protected final ContextType type;
	
	/** The token text. */
	protected final String text;
	
	/** The line the token was read on. */
	protected final int line;
	
	/** The column the token was read on. */
	protected final int column;
	
	/** Comments attached to this token. */
	private final String[] _comments;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the token.
	 *
	 * @param __area The area the token was read from.
	 * @param __type The type of token that was read.
	 * @param __text The text for the token data.
	 * @param __lin The line the column was read from.
	 * @param __col The column the token was read from.
	 * @param __comments Comments associated with this token.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __comments}.
	 * @since 2018/03/07
	 */
	public ContextToken(ContextArea __area, ContextType __type, String __text,
		int __lin, int __col, String[] __comments)
		throws NullPointerException
	{
		if (__area == null || __type == null || __type == null)
			throw new NullPointerException("NARG");
		
		this.area = __area;
		this.type = __type;
		this.text = __text;
		this.line = __lin;
		this.column = __col;
		this._comments = (__comments == null ? new String[0] :
			__comments.clone());
	}
	
	/**
	 * Returns the area the token was read from.
	 *
	 * @return The area the token was read from.
	 * @since 2018/03/07
	 */
	public final ContextArea area()
	{
		return this.area;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/07
	 */
	@Override
	public final int column()
	{
		return this.column;
	}
	
	/**
	 * Returns the comments which precede this token.
	 *
	 * @return The comments that precede this token.
	 * @since 2018/03/07
	 */
	public final String[] comments()
	{
		return this._comments.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/07
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/07
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/07
	 */
	@Override
	public final int line()
	{
		return this.line;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/07
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"%s(%s)@%d,%d: %s", this.type, this.area, this.line,
				this.column, this.text)));
		
		return rv;
	}
	
	/**
	 * Returns the type of context sensitive this token is.
	 *
	 * @return The token type.
	 * @since 2018/03/07
	 */
	public final ContextType type()
	{
		return this.type;
	}
}

