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
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2017/03/12
	 */
	public TokenizerException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2017/03/12
	 */
	public TokenizerException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2017/03/12
	 */
	public TokenizerException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2017/03/12
	 */
	public TokenizerException(Throwable __c)
	{
		super(__c);
	}
	
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @param __lc The line and column information.
	 * @since 2017/03/12
	 */
	public TokenizerException(LineAndColumn __lc)
	{
		super(__lc);
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __lc The line and column information.
	 * @param __m The message.
	 * @since 2017/03/12
	 */
	public TokenizerException(LineAndColumn __lc, String __m)
	{
		super(__lc, __m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __lc The line and column information.
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2017/03/12
	 */
	public TokenizerException(LineAndColumn __lc, String __m, Throwable __c)
	{
		super(__lc, __m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __lc The line and column information.
	 * @param __c The cause.
	 * @since 2017/03/12
	 */
	public TokenizerException(LineAndColumn __lc, Throwable __c)
	{
		super(__lc, __c);
	}
}

