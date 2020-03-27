// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.watchdog;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;

public interface WindowedWatchdogTimer
	extends WatchdogTimer
{
	long getClosedWindowTimeout()
		throws IOException, ClosedDeviceException;
	
	@Override
	void start(long __a)
		throws IOException, ClosedDeviceException;
	
	void start(long __a, long __b)
		throws IOException, ClosedDeviceException;
}


