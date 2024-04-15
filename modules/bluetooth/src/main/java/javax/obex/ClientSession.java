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
public interface ClientSession
	extends Connection
{
	@Api
	HeaderSet connect(HeaderSet __a)
		throws IOException;
	
	@Api
	HeaderSet createHeaderSet();
	
	@Api
	HeaderSet delete(HeaderSet __a)
		throws IOException;
	
	@Api
	HeaderSet disconnect(HeaderSet __a)
		throws IOException;
	
	@Api
	Operation get(HeaderSet __a)
		throws IOException;
	
	@Api
	long getConnectionID();
	
	@Api
	Operation put(HeaderSet __a)
		throws IOException;
	
	@Api
	void setAuthenticator(Authenticator __a);
	
	@Api
	void setConnectionID(long __a);
	
	@Api
	HeaderSet setPath(HeaderSet __a, boolean __b, boolean __c)
		throws IOException;
}
