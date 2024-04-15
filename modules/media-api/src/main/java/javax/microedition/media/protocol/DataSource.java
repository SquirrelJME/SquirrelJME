// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.protocol;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import javax.microedition.media.Controllable;

@Api
public abstract class DataSource
	implements Controllable
{
	@Api
	public DataSource(String __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public abstract void connect()
		throws IOException;
	
	@Api
	public abstract void disconnect();
	
	@Api
	public abstract String getContentType();
	
	@Api
	public abstract SourceStream[] getStreams();
	
	@Api
	public abstract void start()
		throws IOException;
	
	@Api
	public abstract void stop()
		throws IOException;
	
	@Api
	public String getLocator()
	{
		throw Debugging.todo();
	}
}


