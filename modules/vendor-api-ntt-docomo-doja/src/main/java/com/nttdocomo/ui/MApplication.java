// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.constants.TaskStatusType;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public abstract class MApplication
	extends IApplication
{
	@Api
	public static final int	CLOCK_TICK_EVENT =
		3;
	
	@Api
	public static final int	FOLD_CHANGED_EVENT = 
		4;
	
	@Api
	public static final int	MODE_CHANGED_EVENT = 
		1;
	
	@Api
	public static final int	WAKEUP_TIMER_EVENT = 
		2;
	
	@Api
	public MApplication()
	{
	}
	
	@Api
	public final void deactivate()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final int getWakeupTimer()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Is this task currently in the foreground and active?
	 *
	 * @return If this task is active and in the foreground.
	 * @since 2025/06/15
	 */
	@Api
	public final boolean isActive()
	{
		return TaskShelf.status(TaskShelf.current()) != TaskStatusType.ALIVE;
	}
	
	@Api
	public void processSystemEvent(int __type, int __param)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void resetWakeTimer()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void setClockTick(boolean __raise)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void setWakeupTimer(int __time)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void sleep()
	{
		throw Debugging.todo();
	}
}
