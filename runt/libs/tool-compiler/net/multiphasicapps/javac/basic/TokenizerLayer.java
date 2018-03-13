// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.basic;

import java.util.ArrayDeque;
import java.util.Deque;
import net.multiphasicapps.javac.LineAndColumn;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.Tokenizer;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This layers on top of the tokenizer and allows for special handling of
 * comments and input tokens so that parsing of the input file is made simpler.
 *
 * @since 2018/03/12
 */
public final class TokenizerLayer
	implements LineAndColumn
{
	/** The tokenizer this is laid ontop of. */
	protected final Tokenizer tokenizer;
	
	/** This stores the temporary token queue which allows for peeking. */
	private Deque<Token> _queue =
		new ArrayDeque<>();
	
	/**
	 * This initializes the tokenizer layer which allows for slight
	 * modification of input tokens accordingly.
	 *
	 * @param __t The tokenizer to layer on top.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/12
	 */
	public final TokenizerLayer(Tokenizer __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this.tokenizer = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int column()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int line()
	{
		throw new todo.TODO();
	}
}

