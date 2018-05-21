// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represents a string constant value.
 *
 * @since 2018/05/21
 */
public class ConstantValueString
	extends ConstantValue
	implements CharSequence
{
	/** The used string. */
	protected final String value;
	
	/**
	 * Initializes the value.
	 *
	 * @param __s The value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/21
	 */
	public ConstantValueString(String __s)
		throws NullPointerException
	{
		super(__s, ConstantValueType.STRING);
		
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.value = __s;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/21
	 */
	@Override
	public final char charAt(int __i)
	{
		return this.value.charAt(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/21
	 */
	@Override
	public final int length()
	{
		return this.value.length();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/21
	 */
	@Override
	public final CharSequence subSequence(int __s, int __e)
	{
		// So the same class type is returned
		return new ConstantValueString(this.value.substring(__s, __e));
	}
}

