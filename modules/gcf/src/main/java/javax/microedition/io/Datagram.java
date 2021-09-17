// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Datagram
	extends DataInput, DataOutput
{
	String getAddress();
	
	byte[] getData();
	
	int getLength();
	
	int getOffset();
	
	void reset();
	
	void setAddress(String __a)
		throws IOException;
	
	void setAddress(Datagram __a);
	
	void setData(byte[] __a, int __b, int __c);
	
	void setLength(int __a);
}


