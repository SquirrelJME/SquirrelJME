// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.obex;

import java.io.IOException;

public interface HeaderSet
{
	int APPLICATION_PARAMETER = 76;
	
	int COUNT = 192;
	
	int DESCRIPTION = 5;
	
	int HTTP = 71;
	
	int LENGTH = 195;
	
	int NAME = 1;
	
	int OBJECT_CLASS = 79;
	
	int TARGET = 70;
	
	int TIME_4_BYTE = 196;
	
	int TIME_ISO_8601 = 68;
	
	int TYPE = 66;
	
	int WHO = 74;
	
	void createAuthenticationChallenge(String __a, boolean __b, boolean __c);
	
	@SuppressWarnings("RedundantThrows")
	Object getHeader(int __a)
		throws IOException;
	
	@SuppressWarnings("RedundantThrows")
	int[] getHeaderList()
		throws IOException;
	
	@SuppressWarnings("RedundantThrows")
	int getResponseCode()
		throws IOException;
	
	void setHeader(int __a, Object __b);
}
