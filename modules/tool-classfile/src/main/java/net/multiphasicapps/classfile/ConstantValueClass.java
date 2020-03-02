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
 * This represents a constant value which is of a class.
 *
 * @since 2018/09/19
 */
public final class ConstantValueClass
	extends ConstantValue
{
	/**
	 * Initializes the class constant value.
	 *
	 * @param __cn The class constant.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/19
	 */
	public ConstantValueClass(ClassName __cn)
		throws NullPointerException
	{
		super(__cn, ConstantValueType.CLASS);
	}
	
	/**
	 * Returns the class name.
	 *
	 * @return The class name.
	 * @since 2018/09/19
	 */
	public final ClassName className()
	{
		return (ClassName)this.value;
	}
}

