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
 * Represents a single user.
 * 
 * @since 2020/06/27
 */
public class NoteUser
{
	/** The user name. */
	public final String userName;
	
	/** The years. */
	public final Map<Integer, NoteYear> years =
		new TreeMap<>();
	
	/**
	 * Initializes the user.
	 * 
	 * @param __userName The user name.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public NoteUser(String __userName)
		throws NullPointerException
	{
		this.userName = __userName;
	}
	
	/**
	 * Adds the year.
	 * 
	 * @param __date The date.
	 * @param __fileName The file name.
	 * @return The added year.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public NoteYear add(LocalDate __date, String __fileName)
		throws NullPointerException
	{
		if (__date == null || __fileName == null)
			throw new NullPointerException("NARG");
		
		int yearNum = __date.getYear();
		NoteYear rv = this.years.computeIfAbsent(yearNum,
			__key -> new NoteYear(__key));
		
		rv.add(__date, __fileName);
		
		return rv;
	}
}
