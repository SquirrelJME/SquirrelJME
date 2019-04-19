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
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;

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
	
	/** The class this is in. */
	protected final ClassName inclass;
	
	/** The method this is in. */
	protected final MethodName inmethodname;
	
	/** The method's type. */
	protected final MethodDescriptor inmethodtype;
	
	/**
	 * Initializes the static method handle.
	 *
	 * @param __rp The run-time constant pool.
	 * @param __m The minimized method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/17
	 */
	public StaticMethodHandle(RuntimeConstantPool __rp, MinimizedMethod __m,
		ClassName __icl, MethodNameAndType __nat)
		throws NullPointerException
	{
		if (__rp == null || __m == null || __icl == null || __nat == null)
			throw new NullPointerException("NARG");
		
		this.runpool = __rp;
		this.minimethod = __m;
		this.inclass = __icl;
		this.inmethodname = __nat.name();
		this.inmethodtype = __nat.type();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/19
	 */
	@Override
	public final StaticMethodHandle resolve(LoadedClass __ctxcl)
	{
		return this;
	}
}

