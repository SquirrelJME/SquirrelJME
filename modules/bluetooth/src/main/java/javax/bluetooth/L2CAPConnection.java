// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.bluetooth;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import javax.microedition.io.Connection;

@Api
public interface L2CAPConnection
	extends Connection
{
	@Api
	int DEFAULT_MTU = 672;
	
	@Api
	int MINIMUM_MTU = 48;
	
	@Api
	int getReceiveMTU()
		throws IOException;
	
	@Api
	int getTransmitMTU()
		throws IOException;
	
	@Api
	boolean ready()
		throws IOException;
	
	@Api
	int receive(byte[] __a)
		throws IOException;
	
	@Api
	void send(byte[] __a)
		throws IOException;
}
