// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.gpio;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

public interface GPIOPort
	extends Device<GPIOPort>
{
	int INPUT =
		0;
	
	int OUTPUT =
		1;
	
	int getDirection()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int getMaxValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	GPIOPin[] getPins();
	
	int getValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setDirection(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	void setInputListener(PortListener __a)
		throws IOException, ClosedDeviceException;
	
	void setValue(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


