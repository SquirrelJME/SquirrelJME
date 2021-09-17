// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.util.Date;
import java.util.TimeZone;

public class DateField
	extends Item
{
	public static final int DATE =
		1;
	
	public static final int DATE_TIME =
		3;
	
	public static final int TIME =
		2;
	
	/** The timezone to use. */
	private final TimeZone _zone;
	
	/**
	 * Initializes the date field with the default timezone.
	 *
	 * @param __l The label to use.
	 * @param __m The mode used.
	 * @throws IllegalArgumentException If the mode is not valid.
	 * @since 2019/05/17
	 */
	public DateField(String __l, int __m)
		throws IllegalArgumentException
	{
		this(__l, __m, null);
	}
	
	/**
	 * Initializes the date field with the default timezone.
	 *
	 * @param __l The label to use.
	 * @param __m The mode used.
	 * @param __z The time zone to use, {@code null} is the default.
	 * @throws IllegalArgumentException If the mode is not valid.
	 * @since 2019/05/17
	 */
	public DateField(String __l, int __m, TimeZone __z)
		throws IllegalArgumentException
	{
		super(__l);
		
		// {@squirreljme.error EB1g Invalid date field mode. (The mode)}
		if (__m != DateField.DATE && __m != DateField.DATE_TIME && __m != DateField.TIME)
			throw new IllegalArgumentException("EB1g " + __m);
		
		this._zone = __z;
	}
	
	public Date getDate()
	{
		throw new todo.TODO();
	}
	
	public int getInputMode()
	{
		throw new todo.TODO();
	}
	
	public void setDate(Date __a)
	{
		throw new todo.TODO();
	}
	
	public void setInputMode(int __a)
	{
		throw new todo.TODO();
	}
}


