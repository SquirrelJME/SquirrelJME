// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.counter;

import jdk.dio.AsyncErrorHandler;
import jdk.dio.DeviceEventListener;

public interface CountingListener
	extends DeviceEventListener, AsyncErrorHandler<PulseCounter>
{
	public abstract void countValueAvailable(CountingEvent __a);
	
	public abstract void failed(Throwable __a, PulseCounter __b);
}


