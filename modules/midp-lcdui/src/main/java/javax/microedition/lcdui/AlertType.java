// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is used as a modifier to alerts to specify the type of message it is
 * along with potentially providing a default sound or icon set.
 *
 * @since 2017/02/28
 */
@Api
public class AlertType
{
	/**
	 * This is used to specify that previously requested event is about to
	 * occur.
	 */
	@Api
	public static final AlertType ALARM =
		new AlertType();
	
	/** This specified that an action has been performed. */
	@Api
	public static final AlertType CONFIRMATION =
		new AlertType();
	
	/** This specifies an error. */
	@Api
	public static final AlertType ERROR =
		new AlertType();
	
	/** This specified general information. */
	@Api
	public static final AlertType INFO =
		new AlertType();
	
	/**
	 * This specifies a warning which may cause the user to proceed with
	 * caution.
	 */
	@Api
	public static final AlertType WARNING =
		new AlertType();
	
	/**
	 * Initializes the alert type.
	 *
	 * @since 2017/0/228
	 */
	@Api
	protected AlertType()
	{
	}
	
	@Api
	public boolean playSound(Display __a)
	{
		throw Debugging.todo();
	}
}


