// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package todo;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.CallStackItem;
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.runtime.cldc.Poking;
import cc.squirreljme.runtime.cldc.debug.CallTraceElement;

/**
 * Utilities for printing and debugging.
 *
 * @since 2020/03/14
 */
@SuppressWarnings("TodoComment")
final class __Utilities__
{
	/** Used to detect OOPSs/TODOs recursively being called. */
	@SuppressWarnings("StaticVariableMayNotBeInitialized")
	static volatile boolean _DOUBLE_TRIP;
	
	/** Stop formatted text from infinitely printing. */
	@SuppressWarnings("StaticVariableMayNotBeInitialized")
	static volatile boolean _FORMAT_LOCK;
	
	/** Squelch debugging? */
	static final boolean _SQUELCH;
	
	/** The width of the star line. */
	static final int _STAR_LINE_WIDTH =
		52;
	
	/**
	 * Not used.
	 *
	 * @since 2020/03/14
	 */
	private __Utilities__()
	{
	}
	
	static
	{
		// Poke to make sure the required VM stuff is working
		Poking.poke();
		
		int flags = Assembly.sysCallPV(SystemCallIndex.DEBUG_FLAGS);
		
		// Setup flags
		_SQUELCH = ((flags & Constants.DEBUG_SQUELCH_PRINT) != 0);
	}
	
	/**
	 * Prints formatted string.
	 *
	 * @param __a Letter A.
	 * @param __b Letter B.
	 * @param __fmt The format.
	 * @since 2020/03/15
	 */
	@SuppressWarnings("StaticVariableUsedBeforeInitialization")
	public static void dumpFormatLine(char __a, char __b, String __fmt,
		Object... __args)
	{
		// Prevent formatting from printing itself again
		if (__Utilities__._FORMAT_LOCK)
			return;
		__Utilities__._FORMAT_LOCK = true;
		
		// Now print
		try
		{
			// Print prefix
			__Utilities__.printChar(__a);
			__Utilities__.printChar(__b);
			__Utilities__.printChar(' ');
			__Utilities__.printChar('-');
			__Utilities__.printChar('-');
			__Utilities__.printChar(' ');
			
			// End line
			__Utilities__.printChar('\n');
			
			/*
				ps.print(__pfx);
				ps.print(GuestDepth.guestDepth());
				ps.print(' ');
				ps.print(TODO.__formatCondensedTrace(TODO.__where()));
				ps.print(" -- ");
				
				ps.printf(__fmt, __args);
				
				// Add markers to indicate the number of notes which were
				// suppressed
				int suppressed = TODO._supressednotes;
				TODO._supressednotes = 0;
				for (int i = 0; i < suppressed; i++)
					ps.print('^');
				
				ps.println();
			}
			finally
			{
				// In case of exceptions, this will always be unlocked
				TODO._notelock = false;
			}
			 */
		}
		
		// Allow formatted strings to be printed again
		finally
		{
			__Utilities__._FORMAT_LOCK = false;
		}
	}
	
	/**
	 * Dumps the trace to the output using the normal trace logic.
	 *
	 * @param __a Letter A.
	 * @param __b Letter B.
	 * @param __trace The trace to print for.
	 * @since 2020/03/15
	 */
	@SuppressWarnings("StaticVariableUsedBeforeInitialization")
	public static void dumpTrace(char __a, char __b, int[] __trace,
		Object[] __args)
	{
		// Prevent double-tripping of trace handling in the event one is
		// tripped
		if (__Utilities__._DOUBLE_TRIP)
			return;
		__Utilities__._DOUBLE_TRIP = true;
		
		// Dump the trace
		try
		{
			// Starting banner
			__Utilities__.printStarLine(__a, __b);
			
			// Dump the trace
			__Utilities__.printStackTrace(__a, __b, __trace);
			
			// Ending banner
			__Utilities__.printStarLine(__a, __b);
		}
		
		// Reset the tripping state
		finally
		{
			__Utilities__._DOUBLE_TRIP = false;
		}
	}
	
	/**
	 * Prints the given character to the debug output.
	 *
	 * @param __c The output character.
	 * @since 2020/03/15
	 */
	public static void printChar(char __c)
	{
		// Do not print if not requested to
		if (__Utilities__._SQUELCH)
			return;
		
		Assembly.sysCallPV(SystemCallIndex.PD_WRITE_BYTE,
			Assembly.sysCallPV(SystemCallIndex.PD_OF_STDERR, __c));
	}
	
	/**
	 * Prints code to the output.
	 *
	 * @param __a Character A.
	 * @param __b Character B.
	 * @since 2020/03/15
	 */
	public static void printCode(char __a, char __b)
	{
		__Utilities__.printChar(__a);
		__Utilities__.printChar(__b);
		
		__Utilities__.printChar('\n');
	}
	
	/**
	 * Prints a line of asterisks to the output, used for spacing.
	 *
	 * @param __a Letter A.
	 * @param __b Letter B.
	 * @since 2020/03/15
	 */
	public static void printStarLine(char __a, char __b)
	{
		__Utilities__.printChar(__a);
		__Utilities__.printChar(__b);
		__Utilities__.printChar(' ');
		
		for (int i = 0; i < __Utilities__._STAR_LINE_WIDTH; i++)
			__Utilities__.printChar('*');
	}
	
	/**
	 * Prints the current stack trace.
	 *
	 * @param __a Letter A.
	 * @param __b Letter B.
	 * @since 2020/03/15
	 */
	public static void printStackTrace(char __a, char __b)
	{
		__Utilities__.printStackTrace(__a, __b, CallTraceElement.traceRaw());
	}
	
