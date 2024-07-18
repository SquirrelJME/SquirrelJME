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

@Api
public class Alert
	extends Screen
{
	/**
	 * This is delivered to a listener to specify that the alert has been
	 * dismissed.
	 */
	@Api
	public static final Command DISMISS_COMMAND =
		new Command("Okay", Command.OK, 0, true);
	
	/** Specifies that the alert should last forever. */
	@Api
	public static final int FOREVER =
		-2;
	
	/** The message to display. */
	volatile String _message;
	
	/** The image to use. */
	volatile Image _image;
	
	/** The type of alert this is. */
	volatile AlertType _type;
	
	/** The duration the alert should last in milliseconds. */
	volatile int _timeout = Alert.FOREVER;
	
	/**
	 * Initializes the alert with just a title.
	 *
	 * @param __title The title of the alert, may be {@code null}.
	 * @since 2017/02/28
	 */
	@Api
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
	@Api
	public Alert(String __title, String __message, Image __icon,
		AlertType __type)
	{
		this._message = __message;
		this._image = __icon;
		this._type = __type;
		
		// Set titles
		throw Debugging.todo();
		/*
		this._userTitle = __title;
		if (__title != null)
			this._displayTitle = __title;
			
		 */
	}
	
	@Override
	public void addCommand(Command __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getDefaultTimeout()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getHeight()
	{
		throw Debugging.todo();
		/*
		return this.__defaultHeight();
		*/
	}
	
	@Api
	public Image getImage()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Gauge getIndicator()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getString()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getTimeout()
	{
		throw Debugging.todo();
	}
	
	@Api
	public AlertType getType()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getWidth()
	{
		throw Debugging.todo();
		/*
		return this.__defaultWidth();
		*/
	}
	
	@Override
	public void removeCommand(Command __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setCommandListener(CommandListener __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the image to be displayed for this alert. If the image is mutable
	 * then this will take a snapshot of the image and use that snapshot
	 * instead of the normal image.
	 *
	 * A new snapshot from a mutable image can be created by performing:
	 * {@code alert.setImage(alert.getImage())}.
	 *
	 * @param __i The image to set or {@code null} to clear it.
	 * @since 2018/04/06
	 */
	@Api
	public void setImage(Image __i)
	{
		throw Debugging.todo();
		/*
		Image clone = (__i != null && __i.isMutable() ?
			Image.createImage(__i) : __i);
		LcdServiceCall.voidCall(LcdFunction.SET_IMAGE, this._handle,
			(__i == null ? -1 : __i._handle),
			(clone == null ? -1 : clone._handle));
		this._image = __i;
		*/
	}
	
	@Api
	public void setIndicator(Gauge __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the message which is used in the alert.
	 *
	 * @param __s The message to use.
	 * @since 2017/02/28
	 */
	@Api
	public void setString(String __s)
	{
		throw Debugging.todo();
		/*
		this._message = __s;
		
		todo.DEBUG.note("Set alert message: %s%n", __s);
		*/
	}
	
	/**
	 * Sets the duration for which the alert should last.
	 *
	 * @param __ms The number of milliseconds to display the message for or
	 * {@link #FOREVER}.
	 * @throws IllegalArgumentException If the duration is not positive and is
	 * not {@link #FOREVER}.
	 * @since 2017/02/28
	 */
	@Api
	public void setTimeout(int __ms)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB19 The specified number of milliseconds is
		negative. (The number of milliseconds specified)} */
		if (__ms < 0 && __ms != Alert.FOREVER)
			throw new IllegalArgumentException(String.format("EB19 %d", __ms));
		
		// Set
		this._timeout = __ms;
	}
	
	/**
	 * Sets the type of this alert.
	 *
	 * @param __t The alert type, may be {@code null}.
	 * @since 2017/02/28
	 */
	@Api
	public void setType(AlertType __t)
	{
		throw Debugging.todo();
		/*
		this._type = __t;
		*/
	}
	
}


