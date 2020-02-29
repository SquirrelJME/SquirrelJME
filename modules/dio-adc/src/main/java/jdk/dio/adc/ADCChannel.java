// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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
	int acquire()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void acquire(IntBuffer __a)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	int getMaxSamplingInterval()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getMaxValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getMinSamplingInterval()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getMinValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getSamplingInterval()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	double getScaleFactor()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	double getVRefValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setSamplingInterval(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setScaleFactor(double __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void startAcquisition(IntBuffer __a, AcquisitionRoundListener __b)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	void startAcquisition(IntBuffer __a, IntBuffer __b,
		AcquisitionRoundListener __c)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	void startMonitoring(int __a, int __b, MonitoringListener __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void stopAcquisition()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void stopMonitoring()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


