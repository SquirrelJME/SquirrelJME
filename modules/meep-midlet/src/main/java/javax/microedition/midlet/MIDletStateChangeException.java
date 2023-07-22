// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;

/**
 * This must be thrown if the state change in a MIDlet has failed, however
 * it is deprecated and no longer should be used.
 *
 * @since 2019/09/25
 */
@Api
@ApiDefinedDeprecated
public class MIDletStateChangeException
	extends Exception
{
	/**
	 * Initializes the exception with no message,
	 *
	 * @since 2019/09/25
	 */
	@Api
	@ApiDefinedDeprecated
	public MIDletStateChangeException()
	{
	}

	/**
	 * Initializes the exception with the given message,
	 *
	 * @param __s The message to use.
	 * @since 2019/09/25
	 */
	@Api
	@ApiDefinedDeprecated
	public MIDletStateChangeException(String __s)
	{
		super(__s);
	}
}

