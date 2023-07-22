// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.counter;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.gpio.GPIOPin;

@SuppressWarnings("DuplicateThrows")
@Api
public interface PulseCounter
	extends Device<PulseCounter>
{
	@Api
	int getCount()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	GPIOPin getSource();
	
	@Api
	void resetCounting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void resumeCounting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void startCounting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void startCounting(int __a, long __b, CountingListener __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void stopCounting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void suspendCounting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


