// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import dev.shadowtail.classfile.mini.MinimizedMethod;

/**
 * This is a method handle which is bound to a single method and one where the
 * execution uses the exactly specified method, no lookups are performed.
 *
 * @since 2019/01/10
 */
public final class StaticMethodHandle
	implements MethodHandle
{
	/** Runtime constant pool. */
	protected final RuntimeConstantPool runpool;
	
	/** The method to use. */
	protected final MinimizedMethod minimethod;
	
	/**
	 * Initializes the static method handle.
	 *
	 * @param __rp The run-time constant pool.
	 * @param __m The minimized method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public StaticMethodHandle(RuntimeConstantPool __rp, MinimizedMethod __m)
		throws NullPointerException
	{
		if (__rp == null || __m == null)
			throw new NullPointerException("NARG");
		
		this.runpool = __rp;
		this.minimethod = __m;
	}
}

