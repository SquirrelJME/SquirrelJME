// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.generic;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

public interface GenericDevice
	extends Device<GenericDevice>
{
	public abstract <T> T getControl(GenericDeviceControl<T> __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract <T> void setControl(GenericDeviceControl<T> __a, T __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void setEventListener(int __a, GenericEventListener __b)
		throws IOException, ClosedDeviceException;
}


