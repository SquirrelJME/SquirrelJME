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
public class DataElement
{
	@Api
	public static final int BOOL = 40;
	
	@Api
	public static final int DATALT = 56;
	
	@Api
	public static final int DATSEQ = 48;
	
	@Api
	public static final int INT_1 = 16;
	
	@Api
	public static final int INT_16 = 20;
	
	@Api
	public static final int INT_2 = 17;
	
	@Api
	public static final int INT_4 = 18;
	
	@Api
	public static final int INT_8 = 19;
	
	@Api
	public static final int NULL = 0;
	
	@Api
	public static final int STRING = 32;
	
	@Api
	public static final int URL = 64;
	
	@Api
	public static final int UUID = 24;
	
	@Api
	public static final int U_INT_1 = 8;
	
	@Api
	public static final int U_INT_16 = 12;
	
	@Api
	public static final int U_INT_2 = 9;
	
	@Api
	public static final int U_INT_4 = 10;
	
	@Api
	public static final int U_INT_8 = 11;
	
	@Api
	public DataElement(int __i)
	{
		throw Debugging.todo();
	}
	
	@Api
	public DataElement(boolean __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public DataElement(int __i, long __l)
	{
		throw Debugging.todo();
	}
	
	@Api
	public DataElement(int __i, Object __o)
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized void addElement(DataElement __dataElement)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean getBoolean()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getDataType()
	{
		throw Debugging.todo();
	}
	
	@Api
	public long getLong()
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized int getSize()
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized Object getValue()
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized void insertElementAt(DataElement __dataElement,
		int __i)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean removeElement(DataElement __dataElement)
	{
		throw Debugging.todo();
	}
}
