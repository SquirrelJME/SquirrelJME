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
import net.multiphasicapps.squirreljme.jit.base.ClassClassFlag;
import net.multiphasicapps.squirreljme.jit.base.ClassClassFlags;
import net.multiphasicapps.squirreljme.jit.base.ClassFormatException;
import net.multiphasicapps.squirreljme.jit.base.ClassFieldFlag;
import net.multiphasicapps.squirreljme.jit.base.ClassFieldFlags;
import net.multiphasicapps.squirreljme.jit.base.ClassMethodFlag;
import net.multiphasicapps.squirreljme.jit.base.ClassMethodFlags;

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
	static ClassClassFlags __class(int __i)
	{
		Set<ClassClassFlag> fl = new HashSet<>();
		
		// Public?
		if (0 != (__i & 0x0001))
			fl.add(ClassClassFlag.PUBLIC);
		
		// Final?
		if (0 != (__i & 0x0010))
			fl.add(ClassClassFlag.FINAL);
		
		// Super?
		if (0 != (__i & 0x0020))
			fl.add(ClassClassFlag.SUPER);
		
		// Interface?
		if (0 != (__i & 0x0200))
			fl.add(ClassClassFlag.INTERFACE);
		
		// Abstract?
		if (0 != (__i & 0x0400))
			fl.add(ClassClassFlag.ABSTRACT);
		
		// Synthetic?
		if (0 != (__i & 0x1000))
			fl.add(ClassClassFlag.SYNTHETIC);
		
		// Annotation?
		if (0 != (__i & 0x2000))
			fl.add(ClassClassFlag.ANNOTATION);
		
		// Enumeration?
		if (0 != (__i & 0x4000))
			fl.add(ClassClassFlag.ENUM);
		
		// Build it
		return new ClassClassFlags(fl);
	}
	
	/**
	 * Parses the flags for a field.
	 *
	 * @param __oc The outer class.
	 * @param __bits The input bits.
	 * @return The field flags.
	 * @since 2016/04/26
	 */
	static ClassFieldFlags __field(ClassClassFlags __oc, int __bits)
	{
		// Target set
		Set<ClassFieldFlag> ff = new HashSet<>();
		
		// Enumeration?
		if (0 != (__bits & 0x4000))
			ff.add(ClassFieldFlag.ENUM);
		
		// Final?
		if (0 != (__bits & 0x0010))
			ff.add(ClassFieldFlag.FINAL);
		
		// Private?
		if (0 != (__bits & 0x0002))
			ff.add(ClassFieldFlag.PRIVATE);
		
		// Protected?
		if (0 != (__bits & 0x0004))
			ff.add(ClassFieldFlag.PROTECTED);
		
		// Public?
		if (0 != (__bits & 0x0001))
			ff.add(ClassFieldFlag.PUBLIC);
		
		// Static?
		if (0 != (__bits & 0x0008))
			ff.add(ClassFieldFlag.STATIC);
		
		// Synthetic?
		if (0 != (__bits & 0x1000))
			ff.add(ClassFieldFlag.SYNTHETIC);
		
		// Transient?
		if (0 != (__bits & 0x0080))
			ff.add(ClassFieldFlag.TRANSIENT);
		
		// Volatile?
		if (0 != (__bits & 0x0040))
			ff.add(ClassFieldFlag.VOLATILE);
		
		// Build flags
		return new ClassFieldFlags(__oc, ff);
	}
	/**
	 * Parses the flags for a method.
	 *
	 * @param __oc The outer class.
	 * @param __bits The input bits.
	 * @return The method flags.
	 * @since 2016/04/26
	 */
	static ClassMethodFlags __method(ClassClassFlags __oc, int __bits)
	{
		// Target set
		Set<ClassMethodFlag> ff = new HashSet<>();
		
		// Public method.
		if (0 != (__bits & 0x0001))
			ff.add(ClassMethodFlag.PUBLIC);
	
		// Private method.
		if (0 != (__bits & 0x0002))
			ff.add(ClassMethodFlag.PRIVATE);
	
		// Protected method.
		if (0 != (__bits & 0x0004))
			ff.add(ClassMethodFlag.PROTECTED);
	
		// Static method.
		if (0 != (__bits & 0x0008))
			ff.add(ClassMethodFlag.STATIC);
	
		// Final method.
		if (0 != (__bits & 0x0010))
			ff.add(ClassMethodFlag.FINAL);
	
		// Synchronized method.
		if (0 != (__bits & 0x0020))
			ff.add(ClassMethodFlag.SYNCHRONIZED);
	
		// Bridge method.
		if (0 != (__bits & 0x0040))
			ff.add(ClassMethodFlag.BRIDGE);
	
		// Variable argument method.
		if (0 != (__bits & 0x0080))
			ff.add(ClassMethodFlag.VARARGS);
	
		// Native method.
		if (0 != (__bits & 0x0100))
			ff.add(ClassMethodFlag.NATIVE);
	
		// Abstract method.
		if (0 != (__bits & 0x0400))
			ff.add(ClassMethodFlag.ABSTRACT);
	
		// Strict floating point method.
		if (0 != (__bits & 0x0800))
			ff.add(ClassMethodFlag.STRICT);
	
		// Synthetic method.
		if (0 != (__bits & 0x1000))
			ff.add(ClassMethodFlag.SYNTHETIC);
	
		// Build flags
		return new ClassMethodFlags(__oc, ff);
	}
}

