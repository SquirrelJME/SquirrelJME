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
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

public class TabbedPane
	extends Screen
{
	public TabbedPane(String __title, boolean __stringtab, boolean __suptitle)
	{
		throw Debugging.todo();
	}
	
	public void addTab(Screen __t, Image __i)
	{
		throw Debugging.todo();
	}
	
	public void addTabListener(TabListener __tl)
	{
		throw Debugging.todo();
	}
	
	public int getCount()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getHeight()
	{
		throw Debugging.todo();
	}
	
	public Screen getScreen(int __i)
	{
		throw Debugging.todo();
	}
	
	public int getSelectedIndex()
	{
		throw Debugging.todo();
	}
	
	public Image getTabIcon(int __i)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getWidth()
	{
		throw Debugging.todo();
	}
	
	public void insertTab(int __i, Screen __t, Image __img)
	{
		throw Debugging.todo();
	}
	
	public void removeTab(int __i)
	{
		throw Debugging.todo();
	}
	
	public void setFocus(int __i)
	{
		throw Debugging.todo();
	}
	
	public void setTabIcon(int __i, Image __icon)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/14
	 */
	@Override
	__CommonState__ __stateInit(UIBackend __backend)
		throws NullPointerException
	{
		return new __TabbedPaneState__(__backend, this);
	}
	
	/**
	 * File selector state.
	 * 
	 * @since 2023/01/14
	 */
	static class __TabbedPaneState__
		extends Screen.__ScreenState__
	{
		/**
		 * Initializes the backend state.
		 *
		 * @param __backend The backend used.
		 * @param __self Self widget.
		 * @since 2023/01/14
		 */
		__TabbedPaneState__(UIBackend __backend, DisplayWidget __self)
		{
			super(__backend, __self);
		}
	}
}

