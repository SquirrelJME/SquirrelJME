// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

public class Command
{
	/** Returns the user to the previous screen.. */
	public static final int BACK =
		2;
	
	/** Specified a standard negative to something on the screen. */
	public static final int CANCEL =
		3;
	
	/** A command that is used to exit the application. */
	public static final int EXIT =
		7;
	
	/** A request for on-line help. */
	public static final int HELP =
		5;
	
	/** System specific type. */
	public static final int ITEM =
		8;
	
	/** Specified as a standard affirmative to something on the screen. */
	public static final int OK =
		4;
	
	/** A command which pertains to the current screen. */
	public static final int SCREEN =
		1;
	
	/** A command which will stop an in-progress operation. */
	public static final int STOP =
		6;
	
	/** The first command type. */
	private static final int _FIRST_TYPE =
		SCREEN;
	
	/** The last command type. */
	private static final int _LAST_TYPE =
		ITEM;
	
	/** The short label. */
	private final String _shortlabel;
	
	/** The long label. */
	private final String _longlabel;
	
	/** The image used. */
	private final Image _image;
	
	/** The command type. */
	private final int _type;
	
	/** The priority. */
	private final int _priority;
	
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
	public Command(String __sl, int __type, int __pri)
		throws IllegalArgumentException, NullPointerException
	{
		this(__sl, null, null, __type, __pri);
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
	public Command(String __sl, String __ll, int __type, int __pri)
		throws IllegalArgumentException, NullPointerException
	{
		this(__sl, null, null, __type, __pri);
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
	public Command(String __sl, String __ll, Image __i, int __type, int __pri)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__sl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB0r And invalid command type was specified.
		// (The command type)}
		if (__type < _FIRST_TYPE || __type > _LAST_TYPE)
			throw new IllegalArgumentException(
				String.format("EB0r %d", __type));
		
		// Set
		this._shortlabel = __sl;
		this._longlabel = __ll;
		this._image = __i;
		this._type = __type;
		this._priority = __pri;
	}
	
	public int getCommandType()
	{
		throw new todo.TODO();
	}
	
	public boolean getEnabled()
	{
		throw new todo.TODO();
	}
	
	public Font getFont()
	{
		throw new todo.TODO();
	}
	
	public Image getImage()
	{
		throw new todo.TODO();
	}
	
	public String getLabel()
	{
		throw new todo.TODO();
	}
	
	public String getLongLabel()
	{
		throw new todo.TODO();
	}
	
	public int getPriority()
	{
		throw new todo.TODO();
	}
	
	public void onParentEnabled(boolean __e)
	{
		throw new todo.TODO();
	}
	
	public void setEnabled(boolean __e)
	{
		throw new todo.TODO();
	}
	
	public void setFont(Font __f)
	{
		throw new todo.TODO();
	}
	
	public void setLabel(String __s)
	{
		throw new todo.TODO();
	}
	
	public void setLongLabel(String __s)
	{
		throw new todo.TODO();
	}
	
	public void setImage(Image __i)
	{
		throw new todo.TODO();
	}
}


