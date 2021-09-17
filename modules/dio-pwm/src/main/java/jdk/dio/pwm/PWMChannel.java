// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
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
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedByteOrderException;
import jdk.dio.gpio.GPIOPin;

public interface PWMChannel
	extends Device<PWMChannel>, BufferAccess<IntBuffer>
{
	void generate(int __a, int __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void generate(IntBuffer __a)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	int getMaxPulsePeriod()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getMinPulsePeriod()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	GPIOPin getOutput();
	
	int getPulsePeriod()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	double getScaleFactor()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setPulsePeriod(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setScaleFactor(double __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void startGeneration(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void startGeneration(int __a, int __b, GenerationListener __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void startGeneration(IntBuffer __a, GenerationRoundListener __b)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	void startGeneration(IntBuffer __a, IntBuffer __b,
		GenerationRoundListener __c)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	void stopGeneration()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


