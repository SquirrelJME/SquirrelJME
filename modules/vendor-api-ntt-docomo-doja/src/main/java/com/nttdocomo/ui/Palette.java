// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class Palette
{
	@Api
	public Palette()
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	@Api
	public Palette(int[] __colors)
		throws IllegalArgumentException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getEntry(int __index)
		throws ArrayIndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getEntryCount()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setEntry(int __index, int __color)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException
	{
		throw Debugging.todo();
	}
}
