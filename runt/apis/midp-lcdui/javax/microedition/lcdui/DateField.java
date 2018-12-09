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
import java.util.Date;
import java.util.TimeZone;
import cc.squirreljme.runtime.lcdui.ui.UIStack;

public class DateField
	extends Item
{
	public static final int DATE =
		1;
	
	public static final int DATE_TIME =
		3;
	
	public static final int TIME =
		2;
	
	public DateField(String __a, int __b)
	{
		super();
		throw new todo.TODO();
	}
	
	public DateField(String __a, int __b, TimeZone __c)
	{
		super();
		throw new todo.TODO();
	}
	
	public Date getDate()
	{
		throw new todo.TODO();
	}
	
	public int getInputMode()
	{
		throw new todo.TODO();
	}
	
	public void setDate(Date __a)
	{
		throw new todo.TODO();
	}
	
	public void setInputMode(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __draw(UIStack __parent, UIStack __self, Graphics __g)
	{
		__g.drawString(this.getClass().getName().toString(),
			__g.getClipX(), __g.getClipY(), 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __updateUIStack(UIStack __parent)
	{
		throw new todo.TODO();
	}
}


