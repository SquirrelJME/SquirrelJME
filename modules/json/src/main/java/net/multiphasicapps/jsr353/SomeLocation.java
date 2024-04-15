// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import com.oracle.json.stream.JsonLocation;

/**
 * This implements {@link JsonLocation} with fields set by a constructor.
 *
 * @since 2014/08/04
 */
public final class SomeLocation
	implements JsonLocation
{
	/** The column number. */
	private final long _col;
	
	/** The line number. */
	private final long _line;
	
	/** The character number. */
	private final long _char;	
	
	/**
	 * Initializes the location, with nothing valid.
	 *
	 * @since 2014/08/04
	 */
	public SomeLocation()
	{
		this(-1L, -1L);
	}
	
	/**
	 * Initializes all locational values except for the stream value.
	 *
	 * @param __l Line number.
	 * @param __c Column number.
	 * @since 2014/08/04
	 */
	public SomeLocation(long __l, long __c)
	{
		this(__l, __c, -1L);
	}
	
	/**
	 * Initializes all locational values.
	 *
	 * @param __l Line number.
	 * @param __c Column number.
	 * @param __s Bytes or characters into the stream.
	 * @since 2014/08/04
	 */
	public SomeLocation(long __l, long __c, long __s)
	{
		this._line = (__l >= 0L ? __l : -1L);
		this._col = (__c >= 0L ? __c : -1L);
		this._char = (__s >= 0L ? __s : -1L);
	}
	
	/**
	 * Returns the column number for the current event, or {@code -1} if
	 * unknown.
	 *
	 * @return Column number or {@code -1L}.
	 * @since 2014/07/25
	 */
	@Override
	public final long getColumnNumber()
	{
		return this._col;
	}

	/**
	 * Returns the line number for the current event, or {@code -1} if
	 * unknown.
	 *
	 * @return Line number or {@code -1L}.
	 * @since 2014/07/25
	 */
	@Override
	public final long getLineNumber()
	{
		return this._line;
	}

	/**
	 * Returns the stream offset for the current event, or {@code -1} if
	 * unknown.
	 *
	 * @return Stream offset or {@code -1L}.
	 * @since 2014/07/25
	 */
	@Override
	public final long getStreamOffset()
	{
		return this._char;
	}
}
