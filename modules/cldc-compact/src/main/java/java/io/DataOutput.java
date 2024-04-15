// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface DataOutput
{
	@Api
	void write(int __b)
		throws IOException;
	
	@Api
	void write(byte[] __b)
		throws IOException, NullPointerException;
	
	@Api
	void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException;
	
	@Api
	void writeBoolean(boolean __v)
		throws IOException;
	
	@Api
	void writeByte(int __v)
		throws IOException;
	
	@Api
	void writeBytes(String __v)
		throws IOException, NullPointerException;
	
	@Api
	void writeChar(int __v)
		throws IOException;
	
	@Api
	void writeChars(String __v)
		throws IOException, NullPointerException;
	
	@Api
	void writeDouble(double __v)
		throws IOException;
	
	@Api
	void writeFloat(float __v)
		throws IOException;
	
	@Api
	void writeInt(int __v)
		throws IOException;
	
	@Api
	void writeLong(long __v)
		throws IOException;
	
	@Api
	void writeShort(int __v)
		throws IOException;
	
	@Api
	void writeUTF(String __v)
		throws IOException;
}

