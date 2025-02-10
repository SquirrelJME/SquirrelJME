// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import java.io.Reader;

/**
 * Same as a {@link LineColumnReader} but includes source information so that
 * the origin file is known to whatever is using this.
 *
 * @param <S> The type of source to use.
 * @since 2014/12/19
 */
public class LineColumnFileReader<S>
	extends LineColumnReader
{
	/** Source type. */
	protected final S source;
	
	/**
	 * Initializes the line and column count with source reader, with a
	 * default tab size.
	 *
	 * @param __s The source file specified, may be {@code null} if unknown.
	 * @param __r Input stream to read from.
	 * @since 2014/12/19
	 */
	public LineColumnFileReader(S __s, Reader __r)
	{
		this(__s, __r, LineColumnReader.DEFAULT);
	}
	
	/**
	 * Initializes the line and column count with source reader.
	 *
	 * @param __s The source file specified, may be {@code null} if unknown.
	 * @param __r Input stream to read from.
	 * @param __ts Size of tabulation characters.
	 * @since 2014/12/19
	 */
	public LineColumnFileReader(S __s, Reader __r, int __ts)
	{
		super(__r, __ts);
		
		// Set
		this.source = __s;
	}
	
	/**
	 * Initializes the line and column count with source reader.
	 *
	 * @param __s The source file specified, may be {@code null} if unknown.
	 * @param __r Input stream to read from.
	 * @param __ts Size of tabulation characters.
	 * @param __sz Size of the underlying buffer.
	 * @since 2014/12/19
	 */
	public LineColumnFileReader(S __s, Reader __r, int __ts, int __sz)
	{
		super(__r, __ts, __sz);
		
		// Set
		this.source = __s;
	}
	
	/**
	 * Returns the source file if it has been specified.
	 *
	 * @return The source file used.
	 * @since 2014/12/19
	 */
	public S getSource()
	{
		return this.source;
	}
}

