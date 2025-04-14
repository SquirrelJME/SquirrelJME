// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.bluetooth;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class UUID
{
	private final String _uuid;
	
	@Api
	@SuppressWarnings("unused")
	public UUID(long __l)
	{
		throw Debugging.todo();
	}
	
	@Api
	@SuppressWarnings("unused")
	public UUID(String __uuid, boolean __short)
	{
		this._uuid = __uuid;
	}
	
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
}