	/**
	 * Prints the current stack trace.
	 *
	 * @param __a Letter A.
	 * @param __b Letter B.
	 * @param __raw The raw stack trace.
	 * @since 2020/03/15
	 */
	public static void printStackTrace(char __a, char __b, int[] __raw)
	{
		// Code signifying the start of a trace
		__Utilities__.printCode(__a, '<');
		
		// Print every stack trace portion
		for (int base = 0, total = __raw.length; base < total;
			base += CallStackItem.NUM_ITEMS)
		{
			// Print starting code
			__Utilities__.printChar(__a);
			__Utilities__.printChar(__b);
			
			// Spacer for organization
			__Utilities__.printChar(' ');
		}
		
		// Code for ending the trace
		__Utilities__.printCode(__a, '>');
		
		/*
		if (__e == null)
			throw new NullPointerException("NARG");
		
		String cn = __e.className(),
			mn = __e.methodName(),
			md = __e.methodDescriptor(),
			fi = __e.file();
		long ad = __e.address();
		int ln = __e.line();
		
		StringBuilder sb = new StringBuilder();
		
		// Location
		if (cn != null)
		{
			// Get identifier part
			int ld = cn.lastIndexOf('.');
			if (ld < 0)
				if ((ld = cn.lastIndexOf('/')) < 0)
					ld = 0;
			
			// Print slimmed down packages since they could be guessed
			for (int i = 0, n = cn.length(); i >= 0 && i < n;)
			{
				// Before the last dot, print single and skip ahead
				if (i < ld)
				{
					sb.append(cn.charAt(i));
					
					int ldi = cn.indexOf('.', i);
					if (ldi < 0)
						ldi = cn.indexOf('/', i);
					
					i = ldi + 1;
				}
				
				// Finish string
				else
				{
					if (i > 0)
						sb.append('.');
					sb.append(cn.substring(i));
					break;
				}
			}
		}
		sb.append("::");
		if (mn != null)
			sb.append(mn);
		
		// Address, if it is valid
		if (ad != -1L)
		{
			sb.append(" @ ");
			sb.append(Long.toString(ad, 16).toUpperCase());
			sb.append("h");
		}
		
		// Add file/line information if it is valid
		if (fi != null)
		{
			sb.append(" (");
			
			// Use blank class name if not specified
			if (cn == null)
				cn = "";
			
			// The class will end with the Java extension
			int ld = cn.lastIndexOf('.');
			if (ld < 0)
				ld = cn.lastIndexOf('/');
			if (ld >= 0)
				cn = cn.substring(ld + 1);
			
			// Determine how many charcters the class name and the file
			// name have in common, so that it can be shortened
			int deep = 0;
			for (int n = Math.min(cn.length(), fi.length()); deep < n; deep++)
				if (cn.charAt(deep) != fi.charAt(deep))
					break;
			
			// Use whole name
			if (deep == 0)
				sb.append(fi);
			
			// Use fragment
			else
			{
				// But only if it does not end in .java, this makes it
				// implied since it is always around
				String sub = fi.substring(deep);
				if (!sub.equals(".java"))
				{
					sb.append('*');
					sb.append(sub);
				}
			}
			
			if (ln >= 0)
			{
				sb.append(':');
				sb.append(ln);
			}
			
			sb.append(')');
		}
		
		return sb.toString();
		
		--------------------- BACKUP METHOD
		
		// Print this and any causes!
		for (Throwable rover = this; rover != null; rover = rover._cause)
		{
			// Supervisor or caused by?
			todo.DEBUG.code('T', (rover == this ? 'S' : 'C'),
				Assembly.objectToPointer(rover));
			
			// The thrown type
			todo.DEBUG.code('T', 'Y', Assembly.memReadInt(
				Assembly.objectToPointer(this),
				Constants.OBJECT_CLASS_OFFSET));
			
			// Obtain the raw trace that was captured on construction
			int[] rawtrace = this._rawtrace;
			int rawn = rawtrace.length;
			
			// Print all the items in it
			for (int base = 0; base < rawn; base += CallStackItem.NUM_ITEMS)
				try
				{
					// Indicate start of element
					todo.DEBUG.code('T', '-');
					
					// Print out the raw details
					todo.DEBUG.codeUtf('T', 'c',
						rawtrace[base + CallStackItem.CLASS_NAME]);
					todo.DEBUG.codeUtf('T', 'n',
						rawtrace[base + CallStackItem.METHOD_NAME]);
					todo.DEBUG.codeUtf('T', 'y',
						rawtrace[base + CallStackItem.METHOD_TYPE]);
					todo.DEBUG.codeUtf('T', '#',
						rawtrace[base + CallStackItem.TASK_ID]);
					todo.DEBUG.codeUtf('T', 'f',
						rawtrace[base + CallStackItem.SOURCE_FILE]);
					todo.DEBUG.code('T', 'l',
						rawtrace[base + CallStackItem.SOURCE_LINE]);
					todo.DEBUG.code('T', 'a',
						rawtrace[base + CallStackItem.PC_ADDRESS]);
					todo.DEBUG.code('T', 'j',
						rawtrace[base + CallStackItem.JAVA_PC_ADDRESS]);
					todo.DEBUG.code('T', 'o',
						rawtrace[base + CallStackItem.JAVA_OPERATION]);
				}
				
				// Error getting stack trace element
				catch (Throwable t)
				{
					todo.DEBUG.code('X', 'T', base);
				}
			
			// Indicate end of current set
			todo.DEBUG.code('T', '<');
		}
		
		// Indicate end of trace log
		todo.DEBUG.code('T', '_');
		 */
	}
}
