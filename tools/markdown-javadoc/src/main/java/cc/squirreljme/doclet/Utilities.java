// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.doclet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.markdownwriter.MarkdownWriter;

/**
 * Utilities for document generation.
 *
 * @since 2022/08/24
 */
public final class Utilities
{
	/**
	 * Not used.
	 * 
	 * @since 2022/08/24
	 */
	private Utilities()
	{
	}
	
	/**
	 * Returns the first line of text.
	 * 
	 * @param __text The text to get.
	 * @return The first line of text.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/24
	 */
	public static String firstLine(String __text)
		throws NullPointerException
	{
		if (__text == null)
			throw new NullPointerException("NARG");
		
		// Find where the string ends.
		int len = __text.length();
		char lastChar = 0xFFFD;
		for (int i = 0; i < len; i++)
		{
			char nowChar = __text.charAt(i);
			
			// Whitespace followed by a period
			if (lastChar == '.' && Character.isWhitespace(nowChar))
				return __text.substring(0, i);
			
			// Set new last character
			lastChar = nowChar;
		}
		
		// If this point was reached, entire is used
		return __text;
	}
	
	/**
	 * Neatens the text so it does not look horrible.
	 * 
	 * @param __text The text to neaten.
	 * @return The neatened text.
	 * @since 2022/08/24
	 */
	public static String neatText(String __text)
	{
		// If null, just carry it over
		if (__text == null)
			return null;
		
		StringBuilder result = new StringBuilder(__text.length());
		
		// Process every character
		char lastChar = 0xFFFD;
		boolean useChar;
		for (int i = 0, n = __text.length(); i < n; i++)
		{
			// Use the character by default
			useChar = true;
			char nowChar = __text.charAt(i);
			
			// Normalize any and all whitespace to just space
			if (Character.isWhitespace(nowChar))
				nowChar = ' ';
			
			// If there is whitespace after whitespace, trim it down to just
			// a single one...
			if (lastChar == ' ' && nowChar == ' ')
				useChar = false;
			
			// Use character?
			if (useChar)
				result.append(nowChar);
			
			// Set last character for processing
			lastChar = nowChar;
		}
		
		return result.toString();
	}
	
	/**
	 * Returns the relative path to another document.
	 * 
	 * @param __from The source path.
	 * @param __to The target path.
	 * @return The relative string to the path for usage in the document.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/24
	 */
	public static String relativePath(Path __from, Path __to)
		throws NullPointerException
	{
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		// We want to stay in the same directory
		if (!Files.isDirectory(__from))
			__from = __from.getParent();
		
		// Build path
		StringBuilder sb = new StringBuilder();
		for (Path fragment : __from.relativize(__to))
		{
			if (sb.length() > 0)
				sb.append('/');
			
			sb.append(fragment.getFileName());
		}
		
		return sb.toString();
	}
	
	/**
	 * Writes a link to another class.
	 *
	 * @param __writer The writer to write to.
	 * @param __from The class we are coming from.
	 * @param __class The class to target.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/27
	 */
	public static void writerLinkTo(MarkdownWriter __writer,
		ProcessedClass __from, ProcessedClass __class)
		throws IOException, NullPointerException
	{
		if (__writer == null || __from == null || __class == null)
			throw new NullPointerException("NARG");
		
		// If this class is invisible, then hide it from existence and go up
		if (!__class._isVisible)
		{
			// Try going to the super class instead
			ProcessedClass superClass = __class.superClass();
			if (superClass != null)
			{
				Utilities.writerLinkTo(__writer, __from, superClass);
				return;
			}
			
			// Otherwise, refer to object if this is not object
			else if (!__class.name.isObjectClass())
			{
				Utilities.writerLinkTo(__writer, __from,
					__from.doclet().processClass(
						__from.classDoc.findClass("java.lang.Object")));
				return;
			}
		}
		
		// Is within our own documentation tree?
		if (__class._implicit)
		{
			// Write direct link to it
			__writer.uri(Utilities.relativePath(__from._documentPath,
				__class._documentPath), __class.name.simpleName().toString());
			
			return;
		}
		
		// Points outside our documentation tree? Try to locate it		
		RemoteClass remoteClass = __from.doclet()
			.remoteClassProject(__class.name.toRuntimeString());
		if (remoteClass != null)
		{
			// Write longer relative link to it
			__writer.uri(Utilities.relativePath(__from._documentPath,
				__from.doclet().outputDir) + "/../" + remoteClass.projectName +
				"/" + remoteClass.markdownPath,
				__class.name.simpleName().toString());
			
			return;
		}
		
		// Could not figure out the location, so just use the binary name
		__writer.printf(true, "`%s`",
			__class.name.toRuntimeString());
	}
}
