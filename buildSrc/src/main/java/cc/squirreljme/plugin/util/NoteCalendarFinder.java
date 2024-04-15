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
import java.util.regex.Pattern;

/**
 * Finds note files.
 *
 * @since 2020/06/27
 */
public final class NoteCalendarFinder
{
	/**
	 * Not used.
	 * 
	 * @since 2020/06/27
	 */
	private NoteCalendarFinder()
	{
	}
	
	/**
	 * Returns the file map.
	 * 
	 * @param __exe The executable.
	 * @return The file map.
	 * @since 2020/06/27
	 */
	public static NoteUsers findNotes(FossilExe __exe)
	{
		// There will be a number of users
		NoteUsers users = new NoteUsers();
		
		// Determine which notes exist
		for (String fileName : __exe.unversionList())
		{
			// Not a notes files
			if (!fileName.startsWith("developer-notes/"))
				continue;
			
			// Split off
			fileName = fileName.substring("developer-notes/".length());
			
			// Split to determine when
			String[] splice = fileName.split(Pattern.quote("/"));
			if (splice.length != 4)
				continue;
			
			// Determine the date and time
			String userName = splice[0];
			LocalDate date = LocalDate.of(
				Integer.parseInt(splice[1], 10),
				Integer.parseInt(splice[2], 10),
				Integer.parseInt(
					NoteCalendarFinder.__unMkd(splice[3]), 10));
			
			// Add to storage
			users.add(userName, date, fileName);
		}
		
		return users;
	}
	
	
	/**
	 * Removes the markdown extension from the file.
	 * 
	 * @param __s The string to remove from.
	 * @return The string with the extension removed.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	private static String __unMkd(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Must end in this
		if (!__s.endsWith(".mkd"))
			throw new IllegalArgumentException("Missing .mkd: " + __s);
		
		// Remove it
		return __s.substring(0, __s.length() - 4);
	}
}
