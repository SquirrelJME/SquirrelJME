// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.nttdocomo.ui.Canvas;
import com.nttdocomo.ui.ShortTimer;

/**
 * A timer which sends timer events to a given listener.
 * 
 * Unlike {@link ShortTimer} which uses {@link Canvas#processEvent(int, int)},
 * this one sends events to a {@link TimerListener}.
 * 
 * @since 2022/10/10
 */
public final class Timer
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
	
	public void setListener(TimerListener __listener)
	{
		throw Debugging.todo();
	}
	
	public void setRepeat(boolean __repeat)
	{
		throw Debugging.todo();
	}
	
	public void setTime(int __interval)
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
}
