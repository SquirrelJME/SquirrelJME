// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang;

import java.io.OutputStream;
import java.io.PrintStream;
import java.security.Permission;
import net.multiphasicapps.squirreljme.magic.Magic;

public final class System
{
	/** Standard error stream (stderr). */
	public static final PrintStream err =
		new __CanSetPrintStream__(new PrintStream(Magic.stdErr()));
	
	/** Standard output stream (stdout). */
	public static final PrintStream out =
		new __CanSetPrintStream__(new PrintStream(Magic.stdOut()));
	
	private System()
	{
		super();
		throw new Error("TODO");
	}
	
	public static void arraycopy(Object __a, int __b, Object __c, int __d,
		int __e)
	{
		throw new Error("TODO");
	}
	
	public static long currentTimeMillis()
	{
		// Returns the current time in UTC, not local time zone.
		throw new Error("TODO");
	}
	
	public static void exit(int __a)
	{
		throw new Error("TODO");
	}
	
	public static void gc()
	{
		throw new Error("TODO");
	}
	
	public static String getProperty(String __a)
	{
		throw new Error("TODO");
	}
	
	public static String getProperty(String __a, String __b)
	{
		throw new Error("TODO");
	}
	
	public static SecurityManager getSecurityManager()
	{
		throw new Error("TODO");
	}
	
	/**
	 * This returns the identity hash code of the object. The identity hash
	 * code is randomly given by the virtual machine to an object. There is
	 * no definition on how the value is to be derived.
	 *
	 * @param __a The input object to get the hash code for.
	 * @return The hash code which was given by the virtual machine.
	 * @throws NullPointerException If no object was specified.
	 * @since 2015/11/09
	 */
	public static int identityHashCode(Object __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException();
		
		throw new Error("TODO");
	}
	
	public static long nanoTime()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Sets the new destination for standard error.
	 *
	 * Note that the {@link System#err} field is not changed, a wrapper class
	 * is used to prevent reflective abuse.
	 *
	 * @param __a The new stream to use when outputting values.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the current program lacks the given
	 * permission to set the stream.
	 * @since 2016/03/17
	 */
	public static void setErr(PrintStream __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException();
		
		// Not allowed to do this?
		getSecurityManager().checkPermission(new RuntimePermission("setIO"));
		
		// Use a wrapped class to prevent final abuse.
		((__CanSetPrintStream__)err).__set(__a);
	}
	
	/**
	 * Sets the new destination for standard output.
	 *
	 * Note that the {@link System#out} field is not changed, a wrapper class
	 * is used to prevent reflective abuse.
	 *
	 * @param __a The new stream to use when outputting values.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the current program lacks the given
	 * permission to set the stream.
	 * @since 2016/03/17
	 */
	public static void setOut(PrintStream __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException();
		
		// Not allowed to do this?
		getSecurityManager().checkPermission(new RuntimePermission("setIO"));
		
		// Use a wrapped class to prevent final abuse.
		((__CanSetPrintStream__)out).__set(__a);
	}
}

