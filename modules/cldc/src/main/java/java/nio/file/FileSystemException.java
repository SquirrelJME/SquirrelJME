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
public class FileSystemException
	extends IOException
{
	@Api
	public FileSystemException(String __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public FileSystemException(String __a, String __b, String __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getFile()
	{
		throw Debugging.todo();
	}
	
	@Override
	public String getMessage()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getOtherFile()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getReason()
	{
		throw Debugging.todo();
	}
}

