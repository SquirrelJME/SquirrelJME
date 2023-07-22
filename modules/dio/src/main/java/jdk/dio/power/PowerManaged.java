// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.power;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import jdk.dio.ClosedDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface PowerManaged
{
	@Api
	int LOWEST_POWER =
		4;
	
	@Api
	int LOW_POWER =
		2;
	
	@Api
	int POWER_OFF =
		8;
	
	@Api
	int POWER_ON =
		1;
	
	@Api
	long UNLIMITED_DURATION =
		-1L;
	
	@Api
	void disablePowerSaving()
		throws IOException, ClosedDeviceException;
	
	@Api
	void enablePowerSaving(int __a)
		throws IOException, ClosedDeviceException;
	
	@Api
	void enablePowerSaving(int __a, PowerSavingHandler __b)
		throws IOException, ClosedDeviceException;
	
	@Api
	PowerManaged.Group getGroup()
		throws IOException, ClosedDeviceException;
	
	@Api
	int getPowerState()
		throws IOException, ClosedDeviceException;
	
	@Api
	long requestPowerStateChange(int __a, long __b)
		throws IOException, ClosedDeviceException;
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	interface Group
	{
		@Api
		boolean contains(PowerManaged __a)
			throws IOException, ClosedDeviceException;
		
		@Api
		void setPowerSavingHandler(PowerSavingHandler __a)
			throws IOException, ClosedDeviceException;
	}
}


