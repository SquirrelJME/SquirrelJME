// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public abstract class Calendar
	implements Cloneable, Comparable<Calendar>
{
	@Api
	public static final int AM =
		0;
	
	@Api
	public static final int AM_PM =
		9;
	
	@Api
	public static final int APRIL =
		3;
	
	@Api
	public static final int AUGUST =
		7;
	
	@Api
	public static final int DATE =
		5;
	
	@Api
	public static final int DAY_OF_MONTH =
		5;
	
	@Api
	public static final int DAY_OF_WEEK =
		7;
	
	@Api
	public static final int DAY_OF_WEEK_IN_MONTH =
		8;
	
	@Api
	public static final int DAY_OF_YEAR =
		6;
	
	@Api
	public static final int DECEMBER =
		11;
	
	@Api
	public static final int DST_OFFSET =
		16;
	
	@Api
	public static final int ERA =
		0;
	
	@Api
	public static final int FEBRUARY =
		1;
	
	@Api
	public static final int FIELD_COUNT =
		17;
	
	@Api
	public static final int FRIDAY =
		6;
	
	@Api
	public static final int HOUR =
		10;
	
	@Api
	public static final int HOUR_OF_DAY =
		11;
	
	@Api
	public static final int JANUARY =
		0;
	
	@Api
	public static final int JULY =
		6;
	
	@Api
	public static final int JUNE =
		5;
	
	@Api
	public static final int MARCH =
		2;
	
	@Api
	public static final int MAY =
		4;
	
	@Api
	public static final int MILLISECOND =
		14;
	
	@Api
	public static final int MINUTE =
		12;
	
	@Api
	public static final int MONDAY =
		2;
	
	@Api
	public static final int MONTH =
		2;
	
	@Api
	public static final int NOVEMBER =
		10;
	
	@Api
	public static final int OCTOBER =
		9;
	
	@Api
	public static final int PM =
		1;
	
	@Api
	public static final int SATURDAY =
		7;
	
	@Api
	public static final int SECOND =
		13;
	
	@Api
	public static final int SEPTEMBER =
		8;
	
	@Api
	public static final int SUNDAY =
		1;
	
	@Api
	public static final int THURSDAY =
		5;
	
	@Api
	public static final int TUESDAY =
		3;
	
	@SuppressWarnings("SpellCheckingInspection")
	@Api
	public static final int UNDECIMBER =
		12;
	
	@Api
	public static final int WEDNESDAY =
		4;
	
	@Api
	public static final int WEEK_OF_MONTH =
		4;
	
	@Api
	public static final int WEEK_OF_YEAR =
		3;
	
	@Api
	public static final int YEAR =
		1;
	
	@Api
	public static final int ZONE_OFFSET =
		15;
	
	@Api
	protected boolean areFieldsSet;
	
	@Api
	protected int[] fields;
	
	@Api
	protected boolean[] isSet;
	
	@Api
	protected boolean isTimeSet;
	
	@Api
	protected long time;
	
	@Api
	protected Calendar()
	{
		throw Debugging.todo();
	}
	
	@Api
	public abstract void add(int __a, int __b);
	
	@Api
	protected abstract void computeFields();
	
	@Api
	protected abstract void computeTime();
	
	@Api
	public abstract int getGreatestMinimum(int __a);
	
	@Api
	public abstract int getLeastMaximum(int __a);
	
	@Api
	public abstract int getMaximum(int __a);
	
	@Api
	public abstract int getMinimum(int __a);
	
	@Api
	public abstract void roll(int __a, boolean __b);
	
	@Api
	public boolean after(Object __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean before(Object __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void clear()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void clear(int __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public Object clone()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int compareTo(Calendar __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	protected void complete()
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int get(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getActualMaximum(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getActualMinimum(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getFirstDayOfWeek()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getMinimalDaysInFirstWeek()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final Date getTime()
	{
		throw Debugging.todo();
	}
	
	@Api
	public long getTimeInMillis()
	{
		throw Debugging.todo();
	}
	
	@Api
	public TimeZone getTimeZone()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	@Api
	protected final int internalGet(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isLenient()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final boolean isSet(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void roll(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void set(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void set(int __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void set(int __a, int __b, int __c, int __d, int __e)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void set(int __a, int __b, int __c, int __d, int __e, int
		__f)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setFirstDayOfWeek(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setLenient(boolean __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setMinimalDaysInFirstWeek(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void setTime(Date __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setTimeInMillis(long __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setTimeZone(TimeZone __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Calendar getInstance()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Calendar getInstance(TimeZone __a)
	{
		throw Debugging.todo();
	}
}

