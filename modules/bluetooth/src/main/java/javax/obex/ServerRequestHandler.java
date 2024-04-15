// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.obex;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class ServerRequestHandler
{
	@Api
	protected ServerRequestHandler()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final HeaderSet createHeaderSet()
	{
		throw Debugging.todo();
	}
	
	@Api
	public long getConnectionID()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void onAuthenticationFailure(byte[] __bytes)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int onConnect(HeaderSet __headerSet, HeaderSet __headerSet1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int onDelete(HeaderSet __headerSet, HeaderSet __headerSet1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void onDisconnect(HeaderSet __headerSet, HeaderSet __headerSet1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int onGet(Operation __operation)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int onPut(Operation __operation)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int onSetPath(HeaderSet __headerSet, HeaderSet __headerSet1,
		boolean __b, boolean __b1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setConnectionID(long __l)
	{
		throw Debugging.todo();
	}
}
