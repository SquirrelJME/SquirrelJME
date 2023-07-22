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
import javax.microedition.io.ContentConnection;

@Api
public interface Operation
	extends ContentConnection
{
	@Api
	void abort()
		throws IOException;
	
	@Api
	HeaderSet getReceivedHeaders()
		throws IOException;
	
	@Api
	int getResponseCode()
		throws IOException;
	
	@Api
	void sendHeaders(HeaderSet __a)
		throws IOException;
}
