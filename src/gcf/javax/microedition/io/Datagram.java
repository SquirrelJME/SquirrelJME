// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Datagram
	extends DataInput, DataOutput
{
	public abstract String getAddress();
	
	public abstract byte[] getData();
	
	public abstract int getLength();
	
	public abstract int getOffset();
	
	public abstract void reset();
	
	public abstract void setAddress(String __a)
		throws IOException;
	
	public abstract void setAddress(Datagram __a);
	
	public abstract void setData(byte[] __a, int __b, int __c);
	
	public abstract void setLength(int __a);
}


