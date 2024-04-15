// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.obex;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import javax.microedition.io.Connection;

@Api
public interface SessionNotifier
	extends Connection
{
	@Api
	Connection acceptAndOpen(ServerRequestHandler __a)
		throws IOException;
	
	@Api
	Connection acceptAndOpen(ServerRequestHandler __a, Authenticator __b)
		throws IOException;
}
