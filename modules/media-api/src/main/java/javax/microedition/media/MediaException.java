// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is thrown when there was a problem with the media.
 *
 * @since 2019/04/15
 */
@Api
public class MediaException
	extends Exception
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2019/04/15
	 */
	@Api
	public MediaException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message to use.
	 * @since 2019/04/15
	 */
	@Api
	public MediaException(String __m)
	{
		super(__m);
	}
}


