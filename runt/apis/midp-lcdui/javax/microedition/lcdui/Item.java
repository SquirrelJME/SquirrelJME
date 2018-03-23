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


public abstract class Item
	extends __Widget__
{
	public static final int BUTTON =
		2;
	
	public static final int HYPERLINK =
		1;
	
	public static final int LAYOUT_2 =
		16384;
	
	public static final int LAYOUT_BOTTOM =
		32;
	
	public static final int LAYOUT_CENTER =
		3;
	
	public static final int LAYOUT_DEFAULT =
		0;
	
	public static final int LAYOUT_EXPAND =
		2048;
	
	public static final int LAYOUT_LEFT =
		1;
	
	public static final int LAYOUT_NEWLINE_AFTER =
		512;
	
	public static final int LAYOUT_NEWLINE_BEFORE =
		256;
	
	public static final int LAYOUT_RIGHT =
		2;
	
	public static final int LAYOUT_SHRINK =
		1024;
	
	public static final int LAYOUT_TOP =
		16;
	
	public static final int LAYOUT_VCENTER =
		48;
	
	public static final int LAYOUT_VEXPAND =
		8192;
	
	public static final int LAYOUT_VSHRINK =
		4096;
	
	public static final int PLAIN =
		0;
	
	/** Thread safe lock. */
	final Object _lock =
		new Object();
	
	/** The current layout of the item. */
	private volatile int _layout =
		LAYOUT_DEFAULT;
	
	/** The label of this item. */
	private volatile String _label;
	
	/** The owning screen. */
	volatile Screen _screen;
	
	/**
	 * Initializes the base item.
	 *
	 * @since 2017/08/19
	 */
	Item()
	{
	}
	
	public void addCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	public Command[] getCommands()
	{
		throw new todo.TODO();
	}
	
	public String getLabel()
	{
		throw new todo.TODO();
	}
	
	public int getLayout()
	{
		throw new todo.TODO();
	}
	
	public ItemLayoutHint getLayoutHint()
	{
		throw new todo.TODO();
	}
	
	public int getMinimumHeight()
	{
		throw new todo.TODO();
	}
	
	public int getMinimumWidth()
	{
		throw new todo.TODO();
	}
	
	public int getPreferredHeight()
	{
		throw new todo.TODO();
	}
	
	public int getPreferredWidth()
	{
		throw new todo.TODO();
	}
	
	public void notifyStateChanged()
	{
		throw new todo.TODO();
	}
	
	public void removeCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	public void setCommand(Command __c, int __p)
	{
		throw new todo.TODO();
	}
	
	public void setDefaultCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	public void setItemCommandListener(ItemCommandListener __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the label of the item.
	 *
	 * @param __l The label of the item to set, may be {@code null} to clear
	 * the label.
	 * @throws IllegalArgumentException If this item is contained within an
	 * alert.
	 * @since 2017/08/20
	 */
	public void setLabel(String __l)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB1t Cannot set the label of an item which is
		// contained within an Alert.}
		if (this._screen instanceof Alert)
			throw new IllegalArgumentException("EB1t");
		
		// Set
		this._label = __l;
		__update();
	}
	
	public void setLayout(int __a)
	{
		throw new todo.TODO();
	}
	
	public void setLayoutHint(ItemLayoutHint __h)
	{
		throw new todo.TODO();
	}
	
	public void setPreferredSize(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Updates the item on the containing form so that it knows of any
	 * changes which have occured.
	 *
	 * @since 2017/08/20
	 */
	void __update()
	{
		Screen f = this._screen;
		if (f != null)
			throw new todo.TODO();
	}
}


