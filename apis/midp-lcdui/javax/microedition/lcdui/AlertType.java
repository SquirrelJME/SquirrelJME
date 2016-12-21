// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;


public class AlertType
{
	public static final AlertType ALARM =
		new AlertType();
	
	public static final AlertType CONFIRMATION =
		new AlertType();
	
	public static final AlertType ERROR =
		new AlertType();
	
	public static final AlertType INFO =
		new AlertType();
	
	public static final AlertType WARNING =
		new AlertType();
	
	protected AlertType()
	{
		super();
		throw new Error("TODO");
	}
	
	public boolean playSound(Display __a)
	{
		throw new Error("TODO");
	}
}


