// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

public interface DataOutput
{
	void write(int __b)
		throws IOException;
	
	void write(byte[] __b)
		throws IOException, NullPointerException;
	
	void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException;
	
	void writeBoolean(boolean __v)
		throws IOException;
	
	void writeByte(int __v)
		throws IOException;
	
	void writeBytes(String __v)
		throws IOException, NullPointerException;
	
	void writeChar(int __v)
		throws IOException;
	
	void writeChars(String __v)
		throws IOException, NullPointerException;
	
	void writeDouble(double __v)
		throws IOException;
	
	void writeFloat(float __v)
		throws IOException;
	
	void writeInt(int __v)
		throws IOException;
	
	void writeLong(long __v)
		throws IOException;
	
	void writeShort(int __v)
		throws IOException;
	
	void writeUTF(String __v)
		throws IOException;
}

