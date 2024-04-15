// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.dac;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import java.nio.IntBuffer;
import jdk.dio.BufferAccess;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedByteOrderException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface DACChannel
	extends Device<DACChannel>, BufferAccess<IntBuffer>
{
	@Api
	void generate(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void generate(IntBuffer __a)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	@Api
	int getMaxSamplingInterval()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getMaxValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getMinSamplingInterval()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getMinValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getSamplingInterval()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	double getScaleFactor()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	double getVRefValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setSamplingInterval(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setScaleFactor(double __a)
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


