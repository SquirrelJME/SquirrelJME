// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

/**
 * This is thrown when there is an issue with compilation.
 *
 * @since 2017/09/05
 */
public class CompilerException
	extends RuntimeException
	implements FileNameLineAndColumn
{
	/** The column where the error occured. */
	protected final int column;
	
	/** The line where the error occured. */
	protected final int line;
	
	/** The file being processed. */
	protected final String filename;
	
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2017/09/05
	 */
	public CompilerException()
	{
		this.column = -1;
		this.line = -1;
		this.filename = null;
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2017/09/05
	 */
	public CompilerException(String __m)
	{
		super(__m);
		
		this.column = -1;
		this.line = -1;
		this.filename = null;
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2017/09/05
	 */
	public CompilerException(String __m, Throwable __c)
	{
		super(__m, __c);
		
		this.column = -1;
		this.line = -1;
		this.filename = null;
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2017/09/05
	 */
	public CompilerException(Throwable __c)
	{
		super(__c);
		
		this.column = -1;
		this.line = -1;
		this.filename = null;
	}
	
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @param __la Location awareness information.
	 * @since 2017/03/12
	 */
	public CompilerException(LocationAware __la)
	{
		this.filename = ((__la instanceof FileName) ?
			((FileName)__la).fileName() : null);
		
		if (__la instanceof LineAndColumn)
		{
			LineAndColumn lc = (LineAndColumn)__la;
			this.column = lc.column();
			this.line = lc.line();
		}
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __la Location awareness information.
	 * @param __m The message.
	 * @since 2017/03/12
	 */
	public CompilerException(LocationAware __la, String __m)
	{
		super(__m);
		
		this.filename = ((__la instanceof FileName) ?
			((FileName)__la).fileName() : null);
		
		if (__la instanceof LineAndColumn)
		{
			LineAndColumn lc = (LineAndColumn)__la;
			this.column = lc.column();
			this.line = lc.line();
		}
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __la Location awareness information.
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2017/03/12
	 */
	public CompilerException(LocationAware __la, String __m, Throwable __c)
	{
		super(__m, __c);
		
		this.filename = ((__la instanceof FileName) ?
			((FileName)__la).fileName() : null);
		
		if (__la instanceof LineAndColumn)
		{
			LineAndColumn lc = (LineAndColumn)__la;
			this.column = lc.column();
			this.line = lc.line();
		}
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __la Location awareness information.
	 * @param __c The cause.
	 * @since 2017/03/12
	 */
	public CompilerException(LocationAware __la, Throwable __c)
	{
		super(__c);
		
		this.filename = ((__la instanceof FileName) ?
			((FileName)__la).fileName() : null);
		
		if (__la instanceof LineAndColumn)
		{
			LineAndColumn lc = (LineAndColumn)__la;
			this.column = lc.column();
			this.line = lc.line();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int column()
	{
		return this.column;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final String fileName()
	{
		return this.filename;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int line()
	{
		return this.line;
	}
}

