// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.obex;

import java.io.IOException;
import javax.microedition.io.Connection;

public interface SessionNotifier
	extends Connection
{
	@SuppressWarnings("RedundantThrows")
	Connection acceptAndOpen(ServerRequestHandler __a)
		throws IOException;
	
	@SuppressWarnings("RedundantThrows")
	Connection acceptAndOpen(ServerRequestHandler __a, Authenticator __b)
		throws IOException;
}
