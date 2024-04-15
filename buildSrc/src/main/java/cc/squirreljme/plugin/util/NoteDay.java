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

/**
 * Represents the day.
 * 
 * @since 2020/06/27
 */
public class NoteDay
{
	/** The date. */
	public final LocalDate date;
	
	/** The file name. */
	public final String fileName;
	
	/**
	 * Initializes the day.
	 * 
	 * @param __date The date.
	 * @param __fileName The file name.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public NoteDay(LocalDate __date, String __fileName)
		throws NullPointerException
	{
		if (__date == null || __fileName == null)
			throw new NullPointerException("NARG");
		
		this.date = __date;
		this.fileName = __fileName;
	}
}
