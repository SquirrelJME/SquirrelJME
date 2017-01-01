// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.protocol;

import java.io.IOException;
import javax.microedition.media.Control;
import javax.microedition.media.Controllable;

public abstract class DataSource
	implements Controllable
{
	public DataSource(String __a)
	{
		super();
		throw new Error("TODO");
	}
	
	public abstract void connect()
		throws IOException;
	
	public abstract void disconnect();
	
	public abstract String getContentType();
	
	public abstract Control getControl(String __a);
	
	public abstract Control[] getControls();
	
	public abstract SourceStream[] getStreams();
	
	public abstract void start()
		throws IOException;
	
	public abstract void stop()
		throws IOException;
	
	public String getLocator()
	{
		throw new Error("TODO");
	}
}


