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

import cc.squirreljme.runtime.lcdui.SerializedEvent;

public abstract class Item
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
	
	/** The owning displayable. */
	volatile Displayable _displayable;
	
	/** The current layout of the item. */
	volatile int _layout =
		LAYOUT_DEFAULT;
	
	/** The label of this item. */
	volatile String _label;
	
	/** The preferred width. */
	volatile int _preferredw =
		-1;
	
	/** The preferred height. */
	volatile int _preferredh =
		-1;
	
	/**
	 * Initializes the base item.
	 *
	 * @since 2017/08/19
	 */
	Item()
	{
	}
	
	/**
	 * Initializes the base item with some parameters.
	 *
	 * @param __l The label to use.
	 * @since 2019/05/17
	 */
	Item(String __l)
	{
		this._label = __l;
	}
	
	public void addCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	public Command[] getCommands()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the label of the item.
	 *
	 * @return The label item.
	 * @since 2019/12/09
	 */
	public String getLabel()
	{
		return this._label;
	}
	
	public int getLayout()
	{
		throw new todo.TODO();
	}
	
	public ItemLayoutHint getLayoutHint()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the minimum height of the item.
	 *
	 * @return The minimum height.
	 * @since 2019/12/09
	 */
	public int getMinimumHeight()
	{
		// {@squirreljme.error EB37 Minimum height must be implemented.
		// (The class name)}
		throw new RuntimeException("EB37 " + this.getClass().getName());
	}
	
	/**
	 * Returns the minimum width of the item.
	 *
	 * @return The minimum width.
	 * @since 2019/12/09
	 */
	public int getMinimumWidth()
	{
		// {@squirreljme.error EB38 Minimum width must be implemented.
		// (The class name)}
		throw new RuntimeException("EB38 " + this.getClass().getName());
	}
	
	/**
	 * Returns the preferred height of the item, this may be calculated.
	 *
	 * @return The preferred item height.
	 * @since 2019/12/09
	 */
	public int getPreferredHeight()
	{
		int rv = this._preferredh,
			mn = this.getMinimumHeight();
		return (mn > rv ? mn : rv);
	}
	
	/**
	 * Returns the preferred width of the item, this may be calculated.
	 *
	 * @return The preferred item width.
	 * @since 2019/12/09
	 */
	public int getPreferredWidth()
	{
		int rv = this._preferredw,
			mn = this.getMinimumWidth();
		return (mn > rv ? mn : rv);
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
	 * @since 2017/08/20
	 */
	public void setLabel(String __l)
	{
		this._label = __l;
		
		// Repaint the display
		Displayable displayable = this._displayable;
		if (displayable != null)
		{
			Display display = displayable._display;
			if (display != null)
				display._phoneui.repaint();
		}
	}
	
	/**
	 * Sets the layout.
	 *
	 * @param __lay The layout.
	 * @since 2019/05/17
	 */
	public void setLayout(int __lay)
	{
		this._layout = __lay;
	}
	
	public void setLayoutHint(ItemLayoutHint __h)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the preferred size of the item. This sets the size of an item where
	 * if it is higher than the minimum then it is used, otherwise the minimum
	 * is used.
	 *
	 * @param __w The width, or {@code -1} to use the minimum.
	 * @param __h The height, or {@code -1} to use the minimum.
	 * @throws IllegalArgumentException If the width and/or height are lower
	 * than {@code -1}.
	 * @throws IllegalStateException If this item is within an alert.
	 * @since 2019/12/09
	 */
	public void setPreferredSize(int __w, int __h)
		throws IllegalArgumentException, IllegalStateException
	{
		// {@squirreljme.error EB35 Invalid preferred size requested.}
		if (__w < -1 || __h < -1)
			throw new IllegalArgumentException("EB35");
		
		// {@squirreljme.error EB36 Cannot set preferred size of item within
		// an alert.}
		if (this._displayable instanceof Alert)
			throw new IllegalStateException("EB36");
		
		this._preferredw = __w;
		this._preferredh = __h;
	}
}


