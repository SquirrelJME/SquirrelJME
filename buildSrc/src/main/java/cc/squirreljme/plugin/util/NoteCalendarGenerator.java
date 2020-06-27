// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Generator for developer note calendars.
 *
 * @since 2020/06/27
 */
public final class NoteCalendarGenerator
{
	/**
	 * Not used.
	 * 
	 * @since 2020/06/27
	 */
	private NoteCalendarGenerator()
	{
	}
	
	/**
	 * Generates the calendar notes file.
	 * 
	 * @param __exe The executable.
	 * @return The bytes for the generated data.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public static byte[] generate(FossilExe __exe)
		throws IOException, NullPointerException
	{
		return NoteCalendarGenerator.generate(__exe,
			NoteCalendarFinder.findNotes(__exe));
	}
	
	/**
	 * Generates the calendar notes file.
	 * 
	 * @param __exe The executable.
	 * @param __users The users.
	 * @return The bytes for the generated data.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public static byte[] generate(FossilExe __exe, NoteUsers __users)
		throws IOException, NullPointerException
	{
		if (__exe == null || __users == null)
			throw new NullPointerException("NARG");
		
		// The raw bytes
		try (ByteArrayOutputStream raw = new ByteArrayOutputStream();
			 PrintStream out = new PrintStream(
			 	raw, true, "utf-8"))
		{
			// Generate the data
			NoteCalendarGenerator.generate(out, __exe, __users);
			
			// Use the raw bytes
			return raw.toByteArray();
		}
	}
	
	/**
	 * Generates the calendar notes file.
	 * 
	 * @param __out The stream to write to.
	 * @param __exe The executable.
	 * @param __users The users.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public static void generate(PrintStream __out, FossilExe __exe,
		NoteUsers __users)
		throws NullPointerException
	{
		if (__out == null || __exe == null || __users == null)
			throw new NullPointerException("NARG");
		
		// Print Header
		__out.println("# Developer Notes");
		
		// Generate for each user
		for (NoteUser user : __users.users.values())
			NoteCalendarGenerator.__forUser(__out, __exe, user);
		
		// Flush to ensure it is written
		__out.flush();
	}
	
	/**
	 * Generates notes for single user.
	 * 
	 * @param __out The output.
	 * @param __exe The executable.
	 * @param __user The user to generate for.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	private static void __forUser(PrintStream __out, FossilExe __exe,
		NoteUser __user)
		throws NullPointerException
	{
		if (__out == null || __exe == null || __user == null)
			throw new NullPointerException("NARG");
		
		// Print Title
		__out.printf("## %s%n",
			NoteCalendarGenerator.__mapAuthor(__exe, __user.userName));
		
		// End padding
		__out.println();
	}
	
	/**
	 * @param __exe The Fossil executable.
	 * @param __userName The username to map.
	 * @return The mapped author.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	private static String __mapAuthor(FossilExe __exe, String __userName)
		throws NullPointerException
	{
		if (__exe == null || __userName == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}
