// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.bluetooth;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public class UUID
{
	private final String _uuid;
	
	@SuppressWarnings("unused")
	public UUID(long __l)
	{
		throw Debugging.todo();
	}
	
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
