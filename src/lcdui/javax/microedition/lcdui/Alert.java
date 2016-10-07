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


public class Alert
	extends Screen
{
	public static final Command DISMISS_COMMAND =
		Optional.<Command>ofNullable(null).get();
	
	public static final int FOREVER =
		-2;
	
	public Alert(String __a)
	{
		super();
		throw new Error("TODO");
	}
	
	public Alert(String __a, String __b, Image __c, AlertType __d)
	{
		super();
		throw new Error("TODO");
	}
	
	@Override
	public void addCommand(Command __a)
	{
		throw new Error("TODO");
	}
	
	public int getDefaultTimeout()
	{
		throw new Error("TODO");
	}
	
	public Image getImage()
	{
		throw new Error("TODO");
	}
	
	public Gauge getIndicator()
	{
		throw new Error("TODO");
	}
	
	public String getString()
	{
		throw new Error("TODO");
	}
	
	public int getTimeout()
	{
		throw new Error("TODO");
	}
	
	public AlertType getType()
	{
		throw new Error("TODO");
	}
	
	@Override
	public void removeCommand(Command __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public void setCommandListener(CommandListener __a)
	{
		throw new Error("TODO");
	}
	
	public void setImage(Image __a)
	{
		throw new Error("TODO");
	}
	
	public void setIndicator(Gauge __a)
	{
		throw new Error("TODO");
	}
	
	public void setString(String __a)
	{
		throw new Error("TODO");
	}
	
	public void setTimeout(int __a)
	{
		throw new Error("TODO");
	}
	
	public void setType(AlertType __a)
	{
		throw new Error("TODO");
	}
}


