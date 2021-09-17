// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.bluetooth;

import java.io.IOException;

public interface ServiceRecord
{
	int AUTHENTICATE_ENCRYPT = 2;
	
	int AUTHENTICATE_NOENCRYPT = 1;
	
	int NOAUTHENTICATE_NOENCRYPT = 0;
	
	int[] getAttributeIDs();
	
	DataElement getAttributeValue(int __a);
	
	String getConnectionURL(int __a, boolean __b);
	
	RemoteDevice getHostDevice();
	
	@SuppressWarnings("RedundantThrows")
	boolean populateRecord(int[] __a)
		throws IOException;
	
	boolean setAttributeValue(int __a, DataElement __b);
	
	void setDeviceServiceClasses(int __a);
}
