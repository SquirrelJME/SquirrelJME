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
 * Indicates that an exception happened during JSON parsing.
 *
 * @since 2014/07/25
 */
@Api
public class JsonException
	extends RuntimeException
{
	/** Serialized version ID. */
	private static final long serialVersionUID = 0xBAD5EE1A715A7100L;
	
	/**
	 * Initializes with the specified message, however with no cause.
	 *
	 * @param __m Message to set.
	 * @since 2014/07/25
	 */
	@Api
	public JsonException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes with the specified message and cause.
	 *
	 * @param __m Message to set.
	 * @param __c Cause of the exception.
	 * @since 2014/07/25
	 */
	@Api
	public JsonException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
}

