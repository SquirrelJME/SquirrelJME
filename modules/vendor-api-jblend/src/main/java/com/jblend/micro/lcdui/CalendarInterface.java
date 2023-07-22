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

@SuppressWarnings({"FieldNamingConvention", "InterfaceWithOnlyOneDirectInheritor"})
public interface CalendarInterface
{
	@Api
	int kYearSelectionMode = 1;
	
	@Api
	int kMonthSelectionMode = 2;
	
	@Api
	int kDaySelectionMode = 3;
	
	@Api
	void initCalendarValues(int var1, int var2);
	
	@Api
	void paint(Graphics var1, int var2, int var3, int var4, int var5, int var6,
		int var7);
	
	@Api
	String getBackCommand();
	
	@Api
	String getSaveCommand();
}
