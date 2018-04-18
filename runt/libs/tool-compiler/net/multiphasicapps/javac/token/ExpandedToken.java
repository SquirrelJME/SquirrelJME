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
import net.multiphasicapps.javac.FileNameLineAndColumn;

/**
 * This is a layered token which contains a basic token along with any comments
 * it may contain.
 *
 * @since 2018/03/12
 */
@Deprecated
public final class ExpandedToken
	implements FileNameLineAndColumn
{
	/** The base token. */
	protected final Token base;
	
	/** Token comments. */
	private final Token[] _comments;
	
	/**
	 * Initializes the layered token.
	 *
	 * @param __t The base token.
	 * @param __c The token comments.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/12
	 */
	public ExpandedToken(Token __t, Token... __c)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this.base = __t;
		
		// Make sure the comments are valid
		this._comments = (__c = (__c == null ? new Token[0] : __c.clone()));
		for (int i = 0, n = __c.length; i < n; i++)
			if (__c[i] == null)
				throw new NullPointerException("NARG");
	}
	
	/**
	 * Returns the token characters.
	 *
	 * @return The token characters.
	 * @since 2018/03/13
	 */
	public final String characters()
	{
		return this.base.characters();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int column()
	{
		return this.base.column();
	}
	
	/**
	 * Returns the comments associated with this token.
	 *
	 * @return The token comments.
	 * @since 2018/03/12
	 */
	public final Token[] comments()
	{
		return this._comments.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final String fileName()
	{
		return this.base.fileName();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int line()
	{
		return this.base.line();
	}
	
	/**
	 * Return the type of token this is.
	 *
	 * @return The token type.
	 * @since 2018/03/12
	 */
	public final TokenType type()
	{
		return this.base.type();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final String toString()
	{
		return this.base.toString();
	}
}

