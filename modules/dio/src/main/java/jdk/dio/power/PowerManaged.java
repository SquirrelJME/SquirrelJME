// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.power;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;

public interface PowerManaged
{
	int LOWEST_POWER =
		4;
	
	int LOW_POWER =
		2;
	
	int POWER_OFF =
		8;
	
	int POWER_ON =
		1;
	
	long UNLIMITED_DURATION =
		-1L;
	
	void disablePowerSaving()
		throws IOException, ClosedDeviceException;
	
	void enablePowerSaving(int __a)
		throws IOException, ClosedDeviceException;
	
	void enablePowerSaving(int __a, PowerSavingHandler __b)
		throws IOException, ClosedDeviceException;
	
	PowerManaged.Group getGroup()
		throws IOException, ClosedDeviceException;
	
	int getPowerState()
		throws IOException, ClosedDeviceException;
	
	long requestPowerStateChange(int __a, long __b)
		throws IOException, ClosedDeviceException;
	
	interface Group
	{
		boolean contains(PowerManaged __a)
			throws IOException, ClosedDeviceException;
		
		void setPowerSavingHandler(PowerSavingHandler __a)
			throws IOException, ClosedDeviceException;
	}
}


