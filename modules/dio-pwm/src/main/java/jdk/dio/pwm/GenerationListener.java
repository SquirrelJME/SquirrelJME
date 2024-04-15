// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.pwm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import jdk.dio.AsyncErrorHandler;
import jdk.dio.DeviceEventListener;

@Api
public interface GenerationListener
	extends DeviceEventListener, AsyncErrorHandler<PWMChannel>
{
	
	@Api
	void pulseGenerationCompleted(GenerationEvent __a);
}


