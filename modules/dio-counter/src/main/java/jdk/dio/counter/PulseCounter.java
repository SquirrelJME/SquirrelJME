// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.counter;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.UnavailableDeviceException;

public interface PulseCounter
	extends Device<PulseCounter>
{
	public abstract int getCount()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract GPIOPin getSource();
	
	public abstract void resetCounting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void resumeCounting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void startCounting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void startCounting(int __a, long __b, CountingListener 
		__c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void stopCounting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void suspendCounting()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


