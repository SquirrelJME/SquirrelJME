// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.time;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Calendar implementation that uses ISO6801 in the UTC timezone.
 *
 * @since 2024/01/30
 */
public class ISO6801Calendar
	extends Calendar
{
	/** The time zone of this given calendar. */
	protected final TimeZone zone;
	
	/**
	 * Initializes the calendar.
	 *
	 * @param __zone The time zone of the calendar.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/30
	 */
	public ISO6801Calendar(TimeZone __zone)
		throws NullPointerException
	{
		if (__zone == null)
			throw new NullPointerException("NARG");
		
		this.zone = __zone;
	}
	
	@Override
	public void add(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Override
	protected void complete()
	{
		// TODO
		Debugging.todoNote("ISO6801Calendar.complete()");
	}
	
	@Override
	protected void computeFields()
	{
		// TODO
		Debugging.todoNote("ISO6801Calendar.computeFields()");
	}
	
	@Override
	protected void computeTime()
	{
		// TODO
		Debugging.todoNote("ISO6801Calendar.computeTime()");
	}
	
	@Override
	public int getGreatestMinimum(int __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getLeastMaximum(int __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getMaximum(int __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getMinimum(int __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public void roll(int __a, boolean __b)
	{
		throw Debugging.todo();
	}
}
