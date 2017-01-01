// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
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
	public static final int INPUT =
		0;
	
	public static final int OUTPUT =
		1;
	
	public abstract int getDirection()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getMaxValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract GPIOPin[] getPins();
	
	public abstract int getValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setDirection(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setInputListener(PortListener __a)
		throws IOException, ClosedDeviceException;
	
	public abstract void setValue(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


