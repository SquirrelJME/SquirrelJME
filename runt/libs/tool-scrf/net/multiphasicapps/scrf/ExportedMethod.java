// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodName;

/**
 * Represents a method which has been exported.
 *
 * @since 2019/02/24
 */
public final class ExportedMethod
	implements Exported
{
	/** Flags. */
	protected final MethodFlags flags;
	
	/** Name. */
	protected final MethodName name;
	
	/** Type. */
	protected final MethodDescriptor type;
	
	/** Code for the method. */
	protected final ILCode code;
	
	/**
	 * Initializes the exported method.
	 *
	 * @param __f The flags.
	 * @param __n The method name.
	 * @param __t The method type.
	 * @param __c The code, must be {@code null} if native/abstract.
	 * @throws NullPointerException On null arguments.
	 * @throws SummerFormatException If the native/abstract does not match
	 * {@code __c} being {@code null}.
	 * @since 2019/02/24
	 */
	public ExportedMethod(MethodFlags __f, MethodName __n,
		MethodDescriptor __t, ILCode __c)
		throws NullPointerException, SummerFormatException
	{
		if (__f == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AV09 Abstract/native and code availability
		// mismatch. (The flags)}
		if ((__f.isAbstract() || __f.isNative()) != (__c == null))
			throw new SummerFormatException("AV09 " + __f);
		
		this.flags = __f;
		this.name = __n;
		this.type = __t;
		this.code = __c;
	}
}

