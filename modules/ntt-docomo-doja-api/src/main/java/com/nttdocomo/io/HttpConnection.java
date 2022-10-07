// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.io;

import java.io.IOException;
import javax.microedition.io.ContentConnection;

public interface HttpConnection
	extends ContentConnection
{
	void connect()
		throws IOException;
	
	long getDate();
	
	long getExpiration();
	
	String getHeaderField();
	
	long getLastModified();
	
	int getResponseCode();
	
	String getResponseMessage();
	
	void getURL();
	
	void setIfModifiedState(long __ifModifiedSince);
	
	void setRequestMethod(String __method)
		throws IOException;
	
	void setRequestProperty(String __key, String __value)
		throws IOException;
}
