// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.watchdog;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface WatchdogTimer
	extends Device<WatchdogTimer>
{
	@Api
	boolean causedLastReboot()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	long getMaxTimeout()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	long getTimeout()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void refresh()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void start(long __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void stop()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


