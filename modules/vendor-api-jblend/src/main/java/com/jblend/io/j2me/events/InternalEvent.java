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

public interface InternalEvent
{
	@Api
	int BEAM_RECEIVE_KVM_EVENT = 0;
	
	@Api
	int PEN_DOWN_KVM_EVENT = 1;
	
	@Api
	int PEN_UP_KVM_EVENT = 2;
	
	@Api
	int PEN_MOVE_KVM_EVENT = 3;
	
	@Api
	int KEY_DOWN_KVM_EVENT = 4;
	
	@Api
	int KEY_UP_KVM_EVENT = 5;
	
	@Api
	int LAST_KVM_EVENT = 6;
	
	@Api
	int APP_STOP_KVM_EVENT = 7;
	
	@Api
	int UI_KVM_EVENT = 8;
	
	@Api
	int TIMER_KVM_EVENT = 9;
	
	@Api
	int PLATFORM_KVM_EVENT = 10;
	
	@Api
	int SOUND_KVM_EVENT = 11;
	
	@Api
	int MEDIA_KVM_EVENT = 12;
	
	@Api
	int DIAL_KVM_EVENT = 14;
	
	@Api
	int APP_RESUME_KVM_EVENT = 100;
	
	@Api
	int APP_SUSPEND_KVM_EVENT = 101;
	
	@Api
	int GENERAL_NOTIFY_EVENT = 200;
	
	@Api
	int ANI_CHAR_EVENT = 221;
	
	@Api
	int USER_KVM_EVENT = 255;
	
	@Api
	int VENDOR_KVM_EVENT = 1000;
	
	@Api
	int OPTIONAL_EVENT_ORIGIN = 10000;
	
	@Api
	int MAX_OPTIONAL_EVENT = 5;
}
