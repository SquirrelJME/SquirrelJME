// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package todo;

import cc.squirreljme.runtime.cldc.asm.DebugAccess;
import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import java.io.PrintStream;

/**
 * This exception is thrown.
 *
 * When constructed, this exception does not normall finish execution.
 *
 * @since 2017/02/28
 */
public class TODO
	extends Error
{
	/** Used to detect TODOs recursively being called. */
	private static volatile boolean _DOUBLE_TRIP;
	
	/** Suppress infinite note TODOs being printed over and over. */
	private static volatile boolean _notelock;
	
	/** The number of suppressed note TODOs. */
	private static volatile int _supressednotes;
	
	/**
	 * Initializes the exception, prints the trace, and exits the program.
	 *
	 * @since 2017/02/28
	 */
	public TODO()
	{
		this(null);
	}
	
	/**
	 * Initializes the exception, prints the trace, and exits the program.
	 *
	 * @param __s Message input.
	 * @since 2018/09/29
	 */
	public TODO(String __s)
	{
		// Detect TODOs tripping multiple times and fail
		boolean doubletripped = TODO._DOUBLE_TRIP;
		if (doubletripped)
			DebugAccess.fatalTodoReport(DebugAccess.rawCallTrace());
		TODO._DOUBLE_TRIP = true;
		
		// Print a starting banner, but only if the error stream exists
		PrintStream ps = System.err;
		if (ps != null)
		{
			ps.println("****************************************************");
			ps.print("INCOMPLETE CODE HAS BEEN REACHED: ");
			if (__s != null)
				ps.print(__s);
			ps.println();
			
			// Print the trace
			this.printStackTrace(ps);
			
			// Ending banner
			ps.println("****************************************************");
		}
		
		// No streams are currently available, but we would still like to
		// report the trace information to the debugger, we might not be in any
		// condition to actually do printing to the console so this will end
		// here
		else
			DebugAccess.fatalTodoReport(DebugAccess.rawCallTrace());
		
		// {@squirreljme.property
		// cc.squirreljme.notodoexit=(boolean)
		// If this is {@code true} then the ToDo exception will not tell the
		// virtual machine to exit.}
		if (!Boolean.valueOf(
			System.getProperty("cc.squirreljme.notodoexit")))
			try
			{
				System.exit(3);
			}
		
			// Ignore
			catch (SecurityException e)
			{
			}
	}
	
	/**
	 * Specifies that the integer value is missing.
	 *
	 * @return An integer, but is not returned from.
	 * @since 2017/10/27
	 */
	public static final int missingInteger()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Specifies that the object value is missing.
	 *
	 * @param <T> The object to miss.
	 * @return Should return that object, but never does.
	 * @since 2017/10/24
	 */
	public static final <T> T missingObject()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Prints a note to standard error about something that is incomplete.
	 *
	 * @param __fmt The format string.
	 * @param __args The arguments to the call.
	 * @since 2018/04/02
	 */
	public static final void note(String __fmt, Object... __args)
	{
		TODO.__note("TODO", __fmt, __args);
	}
	
	/**
	 * Formats a call trace element in a condensed form.
	 *
	 * @param __e The element to print out.
	 * @return The condensed string.
	 * @since 2018/05/03
	 */
	static final String __formatCondensedTrace(CallTraceElement __e)
		throws NullPointerException
	{
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
		if (md != null)
			sb.append(md);
		
		// Address, if it is valid
		if (ad != -1L)
		{
			sb.append( "@");
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
	}
	
	/**
	 * Formats the call trace element.
	 *
	 * @param __e The element to format.
	 * @return The formatted string.
	 * @since 2018/04/02
	 */
	static final String __formatTrace(CallTraceElement __e)
		throws NullPointerException
	{
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
			sb.append(cn);
		sb.append("::");
		if (mn != null)
			sb.append(mn);
		
		/*if (md != null)
			sb.append(md);*/
		
		// Address, if it is valid
		if (ad != -1L)
			sb.append(String.format("@0x%016X", ad));
		
		// Add file/line information if it is valid
		if (fi != null)
		{
			sb.append(" (");
			sb.append(fi);
			
			if (ln >= 0)
			{
				sb.append(':');
				sb.append(ln);
			}
			
			sb.append(')');
		}
		
		return sb.toString();
	}
	
	
	/**
	 * Prints a note to standard error about something that is incomplete.
	 *
	 * @param __fmt The format string.
	 * @param __args The arguments to the call.
	 * @since 2018/04/02
	 */
	static final void __note(String __pfx, String __fmt, Object... __args)
	{
		// Only print if the stream is valid
		PrintStream ps = System.err;
		if (ps == null)
			return;
		
		// Prevent infinite recursion when printing notes because things might
		// get stuck here if code that this method calls ends up calling
		// things
		boolean printy = false;
		synchronized (TODO.class)
		{
			// Use simple lock counter
			if (!TODO._notelock)
				try
				{
					// Lock note
					TODO._notelock = true;
					printy = true;
				}
				finally
				{
				}
			
			// Add marker for suppressed notes
			else
				TODO._supressednotes++;
		}
		
		// Print it out?
		if (printy)
			try
			{
				ps.print(__pfx);
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
	}
	
	/**
	 * Determines where the code is in the call stack.
	 *
	 * @return The call trace element for the current location.
	 * @since 2018/04/02
	 */
	static final CallTraceElement __where()
	{
		// For the SquirrelJME runtime, use the debug stuff to get the
		// current call trace
		CallTraceElement[] stack = DebugAccess.resolveRawCallTrace(
			DebugAccess.rawCallTrace());
		
		// Get the first one which is not in this class
		for (CallTraceElement e : stack)
		{
			String cn = e.className();
			if (cn == null)
				cn = "<unknown>";
			
			if (!cn.startsWith("todo/"))
				return e;
		}
		
		// Unknown
		return new CallTraceElement();
	}
}

