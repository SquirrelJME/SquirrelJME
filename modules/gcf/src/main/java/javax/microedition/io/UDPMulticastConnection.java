// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.io.IOException;

public interface UDPMulticastConnection
	extends UDPDatagramConnection
{
	int getTimeToLive()
		throws IOException;
	
	boolean isDisableLoopback()
		throws IOException;
	
	boolean isJoinSupported();
	
	void join(String __a)
		throws IOException;
	
	void leave(String __a)
		throws IOException;
	
	void setDisableLoopback(boolean __a)
		throws IOException;
	
	void setTimeToLive(int __a)
		throws IOException;
}


