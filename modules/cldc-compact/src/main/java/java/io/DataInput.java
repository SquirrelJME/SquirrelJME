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
public interface DataInput
{
	@Api
	boolean readBoolean()
		throws IOException;
	
	@Api
	byte readByte()
		throws IOException;
	
	@Api
	char readChar()
		throws IOException;
	
	@Api
	double readDouble()
		throws IOException;
	
	@Api
	float readFloat()
		throws IOException;
	
	@Api
	void readFully(byte[] __b)
		throws IOException;
	
	@Api
	void readFully(byte[] __b, int __o, int __l)
		throws IOException;
	
	@Api
	int readInt()
		throws IOException;
	
	@Api
	long readLong()
		throws IOException;
	
	@Api
	short readShort()
		throws IOException;
	
	@Api
	String readUTF()
		throws IOException;
	
	@Api
	int readUnsignedByte()
		throws IOException;
	
	@Api
	int readUnsignedShort()
		throws IOException;
	
	@Api
	int skipBytes(int __n)
		throws IOException;
}

