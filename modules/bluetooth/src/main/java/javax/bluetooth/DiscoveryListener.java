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

@Api
public interface DiscoveryListener
{
	@Api
	int INQUIRY_COMPLETED = 0;
	
	@Api
	int INQUIRY_ERROR = 7;
	
	@Api
	int INQUIRY_TERMINATED = 5;
	
	@Api
	int SERVICE_SEARCH_COMPLETED = 1;
	
	@Api
	@SuppressWarnings("FieldNamingConvention")
	int SERVICE_SEARCH_DEVICE_NOT_REACHABLE = 6;
	
	@Api
	int SERVICE_SEARCH_ERROR = 3;
	
	@Api
	int SERVICE_SEARCH_NO_RECORDS = 4;
	
	@Api
	int SERVICE_SEARCH_TERMINATED = 2;
	
	@Api
	void deviceDiscovered(RemoteDevice __a, DeviceClass __b);
	
	@Api
	void inquiryCompleted(int __a);
	
	@Api
	void serviceSearchCompleted(int __a, int __b);
	
	@Api
	void servicesDiscovered(int __a, ServiceRecord[] __b);
}
