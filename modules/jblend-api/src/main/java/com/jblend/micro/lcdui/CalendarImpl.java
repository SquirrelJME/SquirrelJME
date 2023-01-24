// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.micro.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Graphics;

@Api
public class CalendarImpl implements CalendarInterface {
	
	@Override
	public void initCalendarValues(int var1, int var2)
	{
		throw Debugging.todo();
	}

    @Override
	public void paint(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7)
	{
		throw Debugging.todo();
	}

    @Override
	public String getBackCommand()
	{
		throw Debugging.todo();
	}

    @Override
	public String getSaveCommand()
	{
		throw Debugging.todo();
	}
}
