// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

@Api
@SuppressWarnings("RedundantThrows")
public class SimpleObjectInputStream
	extends DataInputStream
{
	@Api
	public SimpleObjectInputStream(InputStream var1)
	{
		super((InputStream)null);
		
		throw Debugging.todo();
	}
	
	@Api
	public String readString()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Api
	public SimpleSerializable read(SimpleSerializable var1)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Api
	public Object dispatchReadCommand(Class var1)
		throws IOException
	{
		throw Debugging.todo();
	}
}

