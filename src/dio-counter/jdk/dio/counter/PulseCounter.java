// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.counter;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.gpio.GPIOPin;

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


