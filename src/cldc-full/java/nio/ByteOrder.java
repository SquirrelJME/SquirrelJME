// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.nio;

/**
 * This represents the byte order that data may be in.
 *
 * @since 2016/02/27
 */
public final class ByteOrder
{
	/** Big endian byte order. */
	public static final ByteOrder BIG_ENDIAN =
		new ByteOrder("BIG_ENDIAN");
	
	/** Little endian byte order. */
	public static final ByteOrder LITTLE_ENDIAN =
		new ByteOrder("LITTLE_ENDIAN");
	
	/** The string representing the byte order. */
	private final String _string;
	
	/**
	 * Initializes the byte order.
	 *
	 * @param __str String pertaining to the order.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/02/27
	 */
	private ByteOrder(String __str)
		throws NullPointerException
	{
		// Check
		if (__str == null)
			throw new NullPointerException();
		
		// Set
		_string = __str;
	}
	
	/**
	 * Returns the string representing the byte order, either
	 * {@code BIG_ENDIAN} or {@code LITTLE_ENDIAN}.
	 *
	 * @return The byte order's string.
	 * @since 2016/02/27
	 */
	@Override
	public String toString()
	{
		return _string;
	}
	
	public static ByteOrder nativeOrder()
	{
		throw new Error("TODO");
	}
}

