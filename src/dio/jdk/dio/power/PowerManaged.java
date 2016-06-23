// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.power;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;

public interface PowerManaged
{
	public static final int LOWEST_POWER =
		4;
	
	public static final int LOW_POWER =
		2;
	
	public static final int POWER_OFF =
		8;
	
	public static final int POWER_ON =
		1;
	
	public static final long UNLIMITED_DURATION =
		-1L;
	
	public abstract void disablePowerSaving()
		throws IOException, ClosedDeviceException;
	
	public abstract void enablePowerSaving(int __a)
		throws IOException, ClosedDeviceException;
	
	public abstract void enablePowerSaving(int __a, PowerSavingHandler __b)
		throws IOException, ClosedDeviceException;
	
	public abstract PowerManaged.Group getGroup()
		throws IOException, ClosedDeviceException;
	
	public abstract int getPowerState()
		throws IOException, ClosedDeviceException;
	
	public abstract long requestPowerStateChange(int __a, long __b)
		throws IOException, ClosedDeviceException;
	
	public static interface Group
	{
		public abstract boolean contains(PowerManaged __a)
			throws IOException, ClosedDeviceException;
		
		public abstract void setPowerSavingHandler(PowerSavingHandler __a
			)
			throws IOException, ClosedDeviceException;
	}
}


