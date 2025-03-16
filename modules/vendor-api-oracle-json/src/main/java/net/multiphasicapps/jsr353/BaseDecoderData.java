// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

/**
 * Represents a single input token kind.
 *
 * @since 2015/04/12
 */
public final class BaseDecoderData
{
	/** The containing token text. */
	protected final String token;
	
	/** The type of input token that this represents. */
	protected final BaseDecoderType type;
	
	/**
	 * Initializes new immutable data.
	 *
	 * @param __t The data type.
	 * @param __s The token text.
	 * @throws NullPointerException If any argument is {@code null}
	 * on specified {@code Type} values.
	 * @since 2015/04/12
	 */
	public BaseDecoderData(BaseDecoderType __t, String __s)
		throws NullPointerException
	{
		// Check
		if (__t == null || (__s == null && (__t == BaseDecoderType.STRING || __t == BaseDecoderType.LITERAL)))
			throw new NullPointerException("na");
		
		// Set
		this.type = __t;
		this.token = __s;
	}
	
	/**
	 * Returns the type of data that this is.
	 *
	 * @return The type of data this is.
	 * @since 2015/04/12
	 */
	public BaseDecoderType getType()
	{
		return this.type;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2015/04/12
	 */
	@Override
	public String toString()
	{
		return (this.token != null ? this.token : "");
	}
}
