// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.IOException;

/**
 * This is the base class for any exceptions the inflater may thrown when
 * reading bytes.
 *
 * @since 2016/03/10
 */
public class InflaterException
	extends IOException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2016/03/12
	 */
	public InflaterException()
	{
	}
	
	/**
	 * Initializes the exception with the given cause.
	 *
	 * @param __t The cause of this exception.
	 * @since 2016/03/12
	 */
	public InflaterException(Throwable __t)
	{
		super(__t);
	}
	
	/**
	 * This is thrown when the dynamic huffman alphabet mixing shift is out
	 * of range.
	 *
	 * @since 2016/03/13
	 */
	public static class AlphaShiftOutOfRange
		extends InflaterException
	{
	}
	
	/**
	 * This is thrown when the header of the type error is read.
	 *
	 * @since 2016/03/10
	 */
	public static class HeaderErrorTypeException
		extends InflaterException
	{
	}
	
	/**
	 * This is thrown when a sequence of bits is not legal.
	 *
	 * @since 2016/03/10
	 */
	public static class IllegalSequence
		extends InflaterException
	{
	}
	
	/**
	 * This is thrown when the no compression type has an invalid length, one
	 * where the complement does not match the normal value.
	 *
	 * @since 2016/03/12
	 */
	public static class NoCompressLengthError
		extends InflaterException
	{
	}
	
	/**
	 * This is thrown when there is no value for the given bits.
	 *
	 * @since 2016/03/10
	 */
	public static class NoValueForBits
		extends InflaterException
	{
	}
}

