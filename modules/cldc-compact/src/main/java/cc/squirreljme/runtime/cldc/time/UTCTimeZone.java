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
import java.util.Date;
import java.util.TimeZone;

/**
 * A time zone that can only represent UTC.
 *
 * @since 2024/01/30
 */
public class UTCTimeZone
	extends TimeZone
{
	@Override
	public int getOffset(int __a, int __b, int __c, int __d, int __e,
		int __f)
	{
		throw Debugging.todo();
	}
	
	@Override
	public int getRawOffset()
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean inDaylightTime(Date __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public void setRawOffset(int __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean useDaylightTime()
	{
		throw Debugging.todo();
	}
}
