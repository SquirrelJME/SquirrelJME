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

import net.multiphasicapps.squirreljme.lcdui.DisplayConnector;

public class Alert
	extends Screen
{
	/**
	 * This is delivered to a listener to specify that the alert has been
	 * dismissed.
	 */
	public static final Command DISMISS_COMMAND =
		new Command("", Command.OK, 0);
	
	public static final int FOREVER =
		-2;
	
	/** The title of the alert. */
	private final String _title;
	
	/** The message to display. */
	private volatile String _message;
	
	/** The icon to use. */
	private volatile Image _icon;
	
	/** The type of alert this is. */
	private volatile AlertType _type;
	
	/**
	 * Initializes the alert with just a title.
	 *
	 * @param __title The title of the alert, may be {@code null}.
	 * @since 2017/02/28
	 */
	public Alert(String __title)
	{
		this(__title, null, null, null);
	}
	
	/**
	 * Initializes the alert with just a title, message, image, and type.
	 *
	 * @param __title The title of the alert, may be {@code null}.
	 * @param __message The message to show in the alert, may be {@code null}.
	 * @param __icon The icon to display, may be {@code null}.
	 * @param __type The type of this alert, may be {@code null}.
	 * @since 2017/02/28
	 */
	public Alert(String __title, String __message, Image __icon,
		AlertType __type)
	{
		// Set
		this._title = __title;
		this._message = __message;
		this._icon = __icon;
		this._type = __type;
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
	
	@Override
	public int getHeight()
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
	public int getWidth()
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
	
	/**
	 * Sets the type of this alert.
	 *
	 * @param __t The alert type, may be {@code null}.
	 * @since 2017/02/28
	 */
	public void setType(AlertType __t)
	{
		this._type = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	DisplayConnector __connector()
	{
		throw new Error("TODO");
	}
}


