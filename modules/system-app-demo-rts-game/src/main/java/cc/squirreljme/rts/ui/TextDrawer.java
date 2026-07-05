// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.ui;

import cc.squirreljme.runtime.cldc.util.CharArrayAppendable;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import java.util.Formatter;
import javax.microedition.lcdui.Graphics;

/**
 * Text drawing handling and storage.
 *
 * @since 2026/07/05
 */
public class TextDrawer
{
	/** The string buffer. */
	private final CharArrayAppendable _buffer =
		new CharArrayAppendable(128);
	
	/** The formatter for the buffer. */
	private final Formatter _format =
		new Formatter(this._buffer);
	
	/**
	 * Draws the given text.
	 *
	 * @param __g The graphics to draw with.
	 * @param __x The X coordinates of the text.
	 * @param __y The Y coordinates of the text.
	 * @param __fmt The text format.
	 * @param __args The arguments to the format.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/07/05
	 */
	public void draw(PencilGraphics __g, int __x, int __y,
		String __fmt, Object... __args)
	{
		if (__g == null || __fmt == null)
			throw new NullPointerException("NARG");
		
		// Clear the buffer first
		CharArrayAppendable buffer = this._buffer;
		buffer.reset();
		
		// Format the text and arguments
		this._format.format(__fmt, __args);
		
		// Draw what was used
		__g.drawChars(buffer.charArray(), 0, buffer.position(),
			__x, __y, Graphics.TOP | PencilGraphics.LEFT);
	}
}
