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

import cc.squirreljme.runtime.lcdui.common.CommonColors;
import cc.squirreljme.runtime.lcdui.ui.UIDrawable;
import cc.squirreljme.runtime.lcdui.ui.UIStack;

/**
 * This is a package public mutable class which represents single choices
 * within anything which uses choices.
 *
 * @since 2017/08/20
 */
final class __ChoiceEntry__
	extends __Drawable__
{
	/** The string to display for this choice. */
	volatile String _string;
	
	/** The image to display for this choice. */
	volatile Image _image;
	
	/** The font to use for this choice. */
	volatile Font _font;
	
	/** Is this entry selected? */
	volatile boolean _selected;
	
	/** Is this item disabled (is enabled by default)? */
	volatile boolean _disabled;
	
	/**
	 * Initializes a choice entry with default values.
	 *
	 * @param __s The string to display.
	 * @param __i The image to display.
	 * @since 2017/08/20
	 */
	__ChoiceEntry__(String __s, Image __i)
	{
		// Set
		this._string = __s;
		this._image = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/09
	 */
	@Override
	void __draw(UIStack __parent, UIStack __self, Graphics __g)
	{
		// Draw X position
		int dx = 0;
		
		// Handle drawing of image
		Image image = this._image;
		if (image != null)
		{
			throw new todo.TODO();
		}
		
		// Font used for drawing
		Font f = this._font;
		if (f == null)
			f = Font.getDefaultFont();
		
		// Color changes according to selected and enabled state
		boolean selected = this._selected,
			disabled = this._disabled;
		int fg, bg;
		if (selected)
			if (disabled)
			{
				bg = CommonColors.DISABLED_HIGHLIGHTED_BACKGROUND;
				fg = CommonColors.DISABLED_HIGHLIGHTED_FOREGROUND;
			}
			else
			{
				bg = CommonColors.HIGHLIGHTED_BACKGROUND;
				fg = CommonColors.HIGHLIGHTED_FOREGROUND;
			}
		else
			if (disabled)
			{
				bg = CommonColors.DISABLED_BACKGROUND;
				fg = CommonColors.DISABLED_FOREGROUND;
			}
			else
			{
				bg = CommonColors.BACKGROUND;
				fg = CommonColors.FOREGROUND;
			}
		
		// Draw rectangle where the widget is for its background, but the list
		// will already be the neutral background
		if (selected || disabled)
		{
			__g.setAlphaColor(bg);
			__g.fillRect(0, 0, __self.drawwidth, __self.drawheight);
		}
			
		// Draw foreground text
		__g.setAlphaColor(fg);
		__g.setFont(f);
		__g.drawString(this._string, dx, 0, Graphics.TOP | Graphics.LEFT);
		
		// Strike out disabled items
		if (disabled)
		{
			int hh = __self.drawheight >> 1;
			__g.drawLine(dx, hh, f.stringWidth(this._string), hh);
		}
	}
}

