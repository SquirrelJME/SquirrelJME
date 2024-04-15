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
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents the year.
 * 
 * @since 2020/06/27
 */
public class NoteYear
{
	/** The year number. */
	public final int year;
	
	/** The months. */
	public final Map<Integer, NoteMonth> months =
		new TreeMap<>();
	
	/**
	 * Initializes the year.
	 * 
	 * @param __num The number.
	 * @since 2020/06/27
	 */
	public NoteYear(int __num)
	{
		this.year = __num;
	}
	
	/**
	 * Adds the month.
	 * 
	 * @param __date The date.
	 * @param __fileName The file name.
	 * @return The added month.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public NoteMonth add(LocalDate __date, String __fileName)
		throws NullPointerException
	{
		if (__date == null || __fileName == null)
			throw new NullPointerException("NARG");
		
		int monthNum = __date.getMonthValue();
		NoteMonth rv = this.months.computeIfAbsent(monthNum,
			__key -> new NoteMonth(__date, __key));
		
		rv.add(__date, __fileName);
		
		return rv;
	}
}
