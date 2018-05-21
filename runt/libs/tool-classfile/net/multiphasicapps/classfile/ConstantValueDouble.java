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
 * Represents a double constant value.
 *
 * @since 2018/05/21
 */
public final class ConstantValueDouble
	extends ConstantValueNumber
{
	/**
	 * Initializes the constant value.
	 *
	 * @param __v The value.
	 * @since 2018/05/21
	 */
	public ConstantValueDouble(double __v)
	{
		super(__v, ConstantValueType.DOUBLE);
	}
}

