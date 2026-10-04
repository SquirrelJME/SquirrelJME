// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.LineEndingType;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Utilities for line endings.
 *
 * @see LineEndingType
 * @since 2020/06/11
 */
public final class LineEndingUtils
{
	/**
	 * Not used.
	 *
	 * @since 2020/06/11
	 */
	private LineEndingUtils()
	{
	}
	
	/**
	 * Appends the default line ending to the given output.
	 *
	 * @param __out Where to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/11
	 */
	public static void append(Appendable __out)
		throws IOException, NullPointerException
	{
		LineEndingUtils.append(__out, RuntimeShelf.lineEnding());
	}
	
	/**
	 * Appends the line ending to the given output.
	 *
	 * @param __out Where to write to.
	 * @param __type The {@link LineEndingType}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/11
	 */
	public static void append(Appendable __out, int __type)
		throws IOException, NullPointerException
	{
		for (int i = 0;; i++)
		{
			char c = LineEndingUtils.toChar(__type, i);
			if (c == 0)
				return;
			
			__out.append(c);
		}
	}
	
	/**
	 * Appends the default end of line, but wraps the exception so that it is
	 * not thrown and instead returns a boolean if one was generated.
	 *
	 * @param __out The appendable to write to.
	 * @return If an {@link IOException} was thrown.
	 * @since 2020/06/11
	 */
	public static boolean appendWrap(Appendable __out)
		throws NullPointerException
	{
		return LineEndingUtils.appendWrap(__out, RuntimeShelf.lineEnding());
	}
	
	/**
	 * Appends the end of line, but wraps the exception so that it is not
	 * thrown and instead returns a boolean if one was generated.
	 *
	 * @param __out The appendable to write to.
	 * @param __type The {@link LineEndingType}.
	 * @return If an {@link IOException} was thrown.
	 * @since 2020/06/11
	 */
	public static boolean appendWrap(Appendable __out, int __type)
		throws NullPointerException
	{
		try
		{
			LineEndingUtils.append(__out, __type);
			return false;
		}
		catch (IOException ignored)
		{
			return true;
		}
	}
	
	/**
	 * Returns the character sequence used for the line ending.
	 *
	 * @param __type The {@link LineEndingType}.
	 * @param __index The character index to return.
	 * @return The resultant character or {@code 0} if none are left.
	 * @throws IllegalArgumentException If the line ending type is not valid.
	 * @since 2020/06/11
	 */
	public static char toChar(int __type, int __index)
		throws IllegalArgumentException
	{
		// No sequence is ever within this range
		if (__index < 0 || __index > 1)
			return 0;
		
		switch (__type)
		{
			case LineEndingType.CR:
				return (__index == 0 ? '\r' : 0);
			
			case LineEndingType.LF:
				return (__index == 0 ? '\n' : 0);
				
			case LineEndingType.CRLF:
				return (__index == 0 ? '\r' : '\n');
			
			/* {@squirreljme.error ZZ03 Unknown line ending type. (Type)} */
			default:
				throw new IllegalArgumentException("ZZ03 " + __type);
		}
	}
	
	/**
	 * Returns the string for the given line ending sequence.
	 *
	 * @param __type The {@link LineEndingType}.
	 * @return The string representing the end of line sequence.
	 * @throws IllegalArgumentException If the line ending type is not valid.
	 * @since 2020/06/11
	 */
	public static String toString(int __type)
		throws IllegalArgumentException
	{
		switch (__type)
		{
			case LineEndingType.CR:		return "\r";
			case LineEndingType.LF:		return "\n";
			case LineEndingType.CRLF:	return "\r\n";
			
			/* {@squirreljme.error ZZ29 Unknown line ending type. (Type)} */
			default:
				throw new IllegalArgumentException("ZZ29 " + __type);
		}
	}
	
	/**
	 * Converts the string to the given type.
	 *
	 * @param __string The string to convert.
	 * @return The type from the string.
	 * @throws IllegalArgumentException If the string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/13
	 */
	public static int toType(String __string)
		throws IllegalArgumentException, NullPointerException
	{
		if (__string == null)
			throw new NullPointerException("NARG");
		
		switch (__string)
		{
			case "\r":		return LineEndingType.CR;
			case "\n":		return LineEndingType.LF;
			case "\r\n":	return LineEndingType.CRLF;
			
				/* {@squirreljme.error ZZ3w Unknown line ending string.} */
			default:
				throw new IllegalArgumentException("ZZ3w " + __string);
		}
	}
	
	/**
	 * Writes the default line ending to the given output.
	 *
	 * @param __out Where to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/11
	 */
	public static void write(OutputStream __out)
		throws IOException, NullPointerException
	{
		LineEndingUtils.write(__out, RuntimeShelf.lineEnding());
	}
	
	/**
	 * Writes the line ending to the given output.
	 *
	 * @param __out Where to write to.
	 * @param __type The {@link LineEndingType}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/11
	 */
	public static void write(OutputStream __out, int __type)
		throws IOException, NullPointerException
	{
		for (int i = 0;; i++)
		{
			char c = LineEndingUtils.toChar(__type, i);
			if (c == 0)
				return;
			
			__out.write(c);
		}
	}
	
	/**
	 * Writes the default line ending to the given output.
	 *
	 * @param __out Where to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/11
	 */
	public static void write(Writer __out)
		throws IOException, NullPointerException
	{
		LineEndingUtils.write(__out, RuntimeShelf.lineEnding());
	}
	
	/**
	 * Writes the line ending to the given output.
	 *
	 * @param __out Where to write to.
	 * @param __type The {@link LineEndingType}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/11
	 */
	public static void write(Writer __out, int __type)
		throws IOException, NullPointerException
	{
		for (int i = 0;; i++)
		{
			char c = LineEndingUtils.toChar(__type, i);
			if (c == 0)
				return;
			
			__out.write(c);
		}
	}
}
