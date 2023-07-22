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

public interface GaugeInterface
{
	@Api
	int LOST_FOCUS = 1;
	
	@Api
	int VALUE_CHANGED = 2;
	
	@Api
	void init(boolean var1, int var2, int var3, int var4);
	
	@Api
	void setValue(int var1);
	
	@Api
	int getValue();
	
	@Api
	void setMaxValue(int var1);
	
	@Api
	int getMaxValue();
	
	@Api
	void paint(boolean var1, Graphics var2, int var3, int var4);
	
	@Api
	int processEvent(int var1, int var2, int var3, int var4);
	
	@Api
	int getWidth();
	
	@Api
	int getHeight();
}
