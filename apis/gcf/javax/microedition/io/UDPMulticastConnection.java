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

import java.io.IOException;

public interface UDPMulticastConnection
	extends UDPDatagramConnection
{
	public abstract int getTimeToLive()
		throws IOException;
	
	public abstract boolean isDisableLoopback()
		throws IOException;
	
	public abstract boolean isJoinSupported();
	
	public abstract void join(String __a)
		throws IOException;
	
	public abstract void leave(String __a)
		throws IOException;
	
	public abstract void receive(Datagram __a)
		throws IOException;
	
	public abstract void send(Datagram __a)
		throws IOException;
	
	public abstract void setDisableLoopback(boolean __a)
		throws IOException;
	
	public abstract void setTimeToLive(int __a)
		throws IOException;
}


