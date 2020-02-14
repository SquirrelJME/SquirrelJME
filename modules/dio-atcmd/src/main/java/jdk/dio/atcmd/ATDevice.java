// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.atcmd;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

public interface ATDevice
	extends Device<ATDevice>
{
	@Deprecated
	public abstract void abortCommand(String __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void close()
		throws IOException;
	
	@Deprecated
	public abstract void escapeToCommandMode()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int getMaxCommandLength()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract boolean isConnected()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract boolean isInCommandMode()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract DataConnection openDataConnection(String __a, 
		CommandResponseHandler __b, DataConnectionHandler __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract String sendCommand(String __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void sendCommand(String __a, CommandResponseHandler __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setUnsolicitedResponseHandler(
		UnsolicitedResponseHandler __a)
		throws IOException, ClosedDeviceException;
	
	public abstract String tryAbortCommand(String __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract String tryEscapeToCommandMode()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


