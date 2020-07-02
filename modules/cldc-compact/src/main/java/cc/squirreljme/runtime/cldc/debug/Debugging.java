// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.debug;

import cc.squirreljme.jvm.mle.DebugShelf;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.TerminalShelf;
import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.brackets.TracePointBracket;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.runtime.cldc.io.ConsoleOutputStream;
import cc.squirreljme.runtime.cldc.lang.LineEndingUtils;
import java.io.PrintStream;
import java.util.Objects;
import todo.OOPS;

/**
 * This class contains all of the static methods which are for writing debug
 * output and failing accordingly on incomplete code. All the methods here for
 * the most part do not touch the standard {@link System#err} and
 * {@link System#out} (which use {@link PrintStream}), as in the event those
 * have a bug or otherwise issues can occur. The methods here should be
 * reliable enough on their own to convey a message accordingly.
 *
 * @since 2020/03/21
 */
public final class Debugging
{
	/** Only bytes up to this value are permitted in the output. */
	private static final int _BYTE_LIMIT =
		0x7E;
	
	/** Exit status for TODOs. */
	private static final int _TODO_EXIT_STATUS =
		63;
	
	/** Used to prevent loops. */
	@SuppressWarnings("StaticVariableMayNotBeInitialized")
	private static volatile boolean _noLoop;
	
	/** This will be set when TODOs are tripped, to prevent infinite loops. */
	@SuppressWarnings("StaticVariableMayNotBeInitialized")
	private static volatile boolean _tripped;
	
	/**
	 * Not used.
	 *
	 * @since 2020/03/21
	 */
	private Debugging()
	{
	}
	
	/**
	 * Emits a debugging note.
	 *
	 * @param __fmt The format.
	 * @since 2020/05/13
	 */
	public static void debugNote(String __fmt)
	{
		Debugging.__format('D', 'B', __fmt, (Object[])null);
	}
	
	/**
	 * Emits a debugging note.
	 *
	 * @param __fmt The format.
	 * @param __args The arguments to the string.
	 * @since 2020/03/27
	 */
	public static void debugNote(String __fmt, Object... __args)
	{
		Debugging.__format('D', 'B', __fmt, __args);
	}
	
	/**
	 * Emits an oops error.
	 *
	 * @param __args Argument to the error.
	 * @return The generated error.
	 * @since 2020/03/22
	 */
	public static Error oops(Object... __args)
	{
		return new OOPS();
	}
	
	/**
	 * Emits a To-Do error.
	 *
	 * @return The generated error.
	 * @since 2020/03/21
	 */
	public static Error todo()
	{
		return Debugging.todo((Object[])null);
	}
	
	/**
	 * Emits a To-Do error.
	 *
	 * @param __args Arguments to the error.
	 * @return The generated error.
	 * @since 2020/03/21
	 */
	@SuppressWarnings("StaticVariableUsedBeforeInitialization")
	public static Error todo(Object... __args)
	{
		// Only trip this once! In the event this trips twice, just shortcut
		// with an exception otherwise
		if (Debugging._tripped)
			return new Error("Recursive TODO");
		Debugging._tripped = true;
		
		// This try is here so that in event this fails or throws another
		// exception, we always terminal no matter what
		try
		{
			// Print a very visible banner to not hide this information
			Debugging.todoNote(
				"*****************************************");
			Debugging.todoNote("INCOMPLETE CODE HAS BEEN REACHED: ");
			
			// Print the stack trace first like this so it does not possibly
			// get trashed
			TracePointBracket[] trace = DebugShelf.traceStack();
			CallTraceUtils.printStackTrace(
				new ConsoleOutputStream(StandardPipeType.STDERR),
				"INCOMPLETE CODE", trace,
				null, null, 0);
				
			// Report the To-Do trace so it is known to another program
			ThreadShelf.setTrace("INCOMPLETE CODE", trace);
			
			// Print all arguments passed afterwards, just in case
			if (__args != null)
			{
				Debugging.todoNote(
					"-----------------------------------------");
				
				int n = __args.length;
				for (int i = 0; i < n; i++)
					try
					{
						Debugging.todoNote("%d: %s", i, __args[i]);
					}
					catch (Throwable ignored)
					{
						// Just drop everything here, so we can try to print
						// as much as we can
					}
			}
			
			Debugging.todoNote(
				"*****************************************");
		}
		
		// Always try to exit at the end of the call, in the event another
		// exception is thrown
		finally
		{
			// Just exit directly so there is no way to continue, if we can
			try
			{
				System.exit(Debugging._TODO_EXIT_STATUS);
			}
			catch (SecurityException ignored)
			{
				// However just ignore this case if we cannot truly exit here
			}
		}
		
		// Throw normal error here
		throw new Error("TODO");
	}
	
	/**
	 * Emits a To-Do note.
	 *
	 * @param __fmt Format string.
	 * @since 2020/05/13
	 */
	public static void todoNote(String __fmt)
	{
		Debugging.__format('T', 'D', __fmt, (Object[])null);
	}
	
	/**
	 * Emits a To-Do note.
	 *
	 * @param __fmt Format string.
	 * @param __args Arguments.
	 * @since 2020/03/31
	 */
	public static void todoNote(String __fmt, Object... __args)
	{
		Debugging.__format('T', 'D', __fmt, __args);
	}
	
