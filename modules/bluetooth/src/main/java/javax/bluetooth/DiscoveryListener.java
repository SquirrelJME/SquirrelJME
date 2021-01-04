// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.bluetooth;

public interface DiscoveryListener
{
	int INQUIRY_COMPLETED = 0;
	
	int INQUIRY_ERROR = 7;
	
	int INQUIRY_TERMINATED = 5;
	
	int SERVICE_SEARCH_COMPLETED = 1;
	
	@SuppressWarnings("FieldNamingConvention")
	int SERVICE_SEARCH_DEVICE_NOT_REACHABLE = 6;
	
	int SERVICE_SEARCH_ERROR = 3;
	
	int SERVICE_SEARCH_NO_RECORDS = 4;
	
	int SERVICE_SEARCH_TERMINATED = 2;
	
	void deviceDiscovered(RemoteDevice __a, DeviceClass __b);
	
	void inquiryCompleted(int __a);
	
	void serviceSearchCompleted(int __a, int __b);
	
	void servicesDiscovered(int __a, ServiceRecord[] __b);
}
