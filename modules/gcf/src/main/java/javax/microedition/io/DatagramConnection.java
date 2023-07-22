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
import java.io.IOException;

@Api
public interface DatagramConnection
	extends Connection
{
	@Api
	AccessPoint[] getAccessPoints()
		throws IOException;
	
	@Api
	int getMaximumLength()
		throws IOException;
	
	@Api
	int getNominalLength()
		throws IOException;
	
	@Api
	Datagram newDatagram(int __a)
		throws IOException;
	
	@Api
	Datagram newDatagram(int __a, String __b)
		throws IOException;
	
	@Api
	Datagram newDatagram(byte[] __a, int __b)
		throws IOException;
	
	@Api
	Datagram newDatagram(byte[] __a, int __b, String __c)
		throws IOException;
	
	@Api
	void receive(Datagram __a)
		throws IOException;
	
	@Api
	void send(Datagram __a)
		throws IOException;
}


