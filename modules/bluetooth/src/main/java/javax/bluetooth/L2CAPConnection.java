// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.bluetooth;

import java.io.IOException;
import javax.microedition.io.Connection;

public interface L2CAPConnection
	extends Connection
{
	int DEFAULT_MTU = 672;
	
	int MINIMUM_MTU = 48;
	
	@SuppressWarnings("RedundantThrows")
	int getReceiveMTU()
		throws IOException;
	
	@SuppressWarnings("RedundantThrows")
	int getTransmitMTU()
		throws IOException;
	
	@SuppressWarnings("RedundantThrows")
	boolean ready()
		throws IOException;
	
	@SuppressWarnings("RedundantThrows")
	int receive(byte[] __a)
		throws IOException;
	
	@SuppressWarnings("RedundantThrows")
	void send(byte[] __a)
		throws IOException;
}
