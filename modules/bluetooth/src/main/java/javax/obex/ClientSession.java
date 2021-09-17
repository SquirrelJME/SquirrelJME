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

public interface ClientSession
	extends Connection
{
	@SuppressWarnings("RedundantThrows")
	HeaderSet connect(HeaderSet __a)
		throws IOException;
	
	HeaderSet createHeaderSet();
	
	@SuppressWarnings("RedundantThrows")
	HeaderSet delete(HeaderSet __a)
		throws IOException;
	
	@SuppressWarnings("RedundantThrows")
	HeaderSet disconnect(HeaderSet __a)
		throws IOException;
	
	@SuppressWarnings("RedundantThrows")
	Operation get(HeaderSet __a)
		throws IOException;
	
	long getConnectionID();
	
	@SuppressWarnings("RedundantThrows")
	Operation put(HeaderSet __a)
		throws IOException;
	
	void setAuthenticator(Authenticator __a);
	
	void setConnectionID(long __a);
	
	@SuppressWarnings("RedundantThrows")
	HeaderSet setPath(HeaderSet __a, boolean __b, boolean __c)
		throws IOException;
}
