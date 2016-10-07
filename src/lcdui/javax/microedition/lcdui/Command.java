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


public class Command
{
	public static final int BACK =
		2;
	
	public static final int CANCEL =
		3;
	
	public static final int EXIT =
		7;
	
	public static final int HELP =
		5;
	
	public static final int ITEM =
		8;
	
	public static final int OK =
		4;
	
	public static final int SCREEN =
		1;
	
	public static final int STOP =
		6;
	
	public Command(String __a, int __b, int __c)
	{
		super();
		throw new Error("TODO");
	}
	
	public Command(String __a, String __b, int __c, int __d)
	{
		super();
		throw new Error("TODO");
	}
	
	public int getCommandType()
	{
		throw new Error("TODO");
	}
	
	public String getLabel()
	{
		throw new Error("TODO");
	}
	
	public String getLongLabel()
	{
		throw new Error("TODO");
	}
	
	public int getPriority()
	{
		throw new Error("TODO");
	}
}


