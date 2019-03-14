// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.mini;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;

/**
 * This represents a method which has been mimized.
 *
 * @since 2019/03/14
 */
public final class MinimizedMethod
{
	/** Flags that are used for the method. */
	public final int flags;
	
	/** The index of this method in the instance table. */
	public final int index;
	
	/** The name of the method. */
	public final MethodName name;
	
	/** The type of the method. */
	public final MethodDescriptor type;
	
	/** Translated method code. */
	private final byte[] _code;
	
	/**
	 * Initializes the minimized method.
	 *
	 * @param __f The method flags.
	 * @param __o Index in the method table for instances.
	 * @param __n The method name.
	 * @param __t The method type.
	 * @param __tc Transcoded instructions.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/14
	 */
	public MinimizedMethod(int __f, int __o,
		MethodName __n, MethodDescriptor __t, byte[] __tc)
		throws NullPointerException
	{
		if (__n == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.flags = __f;
		this.index = __o;
		this.name = __n;
		this.type = __t;
		this._code = (__tc == null ? new byte[0] : __tc.clone());
	}
}