	/**
	 * Returns a To-Do for an object.
	 *
	 * @param <T> The type.
	 * @param __args The calling arguments.
	 * @return Never returns.
	 * @since 2020/04/09
	 */
	public static <T> T todoObject(Object... __args)
	{
		throw Debugging.todo(__args);
	}
	
	/**
	 * Prints formatted text to the console output.
	 *
	 * @param __cha First grouping character.
	 * @param __chb Second grouping character.
	 * @param __format Format string.
	 * @param __args Arguments.
	 * @since 2020/05/07
	 */
	@SuppressWarnings({"StaticVariableUsedBeforeInitialization"})
	private static void __format(char __cha, char __chb, String __format,
		Object... __args)
	{
		// Print quickly and stop because this may infinite loop
		if (Debugging._noLoop)
		{
			Debugging.__print('X');
			return;
		}
		
		// Print otherwise
		try
		{
			// Do not re-enter this loop
			Debugging._noLoop = true;
			
			// Print header marker
			Debugging.__print(__cha, __chb);
			Debugging.__print(':', ' ');
			
			// The specifier to print along with the field index
			boolean specifier = false,
				hasArgIndex = false,
				firstChar = false;
			int argIndex = 0,
				baseArg = 0,
				width = -1,
				argCount = (__args == null ? 0 : __args.length);
			
			// Print down the format
			for (int i = 0, n = __format.length(); i < n; i++)
			{
				char c = __format.charAt(i);
				
				// Printing a specifier
				if (specifier)
				{
					// Ignore flags
					if (c == '-' || c == '#' || c == '+' ||
						c == ' ' || c == ',' || c == '(' ||
						(firstChar && c == '0'))
						continue;
					
					// Ignore precision
					else if (c == '.')
						continue;
					
					// Could be width or argument index position
					else if ((c >= '1' && c <= '9') ||
						(!firstChar && c == '0'))
					{
						if (hasArgIndex)
						{
							if (width < 0)
								width = 0;
							width = (width * 10) + (c - '0');
						}
						else
							argIndex = (argIndex * 10) + (c - '0');
					}
					
					// Is argument index
					else if (!hasArgIndex && c == '$')
						hasArgIndex = true;
					
					// Percent or newline
					else if (c == '%' || c == 'n')
					{
						if (c == '%')
							Debugging.__print('%');
						else
							Debugging.__printLine();
						
						// Stop
						specifier = false;
					}
					
					// A type to be printed, probably
					else
					{
						// If no argument index was flagged, then this is
						// likely the width
						if (!hasArgIndex && argIndex > 0)
							width = argIndex;
						
						// Select argument to print
						int choice = (hasArgIndex ? argIndex : baseArg++);
						Object value = (choice < 0 || choice >= argCount ?
							null : __args[choice]);
						
						// Print its value
						if (value == null)
						{
							Debugging.__print('n', 'u');
							Debugging.__print('l', 'l');
						}
						
						// Assume a string
						else
						{
							String string = value.toString();
							
							// Print left padding?
							int strLen = string.length(),
								pad = width - strLen;
							while ((pad--) > 0)
								Debugging.__print(' ');
							
							// Print actual string
							for (int j = 0; j < strLen; j++)
								Debugging.__print(string.charAt(j));
						}
						
						// Stop
						specifier = false;
					}
					
					// No longer will be the first character
					if (firstChar)
						firstChar = false;
				}
				
				// Format specifier?
				else if (c == '%')
				{
					specifier = true;
					firstChar = true;
					argIndex = 0;
				}
				
				// Plain character?
				else
					Debugging.__print(c);
			}
			
			// End of line
			Debugging.__printLine();
		}
		
		// Hopefully this does not happen but just in case, we want to catch
		// any exceptions that are tossed while printing format strings
		catch (Throwable t)
		{
			// Indicate this has occurred
			Debugging.__print('X');
			Debugging.__print('X');
			
			// End of line
			Debugging.__printLine();
		}
		
		// Clear loop prevention flag
		finally
		{
			Debugging._noLoop = false;
		}
	}
	
	/**
	 * Prints the given character.
	 *
	 * @param __c The character to print.
	 * @since 2020/05/07
	 */
	@SuppressWarnings({"SameParameterValue"})
	private static void __print(char __c)
	{
		Debugging.__print(__c, '\0');
	}
	
	/**
	 * Prints the given characters.
	 *
	 * @param __c The character to print.
	 * @param __d Second character to print.
	 * @since 2020/05/07
	 */
	@SuppressWarnings("FeatureEnvy")
	private static void __print(char __c, char __d)
	{
		TerminalShelf.write(StandardPipeType.STDERR,
			(__c > Debugging._BYTE_LIMIT ? '?' : __c));
		
		if (__d > 0)
			TerminalShelf.write(StandardPipeType.STDERR,
				(__d > Debugging._BYTE_LIMIT ? '?' : __d));
	}
	
	/**
	 * Prints end of line.
	 *
	 * @since 2020/05/07
	 */
	private static void __printLine()
	{
		int lineType = RuntimeShelf.lineEnding();
		for (int i = 0;; i++)
		{
			char c = LineEndingUtils.toChar(lineType, i);
			
			if (c == 0)
				break;
			
			Debugging.__print(c, '\0');
		}
	}
}
