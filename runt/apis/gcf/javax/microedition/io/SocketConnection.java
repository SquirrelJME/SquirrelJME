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

public interface SocketConnection
	extends StreamConnection
{
	public static final byte DELAY =
		0;
	
	public static final byte KEEPALIVE =
		2;
	
	public static final byte LINGER =
		1;
	
	public static final byte RCVBUF =
		3;
	
	public static final byte SNDBUF =
		4;
	
	public static final byte TIMEOUT =
		5;
	
	public abstract AccessPoint getAccessPoint()
		throws IOException;
	
	public abstract String getAddress()
		throws IOException;
	
	public abstract String getLocalAddress()
		throws IOException;
	
	public abstract int getLocalPort()
		throws IOException;
	
	public abstract int getPort()
		throws IOException;
	
	public abstract int getSocketOption(byte __a)
		throws IllegalArgumentException, IOException;
	
	public abstract void setSocketOption(byte __a, int __b)
		throws IllegalArgumentException, IOException;
}


