// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;

@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@Api
public interface ServerSocketConnection
	extends StreamConnectionNotifier
{
	@Api
	AccessPoint[] getAccessPoints()
		throws IOException;
	
	@Api
	String getLocalAddress()
		throws IOException;
	
	@Api
	int getLocalPort()
		throws IOException;
}


