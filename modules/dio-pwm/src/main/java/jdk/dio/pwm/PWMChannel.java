// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.pwm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import java.nio.IntBuffer;
import jdk.dio.BufferAccess;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedByteOrderException;
import jdk.dio.gpio.GPIOPin;

@SuppressWarnings("DuplicateThrows")
@Api
public interface PWMChannel
	extends Device<PWMChannel>, BufferAccess<IntBuffer>
{
	@Api
	void generate(int __a, int __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void generate(IntBuffer __a)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	@Api
	int getMaxPulsePeriod()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getMinPulsePeriod()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	GPIOPin getOutput();
	
	@Api
	int getPulsePeriod()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	double getScaleFactor()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setPulsePeriod(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setScaleFactor(double __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void startGeneration(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void startGeneration(int __a, int __b, GenerationListener __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void startGeneration(IntBuffer __a, GenerationRoundListener __b)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	@Api
	void startGeneration(IntBuffer __a, IntBuffer __b,
		GenerationRoundListener __c)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	@Api
	void stopGeneration()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


