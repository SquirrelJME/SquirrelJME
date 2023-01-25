// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SimpleObjectOutputStream
	extends DataOutputStream
{
	public SimpleObjectOutputStream(OutputStream var1)
	{
		super((OutputStream)null);
		
		throw Debugging.todo();
	}
	
	@Api
	public void write(String var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void write(SimpleSerializable var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void dispatchWriteCommand(Object var1)
	{
		throw Debugging.todo();
	}
}
