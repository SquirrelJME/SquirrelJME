// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.file;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

@Api
public abstract class FileStore
{
	@Api
	protected FileStore()
	{
		throw Debugging.todo();
	}
	
	@Api
	public abstract Object getAttribute(String __a)
		throws IOException;
	
	@Api
	public abstract long getTotalSpace()
		throws IOException;
	
	@Api
	public abstract long getUnallocatedSpace()
		throws IOException;
	
	@Api
	public abstract long getUsableSpace()
		throws IOException;
	
	@Api
	public abstract boolean isReadOnly();
	
	@Api
	public abstract String name();
	
	@Api
	public abstract boolean supportsFileAttributeView(String __a);
	
	@Api
	public abstract String type();
}


