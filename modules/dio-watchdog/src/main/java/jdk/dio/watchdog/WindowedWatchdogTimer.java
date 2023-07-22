// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.watchdog;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import jdk.dio.ClosedDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface WindowedWatchdogTimer
	extends WatchdogTimer
{
	@Api
	long getClosedWindowTimeout()
		throws IOException, ClosedDeviceException;
	
	@Override
	void start(long __a)
		throws IOException, ClosedDeviceException;
	
	@Api
	void start(long __a, long __b)
		throws IOException, ClosedDeviceException;
}


