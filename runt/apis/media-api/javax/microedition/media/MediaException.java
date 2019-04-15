// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media;

/**
 * This is thrown when there was a problem with the media.
 *
 * @since 2019/04/15
 */
public class MediaException
	extends Exception
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2019/04/15
	 */
	public MediaException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message to use.
	 * @since 2019/04/15
	 */
	public MediaException(String __m)
	{
		super(__m);
	}
}


