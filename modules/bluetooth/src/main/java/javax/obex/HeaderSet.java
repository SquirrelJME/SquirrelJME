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

@Api
public interface HeaderSet
{
	@Api
	int APPLICATION_PARAMETER = 76;
	
	@Api
	int COUNT = 192;
	
	@Api
	int DESCRIPTION = 5;
	
	@Api
	int HTTP = 71;
	
	@Api
	int LENGTH = 195;
	
	@Api
	int NAME = 1;
	
	@Api
	int OBJECT_CLASS = 79;
	
	@Api
	int TARGET = 70;
	
	@Api
	int TIME_4_BYTE = 196;
	
	@Api
	int TIME_ISO_8601 = 68;
	
	@Api
	int TYPE = 66;
	
	@Api
	int WHO = 74;
	
	@Api
	void createAuthenticationChallenge(String __a, boolean __b, boolean __c);
	
	@Api
	Object getHeader(int __a)
		throws IOException;
	
	@Api
	int[] getHeaderList()
		throws IOException;
	
	@Api
	int getResponseCode()
		throws IOException;
	
	@Api
	void setHeader(int __a, Object __b);
}
