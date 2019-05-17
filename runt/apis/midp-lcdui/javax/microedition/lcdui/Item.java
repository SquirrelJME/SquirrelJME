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
	
	public void setPreferredSize(int __a, int __b)
	{
		throw new todo.TODO();
	}
}


