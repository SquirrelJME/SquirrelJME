// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents the month.
 * 
 * @since 2020/06/27
 */
public class NoteMonth
{
	/** The weeks. */
	public final Map<Integer, NoteWeek> weeks =
		new TreeMap<>();
	
	/** The month number. */
	public final int month;
	
	/** The date. */
	public final LocalDate date;
	
	/**
	 * Initializes the month.
	 * 
	 * @param __date The date.
	 * @param __num The month number.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public NoteMonth(LocalDate __date, int __num)
		throws NullPointerException
	{
		if (__date == null)
			throw new NullPointerException("NARG");
		
		this.date = __date;
		this.month = __num;
	}
	
	/**
	 * Adds a week.
	 * 
	 * @param __date The date.
	 * @param __fileName The file name.
	 * @return The week.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public NoteWeek add(LocalDate __date, String __fileName)
		throws NullPointerException
	{
		if (__date == null || __fileName == null)
			throw new NullPointerException("NARG");
			
		int weekNum = __date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		NoteWeek rv = this.weeks.computeIfAbsent(weekNum,
			__key -> new NoteWeek(__key));
		
		rv.add(__date, __fileName);
		
		return rv;
	}
}
