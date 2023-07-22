// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.oracle.json;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This represents an immutable JSON value.
 *
 * @since 2014/07/26
 */
@Api
public interface JsonValue
{
	/** The null literal. */
	@Api
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
	@Api
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
	@Api
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
	@Api
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
	@Api
	enum ValueType
	{
		/** An array. */
		@Api
		ARRAY,
		
		/** An object. */
		@Api
		OBJECT,
		
		/** A string. */
		@Api
		STRING,
		
		/** A number. */
		@Api
		NUMBER,
		
		/** True. */
		@Api
		TRUE,
		
		/** False. */
		@Api
		FALSE,
		
		/** Null value. */
		@Api
		NULL,
		
		/** End. */
		;
	}
}

