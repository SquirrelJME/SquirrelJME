// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

public interface DataOutput
{
	public abstract void write(int __b)
		throws IOException;
	
	public abstract void write(byte[] __b)
		throws IOException, NullPointerException;
	
	public abstract void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException;
	
	public abstract void writeBoolean(boolean __v)
		throws IOException;
	
	public abstract void writeByte(int __v)
		throws IOException;
	
	public abstract void writeBytes(String __v)
		throws IOException, NullPointerException;
	
	public abstract void writeChar(int __v)
		throws IOException;
	
	public abstract void writeChars(String __v)
		throws IOException, NullPointerException;
	
	public abstract void writeDouble(double __v)
		throws IOException;
	
	public abstract void writeFloat(float __v)
		throws IOException;
	
	public abstract void writeInt(int __v)
		throws IOException;
	
	public abstract void writeLong(long __v)
		throws IOException;
	
	public abstract void writeShort(int __v)
		throws IOException;
	
	public abstract void writeUTF(String __v)
		throws IOException;
}

