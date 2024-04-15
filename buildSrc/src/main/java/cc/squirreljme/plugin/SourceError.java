// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * A single error that exists in source code.
 *
 * @since 2020/02/22
 */
public final class SourceError
{
	/** The maximum error code index that can exist. */
	static final int _MAX_ERROR_CODE =
		Character.MAX_RADIX * Character.MAX_RADIX;
	
	/** The code for the project. */
	public final String projectCode;
	
	/** The error index. */
	public final int index;
	
	/** The body of the error. */
	public final String body;
	
	/** Extra parameters. */
	public final List<String> parameters;
	
	/** Where is this located? */
	public final Path where;
	
	/**
	 * Decodes the content to tag information.
	 *
	 * @param __content The content to decode.
	 * @param __where Where the file is located.
	 * @throws IllegalArgumentException If the content is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public SourceError(Iterable<String> __content, Path __where)
		throws IllegalArgumentException, NullPointerException
	{
		if (__content == null)
			throw new NullPointerException();
		
		// Set file location
		this.where = __where;
		
		// Put items into the queue
		Deque<String> queue = new ArrayDeque<String>();
		for (String item : __content)
			queue.add(item);
		
		// Read the error code
		String errorCode = queue.pollFirst();
		if (errorCode == null)
			throw new IllegalArgumentException("No error code.");
		
		if (errorCode.length() < 4)
			throw new IllegalArgumentException("Error code too short.");
		
		// Project code is the first two characters
		this.projectCode = errorCode.substring(0, 2).toUpperCase();
		this.index = SourceError.stringToIndex(errorCode.substring(2, 4));
		
		// Fill in content description
		StringBuilder body = new StringBuilder();
		while (!queue.isEmpty())
		{
			// Stop when nothing is left, or if there are starting parameters
			String item = queue.peekFirst();
			if (item == null || item.equals("("))
				break;
			
			// Add formatting space
			if (body.length() > 0)
				body.append(' ');
			
			// And append whatever we put in
			body.append(queue.removeFirst());
		}
		
		// Store body in
		this.body = body.toString();
		
		// Parameter list?
		List<String> parameters = new ArrayList<String>();
		if ("(".equals(queue.peekFirst()))
		{
			// Remove the starting parenthesis so it is not added
			queue.removeFirst();
			
			// Process queue
			StringBuilder buffer = new StringBuilder();
			while (!queue.isEmpty())
			{
				String item = queue.peekFirst();
				
				// End parameter or next?
				boolean isEnd = (item == null || item.equals(")"));
				if (isEnd || item.equals(";"))
				{
					// Consume it
					queue.pollFirst();
					
					// Store into parameter list?
					if (buffer.length() > 0)
					{
						parameters.add(buffer.toString());
						buffer.setLength(0);
					}
					
					// End here?
					if (isEnd)
						break;
				}
				
				// Add formatting space
				if (buffer.length() > 0)
					buffer.append(' ');
				
				// And append whatever we put in
				buffer.append(queue.removeFirst());
			}
		}
		
		// Store parameters
		this.parameters = Collections.<String>unmodifiableList(parameters);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/22
	 */
	@Override
	public final String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		// Code index
		sb.append(this.projectCode);
		sb.append(SourceError.indexToString(this.index));
		sb.append(": ");
		
		// Content
		sb.append(this.body);
		
		List<String> parameters = this.parameters;
		if (!parameters.isEmpty())
		{
			sb.append(" (");
			
			for (int i = 0, n = parameters.size(); i < n; i++)
			{
				if (i > 0)
					sb.append("; ");
				
				sb.append(parameters.get(0));
			}
			
			sb.append(")");
		}
		
		// Place location of this file
		Path where = this.where;
		if (where != null)
		{
			sb.append(" <");
			sb.append(where);
			sb.append(">");
		}
		
		return sb.toString();
	}
	
	/**
	 * Returns string form of the given index.
	 *
	 * @param __index The index to translate.
	 * @return The translated string to index.
	 * @since 2020/02/22
	 */
	public static String indexToString(int __index)
	{
		StringBuilder sb = new StringBuilder(4);
		
		sb.append(Character.toLowerCase(Character.forDigit(
			__index / Character.MAX_RADIX, Character.MAX_RADIX)));
		sb.append(Character.toLowerCase(Character.forDigit(
			__index % Character.MAX_RADIX, Character.MAX_RADIX)));
		
		return sb.toString();
	}
	
	/**
	 * Returns the index for the given string.
	 *
	 * @return The resulting index.
	 * @throws IllegalArgumentException If the string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public static int stringToIndex(String __string)
		throws IllegalArgumentException, NullPointerException
	{
		if (__string == null)
			throw new NullPointerException("No string specified.");
		
		if (__string.length() != 2)
			throw new IllegalArgumentException("Invalid string length.");
		
		int rv = (Character.digit(__string.charAt(0), Character.MAX_RADIX) *
			Character.MAX_RADIX) +
			Character.digit(__string.charAt(1), Character.MAX_RADIX);
		
		if (rv < 0 || rv > SourceError._MAX_ERROR_CODE)
			throw new IllegalArgumentException("Out of range index.");
		
		return rv;
	}
}
