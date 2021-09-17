// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public class StringItem
	extends Item
{
	/** Apperance mode. */
	final int _amode;
	
	/** The item text. */
	volatile String _text;
	
	/** The font to use. */
	volatile Font _font;
	
	/**
	 * Initializes the string item with the plain style.
	 *
	 * @param __l The label.
	 * @param __t The text.
	 * @since 2019/05/17
	 */
	public StringItem(String __l, String __t)
	{
		this(__l, __t, Item.PLAIN);
	}
	
	/**
	 * Initializes the string item with the plain style.
	 *
	 * @param __l The label.
	 * @param __t The text.
	 * @param __am The appearance mode.
	 * @throws IllegalArgumentException If the appearance mode is not valid.
	 * @since 2019/05/17
	 */
	public StringItem(String __l, String __t, int __am)
		throws IllegalArgumentException
	{
		super(__l);
		
		// {@squirreljme.error EB2o The appearance mode is not valid.
		// (The appearance mode)}
		if (__am != Item.PLAIN && __am != Item.BUTTON && __am != Item.HYPERLINK)
			throw new IllegalArgumentException("EB2o " + __am);
		
		this._text = __t;
		this._amode = __am;
	}
	
	public int getAppearanceMode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the font used to draw the item.
	 *
	 * @return The font used to draw the item.
	 * @since 2019/12/09
	 */
	public Font getFont()
	{
		Font rv = this._font;
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/09
	 */
	@Override
	public int getMinimumHeight()
	{
		// Empty string needs no space
		String text = this.getText();
		if (text == null)
			return 0;
		
		return this.getFont().getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/09
	 */
	@Override
	public int getMinimumWidth()
	{
		// Empty string needs no space
		String text = this.getText();
		if (text == null)
			return 0;
		
		return this.getFont().stringWidth(text);
	}
	
	/**
	 * Returns the text content of this item.
	 *
	 * @return The text content.
	 * @since 2019/12/09
	 */
	public String getText()
	{
		return this._text;
	}
	
	public void setFont(Font __a)
	{
		throw new todo.TODO();
	}
	
	public void setText(String __a)
	{
		throw new todo.TODO();
	}
}


