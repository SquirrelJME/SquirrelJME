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
import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.StreamConnection;

public class FileSelector
	extends Screen
{
	/**
	 * This is used when the user has dismissed the file selector without
	 * having any file selected.
	 */
	public static final Command CANCEL_COMMAND =
		new Command("Cancel", Command.CANCEL, 1, true);
	
	/** This is used when the user has selected a file. */
	public static final Command OK_COMMAND =
		new Command("Select", Command.OK, 0, true);
	
	public static final int DIRECTORY_CREATE =
		3;
	
	public static final int DIRECTORY_SELECT =
		2;
	
	public static final int LOAD =
		0;
	
	public static final int SAVE =
		1;
	
	public FileSelector(String __title, int __mode)
	{
		throw Debugging.todo();
	}
	
	public String[] getFilterExtensions()
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
		/*
		return this.__defaultHeight();
		*/
	}
	
	public int getMode()
	{
		throw Debugging.todo();
	}
	
	public String getURL()
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
		/*
		return this.__defaultWidth();
		*/
	}
	
	public StreamConnection open(int __mode, boolean __to)
		throws ConnectionNotFoundException, IOException
	{
		throw Debugging.todo();
	}
	
	public void setFilterExtensions(String[] __ext)
	{
		throw Debugging.todo();
	}
	
	public void setMode(int __m)
	{
		throw Debugging.todo();
	}
	
	public void setURL(String __u)
		throws IOException
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
		return new __FileSelectorState__(__backend, this);
	}
	
	/**
	 * File selector state.
	 * 
	 * @since 2023/01/14
	 */
	static class __FileSelectorState__
		extends Screen.__ScreenState__
	{
		/**
		 * Initializes the backend state.
		 *
		 * @param __backend The backend used.
		 * @param __self Self widget.
		 * @since 2023/01/14
		 */
		__FileSelectorState__(UIBackend __backend, DisplayWidget __self)
		{
			super(__backend, __self);
		}
	}
}

