// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

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
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	public boolean before(Object __a)
	{
		throw new todo.TODO();
	}
	
	public final void clear()
	{
		throw new todo.TODO();
	}
	
	public final void clear(int __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public Object clone()
	{
		throw new todo.TODO();
	}
	
	@Override
	public int compareTo(Calendar __a)
	{
		throw new todo.TODO();
	}
	
	protected void complete()
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	public int get(int __a)
	{
		throw new todo.TODO();
	}
	
	public int getActualMaximum(int __a)
	{
		throw new todo.TODO();
	}
	
	public int getActualMinimum(int __a)
	{
		throw new todo.TODO();
	}
	
	public int getFirstDayOfWeek()
	{
		throw new todo.TODO();
	}
	
	public int getMinimalDaysInFirstWeek()
	{
		throw new todo.TODO();
	}
	
	public final Date getTime()
	{
		throw new todo.TODO();
	}
	
	public long getTimeInMillis()
	{
		throw new todo.TODO();
	}
	
	public TimeZone getTimeZone()
	{
		throw new todo.TODO();
	}
	
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	protected final int internalGet(int __a)
	{
		throw new todo.TODO();
	}
	
	public boolean isLenient()
	{
		throw new todo.TODO();
	}
	
	public final boolean isSet(int __a)
	{
		throw new todo.TODO();
	}
	
	public void roll(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public void set(int __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public final void set(int __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public final void set(int __a, int __b, int __c, int __d, int __e)
	{
		throw new todo.TODO();
	}
	
	public final void set(int __a, int __b, int __c, int __d, int __e, int
		__f)
	{
		throw new todo.TODO();
	}
	
	public void setFirstDayOfWeek(int __a)
	{
		throw new todo.TODO();
	}
	
	public void setLenient(boolean __a)
	{
		throw new todo.TODO();
	}
	
	public void setMinimalDaysInFirstWeek(int __a)
	{
		throw new todo.TODO();
	}
	
	public final void setTime(Date __a)
	{
		throw new todo.TODO();
	}
	
	public void setTimeInMillis(long __a)
	{
		throw new todo.TODO();
	}
	
	public void setTimeZone(TimeZone __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
	
	public static Calendar getInstance()
	{
		throw new todo.TODO();
	}
	
	public static Calendar getInstance(TimeZone __a)
	{
		throw new todo.TODO();
	}
}

