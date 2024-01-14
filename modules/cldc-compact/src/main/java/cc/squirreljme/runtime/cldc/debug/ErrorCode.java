// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.debug;

import cc.squirreljme.jvm.mle.DebugShelf;
import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.brackets.TracePointBracket;
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import java.io.IOException;
import java.io.InputStream;
import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.Range;

/**
 * Error code generation and handling.
 *
 * @since 2023/07/19
 */
public final class ErrorCode
{
	/** The prefix for properties. */
	public static final String PREFIX_PROPERTY =
		"X-SquirrelJME-PrefixCode";
	
	/**
	 * Not used.
	 *
	 * @since 2023/07/19
	 */
	private ErrorCode()
	{
	}
	
	/**
	 * Formats an error code.
	 *
	 * @param __format The format to use.
	 * @param __args The arguments to use for the format.
	 * @return The resultant string.
	 * @deprecated This is used for compatibility in migrating codes over
	 * accordingly.
	 * @since 2023/07/19
	 */
	@SuppressWarnings({"NewMethodNamingConvention", 
		"squirreljme_qualifiedError"})
	@Deprecated
	public static String __error__(@PrintFormat String __format,
		Object... __args)
	{
		// Determine the actual sub-ID code
		int subId = 0;
		if (__format.length() >= 4)
		{
			char a = __format.charAt(2);
			char b = __format.charAt(2);
			
			subId = (Character.digit(a, Character.MAX_RADIX) *
				Character.MAX_RADIX) + Character.digit(b, Character.MAX_RADIX);
		}
		
		// Forward call
		return ErrorCode.__error__(subId, __args);
	}
	
	/**
	 * Formats an error code.
	 *
	 * @param __idCode The error ID code.
	 * @return The formatted string.
	 * @since 2023/07/19
	 */
	@SuppressWarnings({"NewMethodNamingConvention", 
		"squirreljme_qualifiedError"})
	public static String __error__(@Range(from = -1, to = 1296) int __idCode)
	{
		return ErrorCode.__error__(__idCode, (Object[])null);
	}
	
	/**
	 * Formats an error code.
	 *
	 * @param __idCode The error ID code.
	 * @param __args Arguments to the error.
	 * @return The formatted string.
	 * @since 2023/07/19
	 */
	@SuppressWarnings("NewMethodNamingConvention")
	public static String __error__(@Range(from = -1, to = 1296) int __idCode,
		Object... __args)
	{
		// Determine the error prefix of our code
		String __prefix = "XX";
		
		// Determine the prefix that should be used
		TypeBracket throwingClass = ErrorCode.__throwingClass();
		if (throwingClass != null)
			__prefix = ErrorCode.__locatePrefix(
				ErrorCode.__unComponent(throwingClass));
		
		// Format the error code accordingly
		int n = (__args == null ? 0 : __args.length);
		return String.format("%s%04d%s", __prefix, __idCode,
			(n == 0 ? "" : ": ")) +
			String.format(ErrorCode.__argFormat(n), __args);
	}
	
	/**
	 * Returns the argument format specifiers to use for the given array
	 * length.
	 *
	 * @param __len The length of the array.
	 * @return The argument format to use for representation.
	 * @since 2023/07/19
	 */
	private static String __argFormat(int __len)
	{
		switch (__len)
		{
			case 0: return "";
			case 1: return "%s";
			case 2: return "%s %s";
			case 3: return "%s %s %s";
			case 4: return "%s %s %s %s";
			case 5: return "%s %s %s %s %s";
			case 6: return "%s %s %s %s %s %s";
			case 7: return "%s %s %s %s %s %s %s";
			case 8: return "%s %s %s %s %s %s %s %s";
			case 9: return "%s %s %s %s %s %s %s %s %s";
			case 10: return "%s %s %s %s %s %s %s %s %s %s";
			case 11: return "%s %s %s %s %s %s %s %s %s %s %s";
			case 12: return "%s %s %s %s %s %s %s %s %s %s %s %s";
		}
		
		// Gosh... Way too many arguments, so add them all ourselves
		StringBuilder result = new StringBuilder((3 * __len) - 1);
		for (int i = 0; i < __len; i++)
			if (i == 0)
				result.append("%s");
			else
				result.append(" %s");
		
		return result.toString();
	}
	
	/**
	 * Returns the prefix for the throwing class.
	 *
	 * @param __throwingClass The throwing class prefix.
	 * @return The prefix for the throwing class.
	 * @since 2023/07/19
	 */
	private static String __locatePrefix(TypeBracket __throwingClass)
	{
		// Ignore here
		if (__throwingClass == null)
			return "?N";
		
		// Is this in any kind of JAR?
		JarPackageBracket inJar = TypeShelf.inJar(__throwingClass);
		if (inJar == null)
			return "?J";
		
		// See if there is a numerical prefix code defined
		int prefixCode = JarPackageShelf.prefixCode(inJar);
		if (prefixCode < 0)
			return "?P";
		
		// Build prefix code
		StringBuilder sb = new StringBuilder(2);
		sb.append(Character.toUpperCase(Character.forDigit(
			prefixCode / Character.MAX_RADIX, Character.MAX_RADIX)));
		sb.append(Character.toUpperCase(Character.forDigit(
			prefixCode % Character.MAX_RADIX, Character.MAX_RADIX)));
		return sb.toString();
	}
	
	/**
	 * Returns the class throwing this.
	 *
	 * @return The class throwing this.
	 * @since 2023/07/19
	 */
	private static TypeBracket __throwingClass()
	{
		// Get the stack trace
		TracePointBracket[] trace = DebugShelf.traceStack();
		
		// Find out who called us
		String selfClassRt = ErrorCode.class.getName();
		String selfClassBn = TypeShelf.binaryName(
			TypeShelf.classToType(ErrorCode.class));
		for (int i = 0, n = trace.length; i < n; i++)
		{
			TracePointBracket point = trace[i];
			
			// Ignore unknown classes
			String atClass = DebugShelf.pointClass(point);
			if (atClass == null)
				continue;
			
			// Ignore ourself
			if (selfClassRt.equals(atClass) || selfClassBn.equals(atClass))
				continue;
			
			// Resolve class
			TypeBracket found = TypeShelf.findType(atClass);
			if (found != null)
				return found;
		}
		
		// Not found ultimately
		return null;
	}
	
	/**
	 * Returns the root component of a type.
	 *
	 * @param __in The input class.
	 * @return The root component.
	 * @since 2023/07/19
	 */
	private static TypeBracket __unComponent(TypeBracket __in)
	{
		if (__in == null)
			return null;
		
		if (TypeShelf.isArray(__in))
			return TypeShelf.componentRoot(__in);
		return __in;
	}
}
