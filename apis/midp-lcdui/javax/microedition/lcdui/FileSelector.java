// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.StreamConnection;
import net.multiphasicapps.squirreljme.lcdui.DisplayConnector;

public class FileSelector
	extends Screen
{
	public static final Command CANCEL_COMMAND =
		new Command(null, -1, -1);
	
	public static final Command OK_COMMAND =
		new Command(null, -1, -1);
	
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
		throw new Error("TODO");
	}
	
	public String[] getFilterExtensions()
	{
		throw new Error("TODO");
	}
	
	@Override
	public int getHeight()
	{
		throw new Error("TODO");
	}
	
	public int getMode()
	{
		throw new Error("TODO");
	}
	
	public String getURL()
	{
		throw new Error("TODO");
	}
	
	@Override
	public int getWidth()
	{
		throw new Error("TODO");
	}
	
	public StreamConnection open(int __mode, boolean __to)
		throws ConnectionNotFoundException, IOException
	{
		throw new Error("TODO");
	}
	
	public void setFilterExtensions(String[] __ext)
	{
		throw new Error("TODO");
	}
	
	public void setMode(int __m)
	{
		throw new Error("TODO");
	}
	
	public void setURL(String __u)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	DisplayConnector __connector()
	{
		throw new Error("TODO");
	}
}

