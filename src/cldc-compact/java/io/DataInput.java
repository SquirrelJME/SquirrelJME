// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.io;

public interface DataInput
{
	public abstract boolean readBoolean()
		throws IOException;
	
	public abstract byte readByte()
		throws IOException;
	
	public abstract char readChar()
		throws IOException;
	
	public abstract double readDouble()
		throws IOException;
	
	public abstract float readFloat()
		throws IOException;
	
	public abstract void readFully(byte[] __a)
		throws IOException;
	
	public abstract void readFully(byte[] __a, int __b, int __c)
		throws IOException;
	
	public abstract int readInt()
		throws IOException;
	
	public abstract long readLong()
		throws IOException;
	
	public abstract short readShort()
		throws IOException;
	
	public abstract String readUTF()
		throws IOException;
	
	public abstract int readUnsignedByte()
		throws IOException;
	
	public abstract int readUnsignedShort()
		throws IOException;
	
	public abstract int skipBytes(int __a)
		throws IOException;
}

