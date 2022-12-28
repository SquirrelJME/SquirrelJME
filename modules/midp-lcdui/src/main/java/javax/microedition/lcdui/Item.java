// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;

public abstract class Item
	extends __CommonWidget__
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
	
	/** The label item. */
	final UIItemBracket _labelItem;
	
	/** The owning displayable. */
	volatile Displayable _displayable;
	
	/** The current layout of the item. */
	volatile int _layout =
		Item.LAYOUT_DEFAULT;
	
	/** The label of this item. */
	volatile String _label;
	
	/** The preferred width. */
	volatile int _preferredW =
		-1;
	
	/** The preferred height. */
	volatile int _preferredH =
		-1;
	
	/**
	 * Initializes the base item.
	 *
	 * @since 2017/08/19
	 */
	Item()
	{
		this(null);
	}
	
	/**
	 * Initializes the base item with some parameters.
	 *
	 * @param __l The label to use.
	 * @since 2019/05/17
	 */
	Item(String __l)
	{
		// Setup label item
		UIBackend backend = UIBackendFactory.getInstance();
		this._labelItem = backend.itemNew(UIItemType.LABEL);
		
		// Set the label accordingly
		this.__setLabel(__l);
	}
	
	public void addCommand(Command __a)
	{
		throw Debugging.todo();
	}
	
	public Command[] getCommands()
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
	}
	
	public ItemLayoutHint getLayoutHint()
	{
		throw Debugging.todo();
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
		int rv = this._preferredH,
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
		int rv = this._preferredW,
			mn = this.getMinimumWidth();
		return (mn > rv ? mn : rv);
	}
	
	public void notifyStateChanged()
	{
		throw Debugging.todo();
	}
	
	public void removeCommand(Command __a)
	{
		throw Debugging.todo();
	}
	
	public void setCommand(Command __c, int __p)
	{
		throw Debugging.todo();
	}
	
	public void setDefaultCommand(Command __a)
	{
		throw Debugging.todo();
	}
	
	public void setItemCommandListener(ItemCommandListener __a)
	{
		throw Debugging.todo();
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
		this.__setLabel(__l);
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
		throw Debugging.todo();
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
		
		this._preferredW = __w;
		this._preferredH = __h;
	}
	
	/**
	 * Sets the label for this item.
	 * 
	 * @param __l The label to use, {@code null} will clear it.
	 * @since 2021/11/27
	 */
	final void __setLabel(String __l)
	{
		// Record the string to be used
		this._label = __l;
		
		// Update the label
		UIBackend backend = UIBackendFactory.getInstance();
		backend.widgetProperty(this._labelItem,
			UIWidgetProperty.STRING_LABEL, 0, __l);
		
		// Perform an update on the form
		Displayable displayable = this._displayable;
		if (displayable instanceof Form)
			((Form)displayable).__update();
	}
}


