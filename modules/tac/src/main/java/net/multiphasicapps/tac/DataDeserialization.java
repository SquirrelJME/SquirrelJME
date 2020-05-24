// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

/**
 * This class contains deserializers for data.
 *
 * @since 2019/01/20
 */
public final class DataDeserialization
{
	/**
	 * Decodes a key for a given secondary value.
	 *
	 * @param __key The key to decode.
	 * @return The decoded key.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
	public static String decodeKey(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Go through string and decode it
		int n;
		StringBuilder sb = new StringBuilder((n = __key.length()));
		boolean escaped = false;
		for (int i = 0; i < n; i++)
		{
			char c = __key.charAt(i);
			
			// Escaping?
			if (escaped)
			{
				// Determine replacement character
				switch (c)
				{
					case 'p':	c = '+'; break;
					case 'h':	c = '#'; break;
					case 'd':	c = '.'; break;
					case '-':	c = '-'; break;
				}
				sb.append(c);
				
				// Do not escape anymore
				escaped = false;
			}
			
			// Going to escape?
			else if (c == '-')
				escaped = true;
			
			// Pass as is
			else
				sb.append(c);
		}
		
		return sb.toString();
	}
	
	/**
	 * Decodes the given string from a manifest safe format to a string.
	 *
	 * @param __s The string to decode.
	 * @return The decoded string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/06
	 */
	public static String decodeString(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		StringBuilder sb = new StringBuilder(__s.length());
		
		// Decode all input characters
		for (int i = 0, n = __s.length(); i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Ignore whitespace, since this could be an artifact of whitespace
			// used in the manifest
			if (c == ' ' || c == '\r' || c == '\n' || c == '\t')
				continue;
			
			// Escaped sequence requires parsing
			else if (c == '\\')
			{
				// Read the next character
				c = __s.charAt(++i);
				
				// Hex sequence for any character
				if (c == '@')
				{
					// Build string to decode hex sequence from
					StringBuilder sub = new StringBuilder(4);
					sub.append(__s.charAt(++i));
					sub.append(__s.charAt(++i));
					sub.append(__s.charAt(++i));
					sub.append(__s.charAt(++i));
					
					// Decode character
					c = (char)(Integer.valueOf(sub.toString(), 16).intValue());
				}
				
				// Code for specific characters
				else
					switch (c)
					{
							// Unchanged
						case '\\':
						case '\"':
							break;
							
							// Space
						case '_':
							c = ' ';
							break;
							
							// Newline
						case 'n':
							c = '\n';
							break;
							
							// Carriage return
						case 'r':
							c = '\r';
							break;
							
							// Tab
						case 't':
							c = '\t';
							break;
							
							// Delete
						case 'd':
							c = (char)0x7F;
							break;
							
							// Open brace
						case '(':
							c = '{';
							break;
							
							// Closing brace
						case ')':
							c = '}';
							break;
						
							// Used to represent all the other upper
							// sequences
						default:
							if (c >= '0' && c <= '9')
								c = (char)(c - '0');
							else if (c >= 'A' && c <= 'Z')
								c = (char)((c - 'A') + 10);
							break;
					}
				
				// Append normalized
				sb.append(c);
			}
			
			// Not escaped
			else
				sb.append(c);
		}
		
		return sb.toString();
	}
	
	/**
	 * Converts the given string to an object.
	 *
	 * @param __s The object to convert.
	 * @return The converted object.
	 * @throws InvalidTestParameterException If the input could not be
	 * converted.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/06
	 */
	public static Object deserialize(String __s)
		throws InvalidTestParameterException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Basic conversions
		switch (__s)
		{
			case "null":
				return null;
			
			case "NoResult":
				return new __NoResult__();
			
			case "UndefinedResult":
				return new __UndefinedResult__();
			
			case "ExceptionThrown":
				return new __ExceptionThrown__();
			
			case "NoExceptionThrown":
				return new __NoExceptionThrown__();
			
			case "true":
				return Boolean.TRUE;
			
			case "false":
				return Boolean.FALSE;
			
			default:
				break;
		}
		
		// A string
		if (__s.startsWith("string:"))
			return DataDeserialization.decodeString(__s.substring(7));
		
		// Byte
		else if (__s.startsWith("byte:"))
			return Byte.valueOf(__s.substring(5));
			
		// Short
		else if (__s.startsWith("short:"))
			return Short.valueOf(__s.substring(6));
			
		// Char
		else if (__s.startsWith("char:"))
			return Character.valueOf(
				(char)Integer.valueOf(__s.substring(5)).intValue());
		
		// Integer
		else if (__s.startsWith("int:"))
			return Integer.valueOf(__s.substring(4));
		
		// Long
		else if (__s.startsWith("long:"))
			return Long.valueOf(__s.substring(5));
		
		// {@squirreljme.error BU01 The specified string cannot be converted
		// to an object because it an unknown representation, the conversion
		// is only one way. (The encoded data)}
		else if (__s.startsWith("other:"))
			throw new InvalidTestParameterException(
				String.format("BU01 %s", __s));
		
		// {@squirreljme.error BU02 The specified object cannot be
		// decoded because it is not known or does not support decoding.
		// (The encoded data)}
		else
			throw new InvalidTestParameterException(
				String.format("BU02 %s", __s));
	}
}

