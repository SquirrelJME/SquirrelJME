// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

/**
 * This contains a value and how that value should be interpreted by the
 * execution engine, it is done this way to simplify various means of usage.
 *
 * @since 2019/03/03
 */
@Deprecated
public final class InterpretAs
{
	/** The interpretation type of the value. */
	protected final InterpretType type;
	
	/** The value to interpret. */
	protected final long value;
	
	/**
	 * Initializes the interpretation.
	 *
	 * @param __t The type of interpretation.
	 * @param __v The value to use, will be interpreted.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/03
	 */
	public InterpretAs(InterpretType __t, int __v)
		throws NullPointerException
	{
		this(__t, (long)__v);
	}
	
	/**
	 * Initializes the interpretation.
	 *
	 * @param __t The type of interpretation.
	 * @param __v The value to use, will be interpreted.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/03
	 */
	public InterpretAs(InterpretType __t, long __v)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this.type = __t;
		this.value = __v;
	}
	
	/**
	 * Initializes an interpretation.
	 *
	 * @param __t The type of interpretation.
	 * @param __v The value to use, will be interpreted.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/03
	 */
	public static final InterpretAs of(InterpretType __t, int __v)
		throws NullPointerException
	{
		return new InterpretAs(__t, __v);
	}
	
	/**
	 * Initializes an interpretation.
	 *
	 * @param __t The type of interpretation.
	 * @param __v The value to use, will be interpreted.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/03
	 */
	public static final InterpretAs of(InterpretType __t, long __v)
		throws NullPointerException
	{
		return new InterpretAs(__t, __v);
	}
}

