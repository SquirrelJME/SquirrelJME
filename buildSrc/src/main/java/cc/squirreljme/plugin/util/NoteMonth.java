// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
	
	/**
	 * Initializes the month.
	 * 
	 * @param __num The month number.
	 * @since 2020/06/27
	 */
	public NoteMonth(int __num)
	{
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
			
		int weekNum = __date.get(IsoFields.WEEK_BASED_YEAR);
		NoteWeek rv = this.weeks.computeIfAbsent(weekNum,
			__key -> new NoteWeek(__key));
		
		rv.add(__date, __fileName);
		
		return rv;
	}
}
