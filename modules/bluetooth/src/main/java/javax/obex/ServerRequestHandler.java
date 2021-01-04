// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.obex;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public class ServerRequestHandler
{
	protected ServerRequestHandler()
	{
		throw Debugging.todo();
	}
	
	public final HeaderSet createHeaderSet()
	{
		throw Debugging.todo();
	}
	
	public long getConnectionID()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public void onAuthenticationFailure(byte[] __bytes)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public int onConnect(HeaderSet __headerSet, HeaderSet __headerSet1)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public int onDelete(HeaderSet __headerSet, HeaderSet __headerSet1)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public void onDisconnect(HeaderSet __headerSet, HeaderSet __headerSet1)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public int onGet(Operation __operation)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public int onPut(Operation __operation)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public int onSetPath(HeaderSet __headerSet, HeaderSet __headerSet1,
		boolean __b, boolean __b1)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public void setConnectionID(long __l)
	{
		throw Debugging.todo();
	}
}
