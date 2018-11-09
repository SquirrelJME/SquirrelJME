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

/**
 * This class acts as the lowest base for displays and items.
 *
 * @since 2018/03/23
 */
abstract class __Widget__
{
	/** Is this being shown right now? */
	boolean _isshown;
	
	/**
	 * Initializes the widget using a handle which is registered on the
	 * remote end.
	 *
	 * @param __h The handle to use.
	 * @since 2018/03/23
	 */
	__Widget__()
	{
		super();
	}
	
	/**
	 * Virtual call which has no effect.
	 *
	 * @since 2018/03/28
	 */
	@SerializedEvent
	void hideNotify()
	{
	}
	
	/**
	 * This is a virtual paint call which has no effect.
	 *
	 * @param __g The graphics to draw into.
	 * @since 2018/03/28
	 */
	@SerializedEvent
	void paint(Graphics __g)
	{
	}
	
	/**
	 * This is a virtual paint call which has no effect.
	 *
	 * @param __g The graphics to draw into.
	 * @param __w The width of the item.
	 * @param __h The height of the item.
	 * @since 2018/03/28
	 */
	@SerializedEvent
	void paint(Graphics __g, int __w, int __h)
	{
		// This will forward for the normal canvas call
		this.paint(__g);
	}
	
	/**
	 * Virtual call which has no effect.
	 *
	 * @since 2018/03/28
	 */
	@SerializedEvent
	void showNotify()
	{
	}
	
	/**
	 * Virtual call which has no effect.
	 *
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/03/27
	 */
	@SerializedEvent
	void sizeChanged(int __w, int __h)
	{
	}
	
	/**
	 * This is called when a repaint is to be performed.
	 *
	 * @param __g The graphics to draw into.
	 * @param __bw The buffer width.
	 * @param __bh The buffer height.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	@SerializedEvent
	final void __doPaint(Graphics __g, int __bw, int __bh)
		throws NullPointerException
	{
		// Determine if a transparent color should be drawn over the widget
		int trans = this.__getTransparentColor();
		if (trans != 0)
		{
			// The graphics object gets the color pre-initialized so make sure
			// to restore it after the paint
			int old = __g.getAlphaColor();
			
			// Fill the area accordingly
			__g.setAlphaColor(trans | 0xFF000000);
			__g.fillRect(__g.getClipX(), __g.getClipY(),
				__g.getClipWidth(), __g.getClipHeight());
			
			// Restore old color
			__g.setAlphaColor(old);
		}
		
		// Perform the paint
		this.paint(__g, __bw, __bh);
	}
	
	/**
	 * This is called when a widget's visibility has changed.
	 *
	 * @param __shown Is the widget shown?
	 * @since 2018/03/24
	 */
	@SerializedEvent
	final void __doShown(boolean __shown)
	{
		boolean isshown = this._isshown;
		
		// Make sure the methods are called only once since there could be
		// multiple events happening
		if (isshown != __shown)
		{
			this._isshown = __shown;
			
			// Create display notification?
			if (this instanceof Display)
				((Display)this).__doDisplayShown(__shown);
			
			// Normal event
			else
			{
				if (__shown)
					this.showNotify();
				else
					this.hideNotify();
			}
		}
	}
	
	/**
	 * This is called when the size of the widget has changed.
	 *
	 * @param __w The new width.
	 * @param __h The new height.
	 * @since 2018/03/23
	 */
	@SerializedEvent
	final void __doSizeChanged(int __w, int __h)
	{
		// Do display notification?
		if (this instanceof Display)
			((Display)this).__doDisplaySizeChanged(__w, __h);
		
		// Normal
		else
			this.sizeChanged(__w, __h);
	}
	
	/**
	 * Returns the height of the displayable or the maximum size of the
	 * default display.
	 *
	 * @return The displayable height or the maximum height of the default
	 * display.
	 * @since 2017/05/24
	 */
	final int __getHeight()
	{
		throw new todo.TODO();
		/*
		int rv = LcdServiceCall.<Integer>call(Integer.class,
			LcdFunction.WIDGET_GET_HEIGHT, this._handle);
		if (rv < 0)
			return Display.getDisplays(0)[0].getHeight();
		return rv;
		*/
	}
	
	/**
	 * Returns the transparent color to use for the widget.
	 *
	 * @return The transparent color or {@code 0} if it is not valid, the
	 * alpha channel must be included.
	 * @since 2018/03/28
	 */
	int __getTransparentColor()
	{
		return 0;
	}
	
	/**
	 * Returns the width of the displayable or the maximum size of the
	 * default display.
	 *
	 * @return The displayable width or the maximum width of the default
	 * display.
	 * @since 2017/05/24
	 */
	final int __getWidth()
	{
		throw new todo.TODO();
		/*
		int rv = LcdServiceCall.<Integer>call(Integer.class,
			LcdFunction.WIDGET_GET_WIDTH, this._handle);
		if (rv < 0)
			return Display.getDisplays(0)[0].getWidth();
		return rv;
		*/
	}
}

