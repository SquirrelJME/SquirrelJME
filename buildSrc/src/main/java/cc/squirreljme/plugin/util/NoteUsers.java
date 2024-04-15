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
 * Represents users.
 * 
 * @since 2020/06/27
 */
public class NoteUsers
{
	/** Represents the users. */
	public final Map<String, NoteUser> users =
		new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	
	/**
	 * Adds the user.
	 * 
	 * @param __userName The user name.
	 * @param __date The date.
	 * @param __fileName The file name.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public NoteUser add(String __userName, LocalDate __date,
		String __fileName)
		throws NullPointerException
	{
		if (__userName == null || __date == null || __fileName == null)
			throw new NullPointerException("NARG");
		
		NoteUser rv = this.users.computeIfAbsent(__userName,
			__key -> new NoteUser(__key));
		
		rv.add(__date, __fileName);
		
		return rv;
	}
}
