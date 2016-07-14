// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.modem;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

public interface ModemSignalsControl<P extends Device<? super P>>
{
	public static final int CTS_SIGNAL =
		32;
	
	public static final int DCD_SIGNAL =
		2;
	
	public static final int DSR_SIGNAL =
		4;
	
	public static final int DTR_SIGNAL =
		1;
	
	public static final int RI_SIGNAL =
		8;
	
	public static final int RTS_SIGNAL =
		16;
	
	public abstract boolean getSignalState(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setSignalChangeListener(ModemSignalListener<P> __a, 
		int __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setSignalState(int __a, boolean __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


