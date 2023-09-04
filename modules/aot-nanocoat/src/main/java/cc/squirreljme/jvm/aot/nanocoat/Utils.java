// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CSourceWriter;
import cc.squirreljme.c.out.AppendableCTokenOutput;
import cc.squirreljme.c.out.CTokenOutput;
import cc.squirreljme.c.out.EchoCTokenOutput;
import cc.squirreljme.c.out.PrettyCTokenOutput;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Identifier;
import net.multiphasicapps.classfile.Method;
import org.intellij.lang.annotations.Language;

/**
 * General utilities.
 *
 * @since 2023/05/28
 */
public final class Utils
{
	/** File name length limit. */
	private static final int _FILENAME_LENGTH_LIMIT =
		24;
	
	/** Header for CMake files. */
	@Language("CMake")
	private static final String[] CMAKE_HEADER =
		new String[]{
			"# -----------------------------------------------------------" +
				"----------------",
			"# SquirrelJME",
			"#     Copyright (C) Stephanie Gawroriski " +
				"<xer@multiphasicapps.net>",
			"# -----------------------------------------------------------" +
				"----------------",
			"# SquirrelJME is under the Mozilla Public License Version 2.0.",
			"# See license.mkd for licensing and copyright information.",
			"# -----------------------------------------------------------" +
				"----------------"};
	
	/** Source file header for branding and otherwise. */
	@Language("C")
	private static final String[] C_HEADER =
		new String[]{"/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-",
			"// -----------------------------------------------------------" +
				"----------------",
			"// SquirrelJME",
			"//     Copyright (C) Stephanie Gawroriski " +
				"<xer@multiphasicapps.net>",
			"// -----------------------------------------------------------" +
				"----------------",
			"// SquirrelJME is under the Mozilla Public License Version 2.0.",
			"// See license.mkd for licensing and copyright information.",
			"// -----------------------------------------------------------" +
				"------------- */"};
	
	/**
	 * Not used.
	 * 
	 * @since 2023/05/28
	 */
	private Utils()
	{
	}
	
	/**
	 * Wraps output to a {@link CFile} in a generic way.
	 *
	 * @param __out The output stream.
	 * @return The resultant C File.
	 * @throws IOException If it could not be opened.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/17
	 */
	public static CFile cFile(OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Base output
		CTokenOutput base = new AppendableCTokenOutput(
			new PrintStream(__out, true, "utf-8"));
		
		// Is debugging used?
		CTokenOutput wrapped;
		if (false && Debugging.ENABLED)
			wrapped = new EchoCTokenOutput(System.err, base);
		else
			wrapped = base;
		
		// Setup C output
		return new CFile(new PrettyCTokenOutput(wrapped));
	}
	
	/**
	 * Determines a basic compatible file name.
	 * 
	 * @param __in The input name.
	 * @return The DOS compatible file name.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/23
	 */
	public static final String basicFileName(String __in)
		throws NullPointerException
	{
		return Utils.basicFileName(__in, Utils._FILENAME_LENGTH_LIMIT);
	}
	
	/**
	 * Determines a basic compatible file name.
	 * 
	 * @param __in The input name.
	 * @return The DOS compatible file name.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public static final String basicFileName(String __in, int __lenLimit)
		throws IllegalArgumentException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		if (__lenLimit < 8)
			throw new NullPointerException("NLEN");
		
		// Extract extension if there is one
		int lastDot = __in.lastIndexOf('.');
		String ext = (lastDot >= 0 ?
			"." + __in.substring(lastDot + 1) : "");
		
		// Determine actual name
		StringBuilder base = new StringBuilder();
		for (int i = 0, n = __in.length(); i < n; i++)
		{
			char c = __in.charAt(i);
			
			// Force all names to lowercase
			if (c >= 'A' && c <= 'Z')
				base.append(Character.toLowerCase(c));
			else if ((c >= 'a' && c <= 'z') ||
				(c >= '0' && c <= '9') || c == '_')
				base.append(c);
			else
				base.append('_');
		}
		
		// Long file name?
		int baseLen = base.length();
		if (baseLen > __lenLimit)
		{
			// Determine the hash of the name
			String temp = base.toString();
			int hash = temp.hashCode();
			if (hash < 0)
				hash = -hash;
			
			// Convert to a max radix number which will have part of the name
			String code = Long.toString(hash & 0xFFFFFFFFL,
				Character.MAX_RADIX).toLowerCase();
			
			// Need this to determine how much to take off
			int codeLen = code.length();
			
			// Determine the pivot point where the hash will be inserted
			// accordingly, splitting into thirds will make it so the second
			// half has more character use while the left loses them
			int pivotBase = __lenLimit / 3;
			
			// Remove characters to fit everything
			int sizeGap = __lenLimit - codeLen;
			while (base.length() > sizeGap)
				base.deleteCharAt(pivotBase);
			
			// Insert the code directly at the pivot point
			base.insert(pivotBase, code);
		}
		
		// Build name
		return (base + ext).toLowerCase();
	}
	
	/**
	 * Writes the output file C header.
	 *
	 * @param __out The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/19
	 */
	public static void headerC(CSourceWriter __out)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Always use enforced newlines for the header
		for (String line : Utils.C_HEADER)
			__out.token(line, true);
		
		// Always have an extra space with enforced newline
		__out.newLine(true);
	}
	
	/**
	 * Writes the output file CMake header.
	 *
	 * @param __out The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/23
	 */
	public static void headerCMake(PrintStream __out)
		throws IOException, NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Always use enforced newlines for the header
		for (String line : Utils.CMAKE_HEADER)
			__out.println(line);
		
		// Always have an extra space with enforced newline
		__out.println();
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
		
		return "sj_" + __module.aotSettings.clutterLevel + "__" +
			__module.baseName + "__" + Utils.mangle(__in);
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
		
		return "rc_" + __module.aotSettings.clutterLevel + "__" +
			__module.baseName + "__" + Utils.mangle(__in);
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
		
		return "mt_" + __module.baseName + "__" +
			Utils.mangle(__method.inClass()) + "__" +
			Utils.mangle(__method.name()) + "__" + __method.methodIndex();
	}
}
