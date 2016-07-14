// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.adc;

import java.io.IOException;
import java.nio.IntBuffer;
import jdk.dio.BufferAccess;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedByteOrderException;

public interface ADCChannel
	extends Device<ADCChannel>, BufferAccess<IntBuffer>
{
	public abstract int acquire()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void acquire(IntBuffer __a)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	public abstract int getMaxSamplingInterval()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getMaxValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getMinSamplingInterval()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getMinValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getSamplingInterval()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract double getScaleFactor()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract double getVRefValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setSamplingInterval(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setScaleFactor(double __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void startAcquisition(IntBuffer __a, 
		AcquisitionRoundListener __b)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	public abstract void startAcquisition(IntBuffer __a, IntBuffer __b, 
		AcquisitionRoundListener __c)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	public abstract void startMonitoring(int __a, int __b, MonitoringListener
		__c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void stopAcquisition()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void stopMonitoring()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


