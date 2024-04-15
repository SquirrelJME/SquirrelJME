// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.graphics;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class WindowSystem
{
	@Api
	public final int real_width;
	
	@Api
	public final int real_height;
	
	@Api
	public final int virtual_width;
	
	@Api
	public final int virtual_height;
	
	@Api
	public final boolean isColor;
	
	@Api
	public final int numColors;
	
	@Api
	public final boolean hasPointerEvents;
	
	@Api
	public final boolean hasPointerMotionEvents;
	
	@Api
	public final int numSoftkeys;
	
	@Api
	public WindowSystem()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static WindowSystem getDefaultWindowSystem()
	{
		throw Debugging.todo();
	}
}
