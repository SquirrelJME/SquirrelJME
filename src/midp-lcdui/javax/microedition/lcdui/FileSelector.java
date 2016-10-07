// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.StreamConnection;

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
	
	public int getMode()
	{
		throw new Error("TODO");
	}
	
	public String getURL()
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
}

