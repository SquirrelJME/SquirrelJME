// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public abstract class Calendar
	implements Cloneable, Comparable<Calendar>
{
	public static final int AM =
		0;
	
	public static final int AM_PM =
		9;
	
	public static final int APRIL =
		3;
	
	public static final int AUGUST =
		7;
	
	public static final int DATE =
		5;
	
	public static final int DAY_OF_MONTH =
		5;
	
	public static final int DAY_OF_WEEK =
		7;
	
	public static final int DAY_OF_WEEK_IN_MONTH =
		8;
	
	public static final int DAY_OF_YEAR =
		6;
	
	public static final int DECEMBER =
		11;
	
	public static final int DST_OFFSET =
		16;
	
	public static final int ERA =
		0;
	
	public static final int FEBRUARY =
		1;
	
	public static final int FIELD_COUNT =
		17;
	
	public static final int FRIDAY =
		6;
	
	public static final int HOUR =
		10;
	
	public static final int HOUR_OF_DAY =
		11;
	
	public static final int JANUARY =
		0;
	
	public static final int JULY =
		6;
	
	public static final int JUNE =
		5;
	
	public static final int MARCH =
		2;
	
	public static final int MAY =
		4;
	
	public static final int MILLISECOND =
		14;
	
	public static final int MINUTE =
		12;
	
	public static final int MONDAY =
		2;
	
	public static final int MONTH =
		2;
	
	public static final int NOVEMBER =
		10;
	
	public static final int OCTOBER =
		9;
	
	public static final int PM =
		1;
	
	public static final int SATURDAY =
		7;
	
	public static final int SECOND =
		13;
	
	public static final int SEPTEMBER =
		8;
	
	public static final int SUNDAY =
		1;
	
	public static final int THURSDAY =
		5;
	
	public static final int TUESDAY =
		3;
	
	public static final int UNDECIMBER =
		12;
	
	public static final int WEDNESDAY =
		4;
	
	public static final int WEEK_OF_MONTH =
		4;
	
	public static final int WEEK_OF_YEAR =
		3;
	
	public static final int YEAR =
		1;
	
	public static final int ZONE_OFFSET =
		15;
	
	protected boolean areFieldsSet;
	
	protected int[] fields;
	
	protected boolean[] isSet;
	
	protected boolean isTimeSet;
	
	protected long time;
	
	protected Calendar()
	{
		throw Debugging.todo();
	}
	
	public abstract void add(int __a, int __b);
	
	protected abstract void computeFields();
	
	protected abstract void computeTime();
	
	public abstract int getGreatestMinimum(int __a);
	
	public abstract int getLeastMaximum(int __a);
	
	public abstract int getMaximum(int __a);
	
	public abstract int getMinimum(int __a);
	
	public abstract void roll(int __a, boolean __b);
	
	public boolean after(Object __a)
	{
		throw Debugging.todo();
	}
	
	public boolean before(Object __a)
	{
		throw Debugging.todo();
	}
	
	public final void clear()
	{
		throw Debugging.todo();
	}
	
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
	
	protected void complete()
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	public int get(int __a)
	{
		throw Debugging.todo();
	}
	
	public int getActualMaximum(int __a)
	{
		throw Debugging.todo();
	}
	
	public int getActualMinimum(int __a)
	{
		throw Debugging.todo();
	}
	
	public int getFirstDayOfWeek()
	{
		throw Debugging.todo();
	}
	
	public int getMinimalDaysInFirstWeek()
	{
		throw Debugging.todo();
	}
	
	public final Date getTime()
	{
		throw Debugging.todo();
	}
	
	public long getTimeInMillis()
	{
		throw Debugging.todo();
	}
	
	public TimeZone getTimeZone()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	protected final int internalGet(int __a)
	{
		throw Debugging.todo();
	}
	
	public boolean isLenient()
	{
		throw Debugging.todo();
	}
	
	public final boolean isSet(int __a)
	{
		throw Debugging.todo();
	}
	
	public void roll(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	public void set(int __a, int __b)
	{
		throw Debugging.todo();
	}
	
	public final void set(int __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public final void set(int __a, int __b, int __c, int __d, int __e)
	{
		throw Debugging.todo();
	}
	
	public final void set(int __a, int __b, int __c, int __d, int __e, int
		__f)
	{
		throw Debugging.todo();
	}
	
	public void setFirstDayOfWeek(int __a)
	{
		throw Debugging.todo();
	}
	
	public void setLenient(boolean __a)
	{
		throw Debugging.todo();
	}
	
	public void setMinimalDaysInFirstWeek(int __a)
	{
		throw Debugging.todo();
	}
	
	public final void setTime(Date __a)
	{
		throw Debugging.todo();
	}
	
	public void setTimeInMillis(long __a)
	{
		throw Debugging.todo();
	}
	
	public void setTimeZone(TimeZone __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	public static Calendar getInstance()
	{
		throw Debugging.todo();
	}
	
	public static Calendar getInstance(TimeZone __a)
	{
		throw Debugging.todo();
	}
}

