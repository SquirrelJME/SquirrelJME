// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This is thrown when the index of the string is out of bounds.
 *
 * Note that this class does not exist in the CLDC but is used by methods
 * within the LCDUI code, as such this is here for compatibility purposes. It
 * is highly recommended to not use this exception or to check against this
 * exception.
 *
 * @since 2017/10/21
 */
public class StringIndexOutOfBoundsException
	extends IndexOutOfBoundsException
{
	/**
	 * Initializes the exception with no message.
	 *
	 * @since 2017/10/21
	 */
	public StringIndexOutOfBoundsException()
	{
		super();
	}
	
	/**
	 * Initializes the exception with the given index for specifying out of
	 * bounds strings.
	 *
	 * @param __dx The index of the out of bound string.
	 * @since 2017/10/21
	 */
	public StringIndexOutOfBoundsException(int __dx)
	{
		// {@squirreljme.error EB1s The specified index is not within the
		// bounds of the string. (The string index)}
		super(String.format("EB1s %d", __dx));
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __m The message to use for the string.
	 * @since 2017/10/21
	 */
	public StringIndexOutOfBoundsException(String __m)
	{
		super(__m);
	}
}

