// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import java.util.HashSet;
import java.util.Set;

/**
 * Private decoding state information, for each scope.
 *
 * @since 2015/04/12
 */
class __Scope__
{
	/** Expectation set. */
	public final Set<__Exp__> expect = new HashSet<>();
	
	/** The starting type of the scope information. */
	public final BaseDecoderType type;
	
	/**
	 * Initializes start of scope.
	 *
	 * @param __t Type which starts the scope.
	 * @throws IllegalArgumentException If not start of an object or an
	 * array.
	 * @since 2015/04/12
	 */
	__Scope__(BaseDecoderType __t)
		throws IllegalArgumentException
	{
		// Check
		if (__t != BaseDecoderType.START_OBJECT && __t != BaseDecoderType.START_ARRAY)
			throw new IllegalArgumentException("snsoa");
		
		// Set
		this.type = __t;
		
		// Initial state that depends on the entry type
		switch (this.type)
		{
			// Start of an object
			case START_OBJECT:
				this.expect.add(__Exp__.KEY_OR_END);
				break;
			
			// Start of an array
			case START_ARRAY:
				this.expect.add(__Exp__.VALUE_OR_END);
				break;
			
			// Unhandled
			default:
				throw new RuntimeException(
					String.format("unhsct", this.type.name()));
		}
	}
	
	/**
	 * Is this an array?
	 *
	 * @return {@code true} if it is.
	 * @since 2015/04/12
	 */
	public boolean isArray()
	{
		return this.type == BaseDecoderType.START_ARRAY;
	}
	
	/**
	 * Is this an object?
	 *
	 * @return {@code true} if it is.
	 * @since 2015/04/12
	 */
	public boolean isObject()
	{
		return this.type == BaseDecoderType.START_OBJECT;
	}
	
	/**
	 * Need these things, clears the current expectation set.
	 *
	 * @param __n Things that are needed.
	 * @since 2015/04/12
	 */
	public void need(__Exp__... __n)
	{
		this.expect.clear();
		for (__Exp__ x : __n)
			this.expect.add(x);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2015/04/12
	 */
	@Override
	public String toString()
	{
		return (this.type == BaseDecoderType.START_OBJECT ? "Object" :
			this.type == BaseDecoderType.START_ARRAY ? "Array" :
				"???") + " [" + this.expect + "]";
	}
	
	/**
	 * Checks if the specified thing is desired.
	 *
	 * @return {@code true} If this is being expected.
	 * @since 2015/04/12
	 */
	public boolean want(__Exp__ __e)
	{
		return this.expect.contains(__e);
	}
}
