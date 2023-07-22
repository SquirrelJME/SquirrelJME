// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.nio.Buffer;

@Api
public class RoundCompletionEvent<P extends Device<? super P>, B extends 
	Buffer>
	extends DeviceEvent<P>
{
	@Api
	public RoundCompletionEvent(P __a, B __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public RoundCompletionEvent(P __a, B __b, int __c, long __d, int __e)
	{
		throw Debugging.todo();
	}
	
	@Api
	public RoundCompletionEvent(P __a, B __b, int __c, boolean __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	public RoundCompletionEvent(P __a, B __b, int __c, boolean __d, long __e,
		int __f)
	{
		throw Debugging.todo();
	}
	
	@Api
	public B getBuffer()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getNumber()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isOnError()
	{
		throw Debugging.todo();
	}
}


