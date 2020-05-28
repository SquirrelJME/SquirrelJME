// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.adc;

import jdk.dio.AsyncErrorHandler;
import jdk.dio.DeviceEventListener;

public interface MonitoringListener
	extends DeviceEventListener, AsyncErrorHandler<ADCChannel>
{
	
	void thresholdReached(MonitoringEvent __a);
}


