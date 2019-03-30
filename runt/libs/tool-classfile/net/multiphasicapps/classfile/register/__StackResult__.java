// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import net.multiphasicapps.classfile.JavaType;

/**
 * This contains the result of the operation when a push or pop has been
 * performed. Since pushing/popping is complicated and will involve
 * information on the type and registers, this is needed to
 * simplify the design of the processor.
 *
 * @since 2019/02/23
 */
@Deprecated
final class __StackResult__
{
	/** The slot this references. */
	public final __StackState__.Slot slot;
	
	/** The Java type which is involved here. */
	public final JavaType type;
	
	/** The register used. */
	public final int register;
	
	/** Is this cached? */
	public final boolean cached;
	
	/** Is this not being counted? */
	public final boolean notcounting;
	
	/**
	 * Initializes the result.
	 *
	 * @param __s The slot to use.
	 * @param __jt The Java type.
	 * @param __rl The register location.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/16
	 */
	__StackResult__(__StackState__.Slot __s, JavaType __jt, int __rl)
		throws NullPointerException
	{
		if (__s == null || __jt == null)
			throw new NullPointerException("NARG");
		
		this.slot = __s;
		this.type = __jt;
		this.register = __rl;
		this.cached = false;
		this.notcounting = false;
	}
	
	/**
	 * Initializes the result.
	 *
	 * @param __s The slot this uses.
	 * @param __jt The Java type.
	 * @param __rl The register location.
	 * @param __ch Is this cached?
	 * @param __nc Not counting this?
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/16
	 */
	__StackResult__(__StackState__.Slot __s, JavaType __jt, int __rl,
		boolean __ch, boolean __nc)
		throws NullPointerException
	{
		if (__s == null || __jt == null)
			throw new NullPointerException("NARG");
		
		this.slot = __s;
		this.type = __jt;
		this.register = __rl;
		this.cached = __ch;
		this.notcounting = __nc;
	}
	
	/**
	 * Is this result cached?
	 *
	 * @return If this result is cached.
	 * @since 2019/03/27
	 */
	public final boolean isCached()
	{
		return this.cached;
	}
	
	/**
	 * Does this result need counting when it is operated on?
	 *
	 * @return If this needs counting.
	 * @since 2019/03/27
	 */
	public final boolean needsCounting()
	{
		return this.type.isObject() && !this.cached && this.notcounting;
	}
}
