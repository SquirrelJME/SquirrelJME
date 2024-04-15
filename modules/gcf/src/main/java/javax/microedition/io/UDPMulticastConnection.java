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
public interface UDPMulticastConnection
	extends UDPDatagramConnection
{
	@Api
	int getTimeToLive()
		throws IOException;
	
	@Api
	boolean isDisableLoopback()
		throws IOException;
	
	@Api
	boolean isJoinSupported();
	
	@Api
	void join(String __a)
		throws IOException;
	
	@Api
	void leave(String __a)
		throws IOException;
	
	@Api
	void setDisableLoopback(boolean __a)
		throws IOException;
	
	@Api
	void setTimeToLive(int __a)
		throws IOException;
}


