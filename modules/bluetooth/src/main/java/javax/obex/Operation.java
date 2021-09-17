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
import javax.microedition.io.ContentConnection;

public interface Operation
	extends ContentConnection
{
	@SuppressWarnings("RedundantThrows")
	void abort()
		throws IOException;
	
	@SuppressWarnings("RedundantThrows")
	HeaderSet getReceivedHeaders()
		throws IOException;
	
	@SuppressWarnings("RedundantThrows")
	int getResponseCode()
		throws IOException;
	
	@SuppressWarnings("RedundantThrows")
	void sendHeaders(HeaderSet __a)
		throws IOException;
}
