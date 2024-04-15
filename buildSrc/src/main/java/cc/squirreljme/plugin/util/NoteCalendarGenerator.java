// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.IsoFields;
import java.util.Locale;

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
		__out.print("# Developer Notes");
		__out.println();
		
		// Spacing
		__out.println();
		
		// Generate for each user
		for (NoteUser user : __users.users.values())
			NoteCalendarGenerator.__forUser(__out, __exe, user);
		
		// Flush to ensure it is written
		__out.flush();
	}
	
	/**
	 * Generates the calendar notes file and stores it.
	 * 
	 * @param __exe The executable.
	 * @throws IOException If it could not be generated.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public static void generateAndStore(FossilExe __exe)
		throws IOException, NullPointerException
	{
		__exe.unversionStoreBytes("developer-notes/index.mkd",
			NoteCalendarGenerator.generate(__exe,
			NoteCalendarFinder.findNotes(__exe)));
	}
	
	/**
	 * Generates for the given month.
	 * 
	 * @param __out The output.
	 * @param __month The month to generate.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	private static void __forMonth(PrintStream __out, NoteMonth __month)
		throws NullPointerException
	{
		if (__out == null || __month == null)
			throw new NullPointerException("NARG");
		
		// Print Title
		__out.printf("#### %s", __month.date.getMonth()
			.getDisplayName(TextStyle.FULL, Locale.US));
		__out.println();
		
		// Spacing
		__out.println();
		
		// Determine the first and last weeks
		int firstWeek = __month.date.withDayOfMonth(1).get(
			IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		int lastWeek = __month.date.plusMonths(1).withDayOfMonth(1)
			.minusDays(1).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
		
		// If the last week of the year is week 1, then that means Monday
		// is in December but the week end (Sunday) is in January. This means
		// that our algorithm breaks because lastWeek would always be less
		// than the first week so we just make it 53
		if (firstWeek > 0 && lastWeek == 1)
			lastWeek = 53;
		
		// Print week header
		__out.println(" * `___`: `Mo` `Tu` `We` `Th` `Fr` `Sa` `Su`");
		
		// Generate for week
		for (int atWeek = firstWeek; atWeek <= lastWeek; atWeek++)
		{
			// Get the week, if any... note that these are optional and the
			// entire month is printed to the calendar even if notes are
			// missing!
			NoteWeek week = __month.weeks.get(atWeek);
			
			// Header
			__out.printf(" * `W%02d`: ", atWeek);
			
			// Print for each day of the week
			for (int i = 1; i <= 7; i++)
			{
				// Java starts days on Monday, so account for this
				DayOfWeek weekDay = DayOfWeek.of(i);
				
				// Get the note, if there is any
				NoteDay day = (week == null ? null :
					week.weekdays.get(weekDay));
				
				// Determine the calendar date this day takes place
				LocalDate calendarDay = __month.date
					.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, atWeek)
					.with(weekDay);
				
				// Not on this month? Ignore it
				if (calendarDay.getMonthValue() != __month.month)
					__out.print("`--` ");
				
				// Print day, with possible note
				else
				{
					// Do we have a note for this day? Then yay!
					if (day != null)
						__out.printf("[**`%02d`**](%s) ",
							calendarDay.getDayOfMonth(), day.fileName);
					
					// Otherwise keep it plain
					else
						__out.printf("`%02d` ", calendarDay.getDayOfMonth());
				}
			}
			
			// End of week line
			__out.println();
		}
		
		// End padding
		__out.println();
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
		__out.printf("## %s",
			NoteCalendarGenerator.__mapAuthor(__exe, __user.userName));
		__out.println();
		
		// Spacing
		__out.println();
		
		// Generate for each year
		for (NoteYear year : __user.years.values())
			NoteCalendarGenerator.__forYear(__out, year); 
		
		// End padding
		__out.println();
	}
	
	/**
	 * Generates for the given year.
	 * 
	 * @param __out The output.
	 * @param __year The year to generate.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	private static void __forYear(PrintStream __out, NoteYear __year)
		throws NullPointerException
	{
		if (__out == null || __year == null)
			throw new NullPointerException("NARG");
		
		// Print Title
		__out.printf("### %04d", __year.year);
		__out.println();
		
		// Spacing
		__out.println();
		
		// Generate for each year
		for (NoteMonth month : __year.months.values())
			NoteCalendarGenerator.__forMonth(__out, month); 
		
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
		
		// Try to map as much as possible
		String match = __userName.replace('-', '.')
			.toLowerCase();
		
		// Read in the author map, in the format of:
		// stephanie.gawroriski Stephanie Gawroriski <xer@multiphasicapps.net>
		try (BufferedReader br = new BufferedReader(
			new InputStreamReader(new ByteArrayInputStream(
			__exe.cat(".authormap")), StandardCharsets.UTF_8)))
		{
			for (;;)
			{
				String ln = br.readLine();
				
				if (ln == null)
					break;
				
				// Find the first space
				int fs = ln.indexOf(' ');
				if (fs < 0)
					continue;
				
				// Wrong user?
				String fossilUser = ln.substring(0, fs)
					.replace("\\.", ".");
				if (!match.equalsIgnoreCase(fossilUser))
					continue;
				
				// Find where the bracket for the Git e-mail is
				int gk = ln.indexOf('<', fs);
				if (gk < 0)
					continue;
				
				// Use this one
				return ln.substring(fs + 1, gk).trim();
			}
		}
		
		// This would be bad
		catch (IOException e)
		{
			throw new RuntimeException("Could not get author map.", e);
		}
		
		// Could not find mapped user
		throw new RuntimeException("Could not get mapped user: " + __userName);
	}
}
