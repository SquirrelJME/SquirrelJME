// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import com.oracle.json.JsonString;

/**
 * This represents a single string value used in an object.
 *
 * @since 2014/08/05
 */
public class ImplValueString
	implements JsonString
{
	/** String value. */
	private final String _val;
	
	/**
	 * Initializes with a string value.
	 *
	 * @param __v String value to set.
	 * @since 2014/08/05
	 */
	public ImplValueString(String __v)
	{
		// Cannot be null
		if (__v == null)
			throw new NullPointerException(
				"No string value specified.");
		
		// Set
		this._val = __v;
	}
	
	/**
	 * Returns the type of value this is.
	 *
	 * @return The type of value that this is.
	 * @since 2014/08/05
	 */
	@Override
	public ValueType getValueType()
	{
		return ValueType.STRING;
	}
	/**
	 * Returns the string representation of this value.
	 *
	 * @return The value of this object represented as a string.
	 * @since 2014/08/05
	 */
	@Override
	public String toString()
	{
		return this._val;
	}
	
	/**
	 * Returns {@code true} only if the other object is a {@link JsonString}
	 * and the strings values match with this one.
	 *
	 * @param __o The object to compare against.
	 * @return {@code true} if this equals another {@link JsonString}.
	 * @since 2014/08/05
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Get object
		if (!(__o instanceof JsonString))
			return false;
		
		// Only if strings match
		return this.getString().equals(((JsonString)__o).getString());
	}
	
	/**
	 * Returns a character sequence which represents the internal string value.
	 *
	 * @return A character sequence for this specified string.
	 * @since 2014/08/05
	 */
	@Override
	public CharSequence getChars()
	{
		return this._val;
	}
	
	/**
	 * Returns the string which is the value of this string.
	 *
	 * @return A string.
	 * @since 2014/08/05
	 */
	@Override
	public String getString()
	{
		return this._val;
	}
	
	/**
	 * Invokes {@code getString().hashCode()}.
	 *
	 * @return The hash code.
	 * @since 2014/08/05
	 */
	@Override
	public int hashCode()
	{
		return this.getString().hashCode();
	}
	
	/**
	 * Escapes the specified string
	 *
	 * @param __v Value to escape.
	 * @since 2014/08/06
	 */
	public static final String escapedForm(String __v)
	{
		// Cannot be null
		if (__v == null)
			throw new NullPointerException(
				"No string specified.");
		
		// Run through the string
		StringBuilder sb = new StringBuilder();
		int n = __v.length();
		for (int i = 0; i < n; i++)
		{
			char c = __v.charAt(i);
			
			// Possibly escaped
			char e = 0;
			switch (c)
			{
					// Escapable sequences
				case '\"': e = '\"'; break;
				case '\\': e = '\\'; break;
				case '/': e = '/'; break;
				case '\b': e = 'b'; break;
				case '\f': e = 'f'; break;
				case '\n': e = 'n'; break;
				case '\r': e = 'r'; break;
				case '\t': e = 't'; break;
				
					// Not escaped
				default:
					break;
			}
			
			// Append character
			if (e != 0)
			{
				sb.append('\\');
				sb.append(e);
			}
			
			// Normal
			else
				sb.append(c);
		}
		
		// Return as string
		return sb.toString();
	}
}

