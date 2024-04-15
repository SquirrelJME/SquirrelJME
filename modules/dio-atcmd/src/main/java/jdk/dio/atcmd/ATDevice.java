// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.atcmd;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface ATDevice
	extends Device<ATDevice>
{
	@ApiDefinedDeprecated
	@Api
	void abortCommand(String __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@ApiDefinedDeprecated
	@Api
	void escapeToCommandMode()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int getMaxCommandLength()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	boolean isConnected()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	boolean isInCommandMode()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	DataConnection openDataConnection(String __a, CommandResponseHandler __b,
		DataConnectionHandler __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	String sendCommand(String __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void sendCommand(String __a, CommandResponseHandler __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setUnsolicitedResponseHandler(UnsolicitedResponseHandler __a)
		throws IOException, ClosedDeviceException;
	
	@Api
	String tryAbortCommand(String __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	String tryEscapeToCommandMode()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


