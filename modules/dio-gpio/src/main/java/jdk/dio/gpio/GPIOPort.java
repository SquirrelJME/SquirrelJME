// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.gpio;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface GPIOPort
	extends Device<GPIOPort>
{
	@Api
	int INPUT =
		0;
	
	@Api
	int OUTPUT =
		1;
	
	@Api
	int getDirection()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getMaxValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	GPIOPin[] getPins();
	
	@Api
	int getValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setDirection(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setInputListener(PortListener __a)
		throws IOException, ClosedDeviceException;
	
	@Api
	void setValue(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


