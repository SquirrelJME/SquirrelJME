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
public interface GPIOPin
	extends Device<GPIOPin>
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
	int getTrigger()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	boolean getValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setDirection(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setInputListener(PinListener __a)
		throws IOException, ClosedDeviceException;
	
	@Api
	void setTrigger(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setValue(boolean __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


