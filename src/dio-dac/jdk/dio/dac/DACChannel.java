// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.dac;

import java.io.IOException;
import java.nio.IntBuffer;
import jdk.dio.BufferAccess;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedByteOrderException;

public interface DACChannel
	extends Device<DACChannel>, BufferAccess<IntBuffer>
{
	public abstract void generate(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void generate(IntBuffer __a)
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
	
	public abstract void startGeneration(IntBuffer __a, 
		GenerationRoundListener __b)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	public abstract void startGeneration(IntBuffer __a, IntBuffer __b, 
		GenerationRoundListener __c)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	public abstract void stopGeneration()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


