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
import cc.squirreljme.runtime.lcdui.common.CommonMetrics;

/**
 * Drawable for command buttons.
 *
 * @since 2018/12/02
 */
class __CommandDrawable__
	extends __Drawable__
{
	/** The command to draw. */
	protected final Command command;
	
	/** Text object representing the command to draw. */
	protected final Text text =
		new Text();
	
	/**
	 * Initializes the command.
	 *
	 * @param __c The command to draw.
	 * @throws NullPointerException On null arguments;
	 * @since 2018/12/02
	 */
	__CommandDrawable__(Command __c)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		this.command = __c;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	final void __drawChain(Graphics __g)
	{
		// Get chain properties
		__DrawChain__ chain = this._drawchain;
		int x = chain.x,
			y = chain.y,
			w = chain.w,
			h = chain.h;
		
		// Draw background color
		int color = __g.getAlphaColor();
		__g.setColor(CommonColors.COMMANDBAR_BACKGROUND);
		__g.fillRect(x, y, w, h);
		__g.setAlphaColor(color);
		
		// Update text with our command's text
		Command command = this.command;
		Text text = this.text;
		text.delete(0, text.getTextLength());
		text.insert(0, command._shortlabel);
		
		// Draw this text
		__g.drawText(text, x, y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/02
	 */
	@Override
	final void __updateDrawChain(__DrawSlice__ __sl)
	{
		// Need width and height
		int w = __sl.w,
			h = __sl.h;
		
		// Copy properties to the chain
		__DrawChain__ chain = this._drawchain;
		
		// Setup properties for the text
		Text text = this.text;
		text.setWidth(w);
		text.setHeight(h);
		text.setForegroundColor(CommonColors.COMMANDBAR_FOREGROUND);
	}
}

