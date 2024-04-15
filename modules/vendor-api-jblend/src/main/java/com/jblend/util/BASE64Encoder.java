// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Api
public class BASE64Encoder
{
	@Api
	public BASE64Encoder()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static synchronized String encode(String var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	protected static int bytesPerAtom()
	{
		throw Debugging.todo();
	}
	
	@Api
	protected static int bytesPerLine()
	{
		throw Debugging.todo();
	}
	
	@Api
	protected static void encodeAtom(OutputStream var0, byte[] var1, int var2,
		int var3)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Api
	protected static int readFully(InputStream var0, byte[] var1)
		throws IOException
	{
		throw Debugging.todo();
	}
}

