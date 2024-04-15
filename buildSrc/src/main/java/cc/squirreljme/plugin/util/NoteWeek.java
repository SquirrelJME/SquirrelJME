// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents the week.
 * 
 * @since 2020/06/27
 */
public class NoteWeek
{
	/** The weekdays. */
	public final Map<DayOfWeek, NoteDay> weekdays =
		new TreeMap<>();
	
	/** The week of the year. */
	public final int week;
	
	/**
	 * Initializes the week.
	 * 
	 * @param __num The week number.
	 * @since 2020/06/27
	 */
	public NoteWeek(int __num)
	{
		this.week = __num;
	}
	
	/**
	 * Adds a day.
	 * 
	 * @param __date The date. 
	 * @param __fileName The file name.
	 * @return The day.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public NoteDay add(LocalDate __date, String __fileName)
		throws NullPointerException
	{
		if (__date == null || __fileName == null)
			throw new NullPointerException("NARG");
		
		return this.weekdays.computeIfAbsent(__date.getDayOfWeek(),
			__key -> new NoteDay(__date, __fileName));
	}
}
