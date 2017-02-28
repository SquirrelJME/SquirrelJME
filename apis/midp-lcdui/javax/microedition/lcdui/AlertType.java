// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * This is used as a modifier to alerts to specify the type of message it is
 * along with potentially providing a default sound or icon set.
 *
 * @since 2017/02/28
 */
public class AlertType
{
	/**
	 * This is used to specify that previously requested event is about to
	 * occur.
	 */
	public static final AlertType ALARM =
		new AlertType();
	
	/** This specified that an action has been performed. */
	public static final AlertType CONFIRMATION =
		new AlertType();
	
	/** This specifies an error. */
	public static final AlertType ERROR =
		new AlertType();
	
	/** This specified general information. */
	public static final AlertType INFO =
		new AlertType();
	
	/**
	 * This specifies a warning which may cause the user to proceed with
	 * caution.
	 */
	public static final AlertType WARNING =
		new AlertType();
	
	/**
	 * Initializes the alert type.
	 *
	 * @since 2017/0/228
	 */
	protected AlertType()
	{
	}
	
	public boolean playSound(Display __a)
	{
		throw new todo.TODO();
	}
}


