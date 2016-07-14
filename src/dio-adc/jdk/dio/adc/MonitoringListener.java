// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.adc;

import jdk.dio.AsyncErrorHandler;
import jdk.dio.DeviceEventListener;

public interface MonitoringListener
	extends DeviceEventListener, AsyncErrorHandler<ADCChannel>
{
	public abstract void failed(Throwable __a, ADCChannel __b);
	
	public abstract void thresholdReached(MonitoringEvent __a);
}


