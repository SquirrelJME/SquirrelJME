// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.pwm;

import java.io.IOException;
import java.nio.IntBuffer;
import jdk.dio.BufferAccess;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedByteOrderException;

public interface PWMChannel
	extends Device<PWMChannel>, BufferAccess<IntBuffer>
{
	public abstract void generate(int __a, int __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void generate(IntBuffer __a)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	public abstract int getMaxPulsePeriod()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getMinPulsePeriod()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract GPIOPin getOutput();
	
	public abstract int getPulsePeriod()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract double getScaleFactor()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setPulsePeriod(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setScaleFactor(double __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void startGeneration(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void startGeneration(int __a, int __b, GenerationListener
		__c)
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


