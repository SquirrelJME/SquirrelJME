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

public interface UiEventType
{
	@Api
	int CLICKED = 1;
	
	@Api
	int SELECTED = 2;
	
	@Api
	int DESELECTED = 3;
	
	@Api
	int CHANGED = 4;
	
	@Api
	int REPAINT = 5;
	
	@Api
	int GOTDISPLAY = 6;
	
	@Api
	int LOSTFOCUS = 7;
	
	@Api
	int REPAINTCOMPONENT = 8;
	
	@Api
	int RELEASEDISPLAY = 9;
}
