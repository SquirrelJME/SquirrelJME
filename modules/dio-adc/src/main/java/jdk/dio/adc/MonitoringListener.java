// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.adc;

import cc.squirreljme.runtime.cldc.annotation.Api;
import jdk.dio.AsyncErrorHandler;
import jdk.dio.DeviceEventListener;

@Api
public interface MonitoringListener
	extends DeviceEventListener, AsyncErrorHandler<ADCChannel>
{
	
	@Api
	void thresholdReached(MonitoringEvent __a);
}


