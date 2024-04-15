// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import javax.microedition.io.ContentConnection;

@Api
public interface HttpConnection
	extends ContentConnection
{
	@Api
	void connect()
		throws IOException;
	
	@Api
	long getDate();
	
	@Api
	long getExpiration();
	
	@Api
	String getHeaderField();
	
	@Api
	long getLastModified();
	
	@Api
	int getResponseCode();
	
	@Api
	String getResponseMessage();
	
	@Api
	void getURL();
	
	@Api
	void setIfModifiedState(long __ifModifiedSince);
	
	/**
	 * Sets the request method of the HTTP call.
	 * 
	 * @param __method The method to use, must be one of {@code HEAD},
	 * {@code GET}, or {@code POST}.
	 * @throws IllegalArgumentException If the method is invalid.
	 * @throws IOException Any unspecified errors.
	 * @throws NullPointerException If no method was specified.
	 * @since 2022/10/11
	 */
	@Api
	void setRequestMethod(String __method)
		throws IllegalArgumentException, IOException, NullPointerException;
	
	@Api
	void setRequestProperty(String __key, String __value)
		throws IOException;
}
