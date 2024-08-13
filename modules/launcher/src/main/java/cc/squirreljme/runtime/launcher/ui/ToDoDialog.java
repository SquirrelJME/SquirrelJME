// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * Dialog which is shown when a To-Do occurs.
 *
 * @since 2024/08/13
 */
public class ToDoDialog
	extends Canvas
{
	/** The class this is in. */
	protected final String inClass;
	
	/** The method this is. */
	protected final String inMethod;
	
	/** The method's type. */
	protected final String inMethodType;
	
	/**
	 * Initializes the dialog.
	 *
	 * @param __inClass The class this is in.
	 * @param __inMethod The method this is in.
	 * @param __inMethodType The method type.
	 * @since 2024/08/13
	 */
	public ToDoDialog(String __inClass, String __inMethod,
		String __inMethodType)
	{
		this.inClass = __inClass;
		this.inMethod = __inMethod;
		this.inMethodType = __inMethodType;
		
		this.setTitle(String.format("%s.%s %s",
			__inClass, __inMethod, __inMethodType));
		this.repaint();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/13
	 */
	@Override
	protected void paint(Graphics __g)
	{
		Font font = __g.getFont();
		int ln = font.getHeight();
		
		int y = 1 + ln;
		
		__g.drawString("TODO HIT! AT:", 1, y, 0);
		y += ln;
		
		__g.drawString(String.format("Class %s", this.inClass),
			1, y, 0);
		y += ln;
		
		__g.drawString(String.format("Methd %s", this.inMethod),
			1, y, 0);
		y += ln;
		
		__g.drawString(String.format("MType %s", this.inMethodType),
			1, y, 0);
		y += ln;
	}
}
