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

import net.multiphasicapps.javac.CompilerException;

/**
 * This is thrown when there is an issue with the tokenizer.
 *
 * @since 2017/09/05
 */
public class TokenizerException
	extends CompilerException
	implements LineAndColumn
{
	/** The line the tokenizer failed on. */
	protected final int line;
	
	/** The column the tokenizer failed on. */
	protected final int column;
	
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2017/09/05
	 */
	public TokenizerException()
	{
		this((LineAndColumn)null);
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2017/09/05
	 */
	public TokenizerException(String __m)
	{
		this((LineAndColumn)null, __m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2017/09/05
	 */
	public TokenizerException(String __m, Throwable __c)
	{
		this((LineAndColumn)null, __m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2017/09/05
	 */
	public TokenizerException(Throwable __c)
	{
		this((LineAndColumn)null, __c);
	}
	
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @param __lc Line and column information.
	 * @since 2017/09/05
	 */
	public TokenizerException(LineAndColumn __lc)
	{
		if (__lc != null)
		{
			this.line = __lc.line();
			this.column = __lc.column();
		}
		else
		{
			this.line = -1;
			this.column = -1;
		}
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __lc Line and column information.
	 * @param __m The message.
	 * @since 2017/09/05
	 */
	public TokenizerException(LineAndColumn __lc, String __m)
	{
		super(__m);
		
		if (__lc != null)
		{
			this.line = __lc.line();
			this.column = __lc.column();
		}
		else
		{
			this.line = -1;
			this.column = -1;
		}
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __lc Line and column information.
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2017/09/05
	 */
	public TokenizerException(LineAndColumn __lc, String __m, Throwable __c)
	{
		super(__m, __c);
		
		if (__lc != null)
		{
			this.line = __lc.line();
			this.column = __lc.column();
		}
		else
		{
			this.line = -1;
			this.column = -1;
		}
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __lc Line and column information.
	 * @param __c The cause.
	 * @since 2017/09/05
	 */
	public TokenizerException(LineAndColumn __lc, Throwable __c)
	{
		super(__c);
		
		if (__lc != null)
		{
			this.line = __lc.line();
			this.column = __lc.column();
		}
		else
		{
			this.line = -1;
			this.column = -1;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/07
	 */
	@Override
	public final int column()
	{
		return this.line;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/07
	 */
	@Override
	public final int line()
	{
		return this.column;
	}
}

