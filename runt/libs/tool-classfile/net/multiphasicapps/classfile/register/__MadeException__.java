// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import net.multiphasicapps.classfile.ClassName;

/**
 * This stores an exception which was made in code.
 *
 * @since 2019/04/02
 */
public final class __MadeException__
{
	/** The class name to use. */
	protected final ClassName name;
	
	/** The exception combo to target. */
	protected final __ExceptionCombo__ combo;
	
	/**
	 * Initializes the made exception.
	 *
	 * @param __n The class to target.
	 * @param __c The target for the exception handler.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/02
	 */
	public __MadeException__(ClassName __n, __ExceptionCombo__ __c)
		throws NullPointerException
	{
		if (__n == null || __c == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
		this.combo = __c;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/02
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof __MadeException__))
			return false;
		
		__MadeException__ o = (__MadeException__)__o;
		return this.name.equals(o.name) &&
			this.combo.equals(o.combo);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/02
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode() ^ this.combo.hashCode();
	}
}

