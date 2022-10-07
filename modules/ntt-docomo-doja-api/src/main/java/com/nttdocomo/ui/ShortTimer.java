// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public final class ShortTimer
	implements TimeKeeper
{
	@Override
	public void dispose()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getMinTimeInterval()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getResolution()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void start()
	{
		throw Debugging.todo();
	}
	
	@Override
	public void stop()
	{
		throw Debugging.todo();
	}
	
	public static ShortTimer getShortTimer(Canvas __canvas, int __id,
		int __time, boolean __repeat)
	{
		throw Debugging.todo();
	}
}
