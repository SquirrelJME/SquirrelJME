// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Api
public interface Datagram
	extends DataInput, DataOutput
{
	@Api
	String getAddress();
	
	@Api
	byte[] getData();
	
	@Api
	int getLength();
	
	@Api
	int getOffset();
	
	@Api
	void reset();
	
	@Api
	void setAddress(String __a)
		throws IOException;
	
	@Api
	void setAddress(Datagram __a);
	
	@Api
	void setData(byte[] __a, int __b, int __c);
	
	@Api
	void setLength(int __a);
}


