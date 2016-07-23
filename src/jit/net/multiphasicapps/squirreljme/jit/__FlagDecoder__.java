// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlag;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlags;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITFieldFlag;
import net.multiphasicapps.squirreljme.jit.base.JITFieldFlags;
import net.multiphasicapps.squirreljme.jit.base.JITMethodFlag;
import net.multiphasicapps.squirreljme.jit.base.JITMethodFlags;

/**
 * This decodes class flags.
 *
 * @since 2016/04/24
 */
final class __FlagDecoder__
{
	/**
	 * Not used.
	 *
	 * @since 2016/04/24
	 */
	private __FlagDecoder__()
	{
		throw new RuntimeException("OOPS");
	}
	
	/**
	 * Decodes the class flag set.
	 *
	 * @param __i The input flag field.
	 * @since 2016/04/23
	 */
	static JITClassFlags __class(int __i)
	{
		Set<JITClassFlag> fl = new HashSet<>();
		
		// Public?
		if (0 != (__i & 0x0001))
			fl.add(JITClassFlag.PUBLIC);
		
		// Final?
		if (0 != (__i & 0x0010))
			fl.add(JITClassFlag.FINAL);
		
		// Super?
		if (0 != (__i & 0x0020))
			fl.add(JITClassFlag.SUPER);
		
		// Interface?
		if (0 != (__i & 0x0200))
			fl.add(JITClassFlag.INTERFACE);
		
		// Abstract?
		if (0 != (__i & 0x0400))
			fl.add(JITClassFlag.ABSTRACT);
		
		// Synthetic?
		if (0 != (__i & 0x1000))
			fl.add(JITClassFlag.SYNTHETIC);
		
		// Annotation?
		if (0 != (__i & 0x2000))
			fl.add(JITClassFlag.ANNOTATION);
		
		// Enumeration?
		if (0 != (__i & 0x4000))
			fl.add(JITClassFlag.ENUM);
		
		// Build it
		return new JITClassFlags(fl);
	}
	
	/**
	 * Parses the flags for a field.
	 *
	 * @param __oc The outer class.
	 * @param __bits The input bits.
	 * @return The field flags.
	 * @since 2016/04/26
	 */
	static JITFieldFlags __field(JITClassFlags __oc, int __bits)
	{
		// Target set
		Set<JITFieldFlag> ff = new HashSet<>();
		
		// Enumeration?
		if (0 != (__bits & 0x4000))
			ff.add(JITFieldFlag.ENUM);
		
		// Final?
		if (0 != (__bits & 0x0010))
			ff.add(JITFieldFlag.FINAL);
		
		// Private?
		if (0 != (__bits & 0x0002))
			ff.add(JITFieldFlag.PRIVATE);
		
		// Protected?
		if (0 != (__bits & 0x0004))
			ff.add(JITFieldFlag.PROTECTED);
		
		// Public?
		if (0 != (__bits & 0x0001))
			ff.add(JITFieldFlag.PUBLIC);
		
		// Static?
		if (0 != (__bits & 0x0008))
			ff.add(JITFieldFlag.STATIC);
		
		// Synthetic?
		if (0 != (__bits & 0x1000))
			ff.add(JITFieldFlag.SYNTHETIC);
		
		// Transient?
		if (0 != (__bits & 0x0080))
			ff.add(JITFieldFlag.TRANSIENT);
		
		// Volatile?
		if (0 != (__bits & 0x0040))
			ff.add(JITFieldFlag.VOLATILE);
		
		// Build flags
		return new JITFieldFlags(__oc, ff);
	}
	/**
	 * Parses the flags for a method.
	 *
	 * @param __oc The outer class.
	 * @param __bits The input bits.
	 * @return The method flags.
	 * @since 2016/04/26
	 */
	static JITMethodFlags __method(JITClassFlags __oc, int __bits)
	{
		// Target set
		Set<JITMethodFlag> ff = new HashSet<>();
		
		// Public method.
		if (0 != (__bits & 0x0001))
			ff.add(JITMethodFlag.PUBLIC);
	
		// Private method.
		if (0 != (__bits & 0x0002))
			ff.add(JITMethodFlag.PRIVATE);
	
		// Protected method.
		if (0 != (__bits & 0x0004))
			ff.add(JITMethodFlag.PROTECTED);
	
		// Static method.
		if (0 != (__bits & 0x0008))
			ff.add(JITMethodFlag.STATIC);
	
		// Final method.
		if (0 != (__bits & 0x0010))
			ff.add(JITMethodFlag.FINAL);
	
		// Synchronized method.
		if (0 != (__bits & 0x0020))
			ff.add(JITMethodFlag.SYNCHRONIZED);
	
		// Bridge method.
		if (0 != (__bits & 0x0040))
			ff.add(JITMethodFlag.BRIDGE);
	
		// Variable argument method.
		if (0 != (__bits & 0x0080))
			ff.add(JITMethodFlag.VARARGS);
	
		// Native method.
		if (0 != (__bits & 0x0100))
			ff.add(JITMethodFlag.NATIVE);
	
		// Abstract method.
		if (0 != (__bits & 0x0400))
			ff.add(JITMethodFlag.ABSTRACT);
	
		// Strict floating point method.
		if (0 != (__bits & 0x0800))
			ff.add(JITMethodFlag.STRICT);
	
		// Synthetic method.
		if (0 != (__bits & 0x1000))
			ff.add(JITMethodFlag.SYNTHETIC);
	
		// Build flags
		return new JITMethodFlags(__oc, ff);
	}
}

