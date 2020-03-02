// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.io.IOException;

public interface DatagramConnection
	extends Connection
{
	AccessPoint[] getAccessPoints()
		throws IOException;
	
	int getMaximumLength()
		throws IOException;
	
	int getNominalLength()
		throws IOException;
	
	Datagram newDatagram(int __a)
		throws IOException;
	
	Datagram newDatagram(int __a, String __b)
		throws IOException;
	
	Datagram newDatagram(byte[] __a, int __b)
		throws IOException;
	
	Datagram newDatagram(byte[] __a, int __b, String __c)
		throws IOException;
	
	void receive(Datagram __a)
		throws IOException;
	
	void send(Datagram __a)
		throws IOException;
}


