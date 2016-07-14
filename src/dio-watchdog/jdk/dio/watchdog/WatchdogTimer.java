// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.watchdog;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

public interface WatchdogTimer
	extends Device<WatchdogTimer>
{
	public abstract boolean causedLastReboot()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract long getMaxTimeout()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract long getTimeout()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void refresh()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void start(long __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void stop()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


