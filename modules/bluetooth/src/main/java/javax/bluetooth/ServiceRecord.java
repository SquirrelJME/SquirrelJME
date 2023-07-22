// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.bluetooth;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;

@Api
public interface ServiceRecord
{
	@Api
	int AUTHENTICATE_ENCRYPT = 2;
	
	@Api
	int AUTHENTICATE_NOENCRYPT = 1;
	
	@Api
	int NOAUTHENTICATE_NOENCRYPT = 0;
	
	@Api
	int[] getAttributeIDs();
	
	@Api
	DataElement getAttributeValue(int __a);
	
	@Api
	String getConnectionURL(int __a, boolean __b);
	
	@Api
	RemoteDevice getHostDevice();
	
	@Api
	@SuppressWarnings("RedundantThrows")
	boolean populateRecord(int[] __a)
		throws IOException;
	
	@Api
	boolean setAttributeValue(int __a, DataElement __b);
	
	@Api
	void setDeviceServiceClasses(int __a);
}
