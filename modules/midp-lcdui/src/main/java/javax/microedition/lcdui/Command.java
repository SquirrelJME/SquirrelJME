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
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayManager;
import cc.squirreljme.runtime.lcdui.scritchui.MenuAction;
import cc.squirreljme.runtime.lcdui.scritchui.MenuLayoutItem;

@Api
public class Command
	extends MenuAction<MenuLayoutItem>
{ 
	/** Returns the user to the previous screen.. */
	@Api
	public static final int BACK =
		2;
	
	/** Specified a standard negative to something on the screen. */
	@Api
	public static final int CANCEL =
		3;
	
	/** A command that is used to exit the application. */
	@Api
	public static final int EXIT =
		7;
	
	/** A request for on-line help. */
	@Api
	public static final int HELP =
		5;
	
	/** System specific type. */
	@Api
	public static final int ITEM =
		8;
	
	/** Specified as a standard affirmative to something on the screen. */
	@Api
	public static final int OK =
		4;
	
	/** A command which pertains to the current screen. */
	@Api
	public static final int SCREEN =
		1;
	
	/** A command which will stop an in-progress operation. */
	@Api
	public static final int STOP =
		6;
	
	/** The first command type. */
	private static final int _FIRST_TYPE = Command.SCREEN;
	
	/** The last command type. */
	private static final int _LAST_TYPE = Command.ITEM;
	
	/** The command type. */
	final int _type;
	
	/** The priority. */
	@ImplementationNote("In SquirrelJME if the priority is " +
		"Integer.MAX_VALUE then it will not be displayed.")
	private final int _priority;
	
	/** Is this an implementation specific command with fixed text? */
	private final boolean _implspec;
	
	/**
	 * Creates a new command with the specified parameters.
	 *
	 * @param __sl The short label of the command.
	 * @param __type The type of command this is.
	 * @param __pri The priority of the command.
	 * @throws IllegalArgumentException If the command type is not valid.
	 * @throws NullPointerException If no short label was specified.
	 * @since 2017/02/28
	 */
	@Api
	public Command(String __sl, int __type, int __pri)
		throws IllegalArgumentException, NullPointerException
	{
		this(__sl, null, null, __type, __pri, false);
	}
	
	/**
	 * Creates a new command with the specified parameters.
	 *
	 * @param __sl The short label of the command.
	 * @param __type The type of command this is.
	 * @param __pri The priority of the command.
	 * @param __implspec If true this is an implementation specific command
	 * which the returned labels are always blank except that internally they
	 * use the passed strings.
	 * @throws IllegalArgumentException If the command type is not valid.
	 * @throws NullPointerException If no short label was specified.
	 * @since 2017/02/28
	 */
	Command(String __sl, int __type, int __pri, boolean __implspec)
		throws IllegalArgumentException, NullPointerException
	{
		this(__sl, null, null, __type, __pri, __implspec);
	}
	
	/**
	 * Creates a new command with the specified parameters.
	 *
	 * @param __sl The short label of the command.
	 * @param __ll The long label of the command, may be {@code null}.
	 * @param __type The type of command this is.
	 * @param __pri The priority of the command.
	 * @throws IllegalArgumentException If the command type is not valid.
	 * @throws NullPointerException If no short label was specified.
	 * @since 2017/02/28
	 */
	@Api
	public Command(String __sl, String __ll, int __type, int __pri)
		throws IllegalArgumentException, NullPointerException
	{
		this(__sl, null, null, __type, __pri, false);
	}
	
	/**
	 * Creates a new command with the specified parameters.
	 *
	 * @param __sl The short label of the command.
	 * @param __ll The long label of the command, may be {@code null}.
	 * @param __i The image used on the command, may be {@code null}.
	 * @param __type The type of command this is.
	 * @param __pri The priority of the command.
	 * @throws IllegalArgumentException If the command type is not valid.
	 * @throws NullPointerException If no short label was specified.
	 * @since 2017/02/28
	 */
	@Api
	public Command(String __sl, String __ll, Image __i, int __type, int __pri)
		throws IllegalArgumentException, NullPointerException
	{
		this(__sl, __ll, __i, __type, __pri, false);
	}
	
	/**
	 * Creates a new command with the specified parameters.
	 *
	 * @param __sl The short label of the command.
	 * @param __ll The long label of the command, may be {@code null}.
	 * @param __i The image used on the command, may be {@code null}.
	 * @param __type The type of command this is.
	 * @param __pri The priority of the command.
	 * @param __implspec If true this is an implementation specific command
	 * which the returned labels are always blank except that internally they
	 * use the passed strings.
	 * @throws IllegalArgumentException If the command type is not valid.
	 * @throws NullPointerException If no short label was specified.
	 * @since 2018/03/29
	 */
	Command(String __sl, String __ll, Image __i, int __type, int __pri,
		boolean __implspec)
		throws IllegalArgumentException, NullPointerException
	{
		super(new MenuLayoutItem(DisplayManager.instance().scritch()
			.eventLoop()), __sl, __ll, __i);
		
		// Check
		if (__sl == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EB1f And invalid command type was specified.
		(The command type)} */
		if (__type < Command._FIRST_TYPE || __type > Command._LAST_TYPE)
			throw new IllegalArgumentException(
				String.format("EB1f %d", __type));
		
		// Set
		this._implspec = __implspec;
		this._type = __type;
		this._priority = __pri;
	}
	
	/**
	 * Returns the type of command this is.
	 *
	 * @return The command type.
	 * @since 2018/03/31
	 */
	@Api
	public int getCommandType()
	{
		return this._type;
	}
	
	@Api
	public boolean getEnabled()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the font that is used as a hint for rendering the text in
	 * a command, it may be disregarded by the implementation.
	 *
	 * @return The font that should be used as a hint.
	 * @since 2018/03/31
	 */
	@Api
	public Font getFont()
	{
		throw Debugging.todo();
		/*
		return this._font;
		*/
	}
	
	/**
	 * Returns the image the command.
	 *
	 * @return The image of the command or {@code null} if it has none.
	 * @since 2018/03/29
	 */
	@Api
	public Image getImage()
	{
		// Do not provide implementation specific images
		if (this._implspec)
			return null;
		
		throw Debugging.todo();
	}
	
	/**
	 * Returns the label used for this command.
	 *
	 * @return The label used for the command.
	 * @since 2018/03/29
	 */
	@Api
	public String getLabel()
	{
		// Do not provide implementation specific labels
		if (this._implspec)
			return "";
		
		throw Debugging.todo();
		/*
		return this._shortLabel;
		
		 */
	}
	
	/**
	 * Returns the long label of the command.
	 *
	 * @return The long label of the command or {@code null} if it has none.
	 * @since 2018/03/29
	 */
	@Api
	public String getLongLabel()
	{
		// Do not provide implementation specific labels
		if (this._implspec)
			return null;
		
		throw Debugging.todo();
		/*
		return this._longLabel;
		
		 */
	}
	
	/**
	 * Returns the priority of the command.
	 *
	 * @return The command priority.
	 * @since 2018/12/03
	 */
	@Api
	public int getPriority()
	{
		return this._priority;
	}
	
	/**
	 * This is called when the enabled state of the parent has changed.
	 *
	 * @param __e The new state of the parent.
	 * @since 2018/04/01
	 */
	@Override
	public void onParentEnabled(boolean __e)
	{
		// The default implementation does nothing
	}
	
	/**
	 * Sets whether this command is enabled or disabled.
	 *
	 * @param __e If the command is enabled or not.
	 * @since 2018/04/01
	 */
	@Api
	public void setEnabled(boolean __e)
	{
		// Do nothing for implementation specific commands
		if (this._implspec)
			return;
		
		throw Debugging.todo();
	}
	
	/**
	 * Sets the font used to be used as a hint when rendering the command.
	 *
	 * @param __f The font to use as a hint when rendering the command,
	 * {@code null} will use the default.
	 * @since 2018/03/31
	 */
	@Api
	public void setFont(Font __f)
	{
		// Do nothing for implementation specific commands
		if (this._implspec)
			return;
		
		throw Debugging.todo();
		/*
		// Just cache the font but do nothing as it is not supported in
		// SquirrelJME (it would complicate command handling) although it
		// could potentially be supported in the future for stuff such as
		// word processors and such
		this._font = __f;
		*/
	}
	
	/**
	 * Sets the image to be displayed for this command. If the image is mutable
	 * then this will take a snapshot of the image and use that snapshot
	 * instead of the normal image.
	 *
	 * A new snapshot from a mutable image can be created by performing:
	 * {@code command.setImage(command.getImage())}.
	 *
	 * @param __i The image to set or {@code null} to clear it.
	 * @since 2018/04/06
	 */
	@Api
	public void setImage(Image __i)
	{
		throw Debugging.todo();
		/*
		// Do nothing for implementation specific commands
		if (this._implspec)
			return;
		
		Image clone = (__i != null && __i.isMutable() ?
			Image.createImage(__i) : __i);
		LcdServiceCall.voidCall(LcdFunction.SET_IMAGE, this._handle,
			(__i == null ? -1 : __i._handle),
			(clone == null ? -1 : clone._handle));
		this._image = __i;
		*/
	}
	
	/**
	 * Sets the label to be displayed.
	 *
	 * @param __s The label to display.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/29
	 */
	@Api
	public void setLabel(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Do nothing for implementation specific commands
		if (this._implspec)
			return;
		
		throw Debugging.todo();
		/*
		this.__setLabels(__s, this._longLabel, this._image);
		
		 */
	}
	
	/**
	 * Sets the long label of the command.
	 *
	 * @param __s The long label to use, {@code null} clears it.
	 * @since 2018/03/29
	 */
	@Api
	public void setLongLabel(String __s)
	{
		// Do nothing for implementation specific commands
		if (this._implspec)
			return;
		
		throw Debugging.todo();
		/*
		this.__setLabels(this._shortLabel, __s, this._image);
		
		 */
	}
}


