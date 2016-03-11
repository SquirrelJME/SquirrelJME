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
	 * This is thrown when there is no value for the given bits.
	 *
	 * @since 2016/03/10
	 */
	public static class NoValueForBits
		extends InflaterException
	{
	}
}

