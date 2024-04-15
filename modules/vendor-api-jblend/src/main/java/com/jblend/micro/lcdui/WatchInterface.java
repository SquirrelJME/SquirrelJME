// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.micro.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import javax.microedition.lcdui.Graphics;

public interface WatchInterface
{
	@Api
	int kHourSelection = 1;
	
	@Api
	int kMinuteSelection = 2;
	
	@Api
	void initWatchValues(int var1, int var2);
	
	@Api
	void paint(Graphics var1, int var2, int var3, int var4);
	
	@Api
	String getBackCommand();
	
	@Api
	String getSaveCommand();
}

