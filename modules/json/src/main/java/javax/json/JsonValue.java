// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.json;

/**
 * This represents an immutable JSON value.
 *
 * @since 2014/07/26
 */
public interface JsonValue
{
	/** The null literal. */
	JsonValue NULL =
		new JsonValue()
		{
			@Override
			public ValueType getValueType()
			{
				return ValueType.NULL;
			}
		
			@Override
			public String toString()
			{
				return "null";
			}
		};
	
	/** The true literal .*/
	JsonValue TRUE =
		new JsonValue()
		{
			@Override
			public ValueType getValueType()
			{
				return ValueType.TRUE;
			}
		
			@Override
			public String toString()
			{
				return "true";
			}
		};
	
	/** The false literal. */
	JsonValue FALSE =
		new JsonValue()
		{
			@Override
			public ValueType getValueType()
			{
				return ValueType.FALSE;
			}
		
			@Override
			public String toString()
			{
				return "false";
			}
		};
	
	/**
	 * Returns the type of value this is.
	 *
	 * @return The type of value that this is.
	 * @since 2014/07/26
	 */
	JsonValue.ValueType getValueType();
	
	/**
	 * Returns the string representation of this value.
	 *
	 * @return The value of this object represented as a string.
	 * @since 2014/07/26
	 */
	@Override
	String toString();
	
	/**
	 * This represents the type of the value.
	 *
	 * @since 2014/06/26
	 */
	enum ValueType
	{
		/** An array. */
		ARRAY,
		
		/** An object. */
		OBJECT,
		
		/** A string. */
		STRING,
		
		/** A number. */
		NUMBER,
		
		/** True. */
		TRUE,
		
		/** False. */
		FALSE,
		
		/** Null value. */
		NULL,
		
		/** End. */
		;
	}
}

