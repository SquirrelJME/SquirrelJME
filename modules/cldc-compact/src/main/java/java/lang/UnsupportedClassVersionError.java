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
 * This is thrown when the version of a class is not supported.
 *
 * @since 2018/12/04
 */
public class UnsupportedClassVersionError
	extends ClassFormatError
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/12/04
	 */
	public UnsupportedClassVersionError()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @since 2018/12/04
	 */
	public UnsupportedClassVersionError(String __m)
	{
		super(__m);
	}
}

