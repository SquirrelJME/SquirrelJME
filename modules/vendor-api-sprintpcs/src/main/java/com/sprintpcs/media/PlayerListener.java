// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sprintpcs.media;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface PlayerListener
{
	@Api
	int AUDIO_DEVICE_UNAVAILABLE =
		0;
	
	@Api
	int END_OF_DATA =
		1;
	
	@Api
	int ERROR =
		2;
	
	@Api
	int PAUSED =
		5;
	
	@Api
	int PREEMPTED =
		7;
	
	@Api
	int RESUME =
		6;
	
	@Api
	int STARTED =
		3;
	
	@Api
	int STOPPED =
		4;
	
	@Api
	void playerUpdate(int __unknownA, Object __unknownB);
}
