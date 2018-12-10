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
import cc.squirreljme.runtime.lcdui.ui.UIPersist;
import cc.squirreljme.runtime.lcdui.ui.UIStack;

public class StringItem
	extends Item
{
	public StringItem(String __a, String __b)
	{
		super();
		throw new todo.TODO();
	}
	
	public StringItem(String __a, String __b, int __c)
	{
		super();
		throw new todo.TODO();
	}
	
	public int getAppearanceMode()
	{
		throw new todo.TODO();
	}
	
	public Font getFont()
	{
		throw new todo.TODO();
	}
	
	public String getText()
	{
		throw new todo.TODO();
	}
	
	public void setFont(Font __a)
	{
		throw new todo.TODO();
	}
	
	public void setText(String __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __draw(UIPersist __persist, UIStack __parent, UIStack __self,
		Graphics __g)
	{
		__g.drawString(this.getClass().getName().toString(),
			__g.getClipX(), __g.getClipY(), 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __updateUIStack(UIPersist __keep, UIStack __parent)
	{
		throw new todo.TODO();
	}
}


