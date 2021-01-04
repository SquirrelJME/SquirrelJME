// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.bluetooth;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public class DataElement
{
	public static final int BOOL = 40;
	
	public static final int DATALT = 56;
	
	public static final int DATSEQ = 48;
	
	public static final int INT_1 = 16;
	
	public static final int INT_16 = 20;
	
	public static final int INT_2 = 17;
	
	public static final int INT_4 = 18;
	
	public static final int INT_8 = 19;
	
	public static final int NULL = 0;
	
	public static final int STRING = 32;
	
	public static final int URL = 64;
	
	public static final int UUID = 24;
	
	public static final int U_INT_1 = 8;
	
	public static final int U_INT_16 = 12;
	
	public static final int U_INT_2 = 9;
	
	public static final int U_INT_4 = 10;
	
	public static final int U_INT_8 = 11;
	
	@SuppressWarnings("unused")
	public DataElement(int __i)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public DataElement(boolean __b)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public DataElement(int __i, long __l)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public DataElement(int __i, Object __o)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public synchronized void addElement(DataElement __dataElement)
	{
		throw Debugging.todo();
	}
	
	public boolean getBoolean()
	{
		throw Debugging.todo();
	}
	
	public int getDataType()
	{
		throw Debugging.todo();
	}
	
	public long getLong()
	{
		throw Debugging.todo();
	}
	
	public synchronized int getSize()
	{
		throw Debugging.todo();
	}
	
	public synchronized Object getValue()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public synchronized void insertElementAt(DataElement __dataElement,
		int __i)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public boolean removeElement(DataElement __dataElement)
	{
		throw Debugging.todo();
	}
}
