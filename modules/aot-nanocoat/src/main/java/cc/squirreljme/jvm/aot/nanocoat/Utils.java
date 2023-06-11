// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Identifier;
import net.multiphasicapps.classfile.Method;

/**
 * General utilities.
 *
 * @since 2023/05/28
 */
public final class Utils
{
	/**
	 * Not used.
	 * 
	 * @since 2023/05/28
	 */
	private Utils()
	{
	}
	
	/**
	 * Determines a DOS compatible file name.
	 * 
	 * @param __in The input name.
	 * @return The DOS compatible file name.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public static final String dosFileName(String __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Extract extension if there is one
		int lastDot = __in.lastIndexOf('.');
		String ext = (lastDot >= 0 ?
			"." + __in.substring(lastDot + 1) : "");
		
		// Determine actual name
		StringBuilder base = new StringBuilder();
		for (int i = 0, n = __in.length(); i < n; i++)
		{
			char c = __in.charAt(i);
			
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||
				(c >= '0' && c <= '9') || c == '_')
				base.append(c);
			else
				base.append('_');
		}
		
		// Long file name?
		if (base.length() > 8)
		{
			// Determine the hash of the name
			String temp = base.toString().toLowerCase();
			int hash = temp.hashCode();
			if (hash < 0)
				hash = -hash;
			
			// Convert to a max radix number which will have part of the name
			String code = Long.toString(hash & 0xFFFFFFFFL,
				Character.MAX_RADIX);
			
			// This will be [1, 7] characters, so clear out to fit the code
			// with 8 characters
			int codeLen = code.length();
			int baseLen = 8 - codeLen;
			base.delete(baseLen, base.length());
			
			// Append the code, which is the compactified hashcode
			base.append(code);
		}
		
		// Build name
		return (base + ext).toLowerCase();
	}
	
	/**
	 * Mangles the given input.
	 * 
	 * @param __in The input to mangle.
	 * @return The mangled output.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public static final String mangle(BinaryName __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Mangles the given input.
	 * 
	 * @param __in The input to mangle.
	 * @return The mangled output.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public static final String mangle(ClassName __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		return Utils.mangle(__in.toString());
	}
	
	/**
	 * Mangles the given input.
	 * 
	 * @param __in The input to mangle.
	 * @return The mangled output.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public static final String mangle(Identifier __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		return Utils.mangle(__in.toString());
	}
	
	/**
	 * Mangles the given input.
	 * 
	 * @param __in The input to mangle.
	 * @return The mangled output.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public static final String mangle(String __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		StringBuilder result = new StringBuilder();
		char lastChar = 0;
		for (int i = 0, n = __in.length(); i < n; i++)
		{
			char c = __in.charAt(i);
			
			// Simple mangles (same as JNI)
			if (c == '_')
				result.append("_1");
			else if (c == ';')
				result.append("_2");
			else if (c == '[')
				result.append("_3");
			else if (c == '/')
				result.append("_");
			
			// Copy over, for numbers we cannot demangle if there was a _
			// because otherwise there could be a collision, also numbers
			// cannot be first but if they ever are we escape them
			else if ((c >= 'a' && c <= 'z') ||
				(c >= 'A' && c <= 'Z') ||
				((c >= '0' && c <= '9') && i > 0 && lastChar != '/'))
				result.append(c);
			
			// Unicode mangle
			else
			{
				result.append("_0");
				result.append(String.format("%04x", c & 0xFFFF));
			}
			
			// Used for some number sequences
			lastChar = c;
		}
		
		return result.toString();
	}
	
	/**
	 * Determines the mangled symbol name for a class.
	 * 
	 * @param __module The module used.
	 * @param __in The input class name.
	 * @return The mangled symbol name for the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public static String symbolClassName(NanoCoatLinkGlob __module,
		ClassName __in)
		throws NullPointerException
	{
		if (__module == null || __in == null)
			throw new NullPointerException("NARG");
		
		return "sjmeCl__" + __module.baseName + "__" + Utils.mangle(__in);
	}
	
	/**
	 * Returns the symbol for the resource path.
	 * 
	 * @param __module The input module.
	 * @param __in The input.
	 * @return The mangled symbol.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public static String symbolResourcePath(NanoCoatLinkGlob __module,
		String __in)
		throws NullPointerException
	{
		if (__module == null || __in == null)
			throw new NullPointerException("NARG");
		
		return "sjmeRc__" + __module.baseName + "__" + Utils.mangle(__in);
	}
	
	/**
	 * Determines the symbol for the method.
	 * 
	 * @param __module The glob used.
	 * @param __method The method used.
	 * @return The symbol for the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public static String symbolMethodName(NanoCoatLinkGlob __module,
		Method __method)
		throws NullPointerException
	{
		if (__module == null || __method == null)
			throw new NullPointerException("NARG");
		
		return "sjmeMt__" + __module.baseName + "__" +
			Utils.mangle(__method.inClass()) + "__" +
			Utils.mangle(__method.name()) + "__" + __method.methodIndex();
	}
	
	/**
	 * Quotes the input as a string.
	 * 
	 * @param __input The input.
	 * @return The input as a quoted string.
	 * @since 2023/05/31
	 */
	public static String quotedString(Object __input)
	{
		if (__input == null)
			return null;
		
		// Open string
		StringBuilder sb = new StringBuilder("\"");
		
		// Whatever bytes need to be encoded properly
		try
		{
			for (byte b : __input.toString().getBytes("utf-8"))
			{
				if (b == '\t')
					sb.append("\\t");
				else if (b == '\r')
					sb.append("\\r");
				else if (b == '\n')
					sb.append("\\n");
				else if (b >= 0x20 && b < 0x7F)
					sb.append((char)b);
				
				// This is really the only most consistent here
				else
					sb.append(String.format("\\x%03o", b & 0xFF));
			}
		}
		catch (IOException __e)
		{
			throw new RuntimeException(__e);
		}
		
		// Close string
		sb.append("\"");
		
		return sb.toString();
	}
}
