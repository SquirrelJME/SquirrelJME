// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * A number represents any kind of number which may be transformed to other
 * types for those numbers.
 *
 * Note that any of these methods may result in values which lose their sign,
 * significant bits, or the value itself. Most classes will likely implement
 * these using narrowing conversions that match the Java language.
 *
 * @since 2018/09/23
 */
@Api
public abstract class Number
{
	
	/**
	 * Returns the value of this number converted as a double.
	 *
	 * @return The converted value of this number.
	 * @since 2018/09/23
	 */
	@Api
	public abstract double doubleValue();
	
	/**
	 * Returns the value of this number converted as a float.
	 *
	 * @return The converted value of this number.
	 * @since 2018/09/23
	 */
	@Api
	public abstract float floatValue();
	
	/**
	 * Returns the value of this number converted as an integer.
	 *
	 * @return The converted value of this number.
	 * @since 2018/09/23
	 */
	@Api
	public abstract int intValue();
	
	/**
	 * Returns the value of this number converted as a long.
	 *
	 * @return The converted value of this number.
	 * @since 2018/09/23
	 */
	@Api
	public abstract long longValue();
	
	/**
	 * Returns the value of this number converted as a byte.
	 *
	 * @return The converted value of this number.
	 * @since 2018/09/23
	 */
	@Api
	public byte byteValue()
	{
		return (byte)this.intValue();
	}
	
	/**
	 * Returns the value of this number converted as a short.
	 *
	 * @return The converted value of this number.
	 * @since 2018/09/23
	 */
	@Api
	public short shortValue()
	{
		return (short)this.intValue();
	}
}

