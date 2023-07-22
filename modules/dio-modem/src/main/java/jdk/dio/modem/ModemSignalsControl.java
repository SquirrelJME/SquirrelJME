// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.modem;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface ModemSignalsControl<P extends Device<? super P>>
{
	@Api
	int CTS_SIGNAL =
		32;
	
	@Api
	int DCD_SIGNAL =
		2;
	
	@Api
	int DSR_SIGNAL =
		4;
	
	@Api
	int DTR_SIGNAL =
		1;
	
	@Api
	int RI_SIGNAL =
		8;
	
	@Api
	int RTS_SIGNAL =
		16;
	
	@Api
	boolean getSignalState(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setSignalChangeListener(ModemSignalListener<P> __a, int __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setSignalState(int __a, boolean __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


