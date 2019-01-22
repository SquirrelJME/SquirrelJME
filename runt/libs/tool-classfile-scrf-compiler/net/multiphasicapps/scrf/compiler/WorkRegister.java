// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

import net.multiphasicapps.classfile.JavaType;

/**
 * This represents a single working register, which has a given index.
 *
 * @since 2019/01/21
 */
public final class WorkRegister
{
	/** The index of this work register. */
	protected final int index;
	
	/** The Java type used. */
	private JavaType _jtype =
		JavaType.NOTHING;
	
	/**
	 * Initializes the work register.
	 *
	 * @param __idx The index of this register.
	 * @since 2019/01/21
	 */
	public WorkRegister(int __idx)
	{
		this.index = __idx;
	}
	
	/**
	 * Returns the Java type.
	 *
	 * @return The Java type.
	 * @since 2019/01/22
	 */
	public final JavaType javaType()
	{
		return this._jtype;
	}
	
	/**
	 * Sets the Java type of this register.
	 *
	 * @param __t The Java type to use.
	 * @since 2019/01/22
	 */
	public final void setJavaType(JavaType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this._jtype = __t;
	}
}

