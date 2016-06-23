// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.gpio;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

public interface GPIOPin
	extends Device<GPIOPin>
{
	public static final int INPUT =
		0;
	
	public static final int OUTPUT =
		1;
	
	public abstract int getDirection()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getTrigger()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract boolean getValue()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setDirection(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setInputListener(PinListener __a)
		throws IOException, ClosedDeviceException;
	
	public abstract void setTrigger(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setValue(boolean __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


