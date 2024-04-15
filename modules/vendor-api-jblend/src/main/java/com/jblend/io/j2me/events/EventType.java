// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.io.j2me.events;

import cc.squirreljme.runtime.cldc.annotation.Api;

public interface EventType
{
	@Api
	int PRESSED = 1;
	
	@Api
	int RELEASED = 2;
	
	@Api
	int CLICKED = 3;
	
	@Api
	int EXPIRED = 4;
	
	@Api
	int STARTED = 5;
	
	@Api
	int STOPPED = 6;
	
	@Api
	int PAUSED = 7;
	
	@Api
	int REQUEST_PAUSE = 8;
	
	@Api
	int REQUEST_STOP = 9;
	
	@Api
	int REQUEST_RESUME = 10;
	
	@Api
	int CALLING = 11;
	
	@Api
	int RECEIVED = 12;
	
	@Api
	int SCHEDULED_ALARM = 13;
}
