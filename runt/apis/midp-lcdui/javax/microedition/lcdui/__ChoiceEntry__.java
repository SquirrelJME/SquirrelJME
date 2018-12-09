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
		
		// If the item is selected, draw a highlighted background and set the
		// text color
		boolean selected = this._selected;
		if (selected)
		{
			// Draw rectangle where the widget is for its background
			__g.setAlphaColor(CommonColors.HIGHLIGHTED_BACKGROUND);
			__g.fillRect(0, 0, __self.drawwidth, __self.drawheight);
			
			// Set text color
			__g.setAlphaColor(CommonColors.HIGHLIGHTED_FOREGROUND);
		}
		
		// Otherwise use a normal color
		else
			__g.setAlphaColor(CommonColors.FOREGROUND);
		
		// Draw the list text
		__g.drawString(this._string, dx, 0, Graphics.TOP | Graphics.LEFT);
	}
}

