// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import dev.shadowtail.classfile.mini.MinimizedPool;

/**
 * This is a constant pool which is initialized at run-time when it is needed
 * to.
 *
 * @since 2019/04/17
 */
public final class RuntimeConstantPool
{
	/** The pool this references. */
	protected final MinimizedPool minipool;
	
	/** Realized pool entries with references and such. */
	volatile Object[] _realized;
	
	/**
	 * Initializes the runtime constant pool.
	 *
	 * @param __mp The minimized pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public RuntimeConstantPool(MinimizedPool __mp)
		throws NullPointerException
	{
		if (__mp == null)
			throw new NullPointerException("NARG");
		
		this.minipool = __mp;
	}
	
	/**
	 * Gets the pool value.
	 *
	 * @param __i The index.
	 * @return The realized value.
	 * @since 2019/04/20
	 */
	public final Object get(int __i)
	{
		return this._realized[__i];
	}
}

