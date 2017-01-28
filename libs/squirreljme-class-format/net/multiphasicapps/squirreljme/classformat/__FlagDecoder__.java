// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.linkage.ClassFlag;
import net.multiphasicapps.squirreljme.linkage.ClassFlags;
import net.multiphasicapps.squirreljme.linkage.FieldFlag;
import net.multiphasicapps.squirreljme.linkage.FieldFlags;
import net.multiphasicapps.squirreljme.linkage.InvalidFlagsException;
import net.multiphasicapps.squirreljme.linkage.MethodFlag;
import net.multiphasicapps.squirreljme.linkage.MethodFlags;

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
	static ClassFlags __class(int __i)
	{
		Set<ClassFlag> fl = new HashSet<>();
		
		// Public?
		if (0 != (__i & 0x0001))
			fl.add(ClassFlag.PUBLIC);
		
		// Final?
		if (0 != (__i & 0x0010))
			fl.add(ClassFlag.FINAL);
		
		// Super?
		if (0 != (__i & 0x0020))
			fl.add(ClassFlag.SUPER);
		
		// Interface?
		if (0 != (__i & 0x0200))
			fl.add(ClassFlag.INTERFACE);
		
		// Abstract?
		if (0 != (__i & 0x0400))
			fl.add(ClassFlag.ABSTRACT);
		
		// Synthetic?
		if (0 != (__i & 0x1000))
			fl.add(ClassFlag.SYNTHETIC);
		
		// Annotation?
		if (0 != (__i & 0x2000))
			fl.add(ClassFlag.ANNOTATION);
		
		// Enumeration?
		if (0 != (__i & 0x4000))
			fl.add(ClassFlag.ENUM);
		
		// Build it
		try
		{
			return new ClassFlags(fl);
		}
		
		// {@squirreljme.error AY01 Invalid class flags.}
		catch (InvalidFlagsException e)
		{
			throw new ClassFormatException("AY01", e);
		}
	}
	
	/**
	 * Parses the flags for a field.
	 *
	 * @param __oc The outer class.
	 * @param __bits The input bits.
	 * @return The field flags.
	 * @since 2016/04/26
	 */
	static FieldFlags __field(ClassFlags __oc, int __bits)
	{
		// Target set
		Set<FieldFlag> ff = new HashSet<>();
		
		// Enumeration?
		if (0 != (__bits & 0x4000))
			ff.add(FieldFlag.ENUM);
		
		// Final?
		if (0 != (__bits & 0x0010))
			ff.add(FieldFlag.FINAL);
		
		// Private?
		if (0 != (__bits & 0x0002))
			ff.add(FieldFlag.PRIVATE);
		
		// Protected?
		if (0 != (__bits & 0x0004))
			ff.add(FieldFlag.PROTECTED);
		
		// Public?
		if (0 != (__bits & 0x0001))
			ff.add(FieldFlag.PUBLIC);
		
		// Static?
		if (0 != (__bits & 0x0008))
			ff.add(FieldFlag.STATIC);
		
		// Synthetic?
		if (0 != (__bits & 0x1000))
			ff.add(FieldFlag.SYNTHETIC);
		
		// Transient?
		if (0 != (__bits & 0x0080))
			ff.add(FieldFlag.TRANSIENT);
		
		// Volatile?
		if (0 != (__bits & 0x0040))
			ff.add(FieldFlag.VOLATILE);
		
		// Build flags
		try
		{
			return new FieldFlags(__oc, ff);
		}
		
		// {@squirreljme.error AY02 Invalid field flags.}
		catch (InvalidFlagsException e)
		{
			throw new ClassFormatException("AY02", e);
		}
	}
	/**
	 * Parses the flags for a method.
	 *
	 * @param __oc The outer class.
	 * @param __bits The input bits.
	 * @return The method flags.
	 * @since 2016/04/26
	 */
	static MethodFlags __method(ClassFlags __oc, int __bits)
	{
		// Target set
		Set<MethodFlag> ff = new HashSet<>();
		
		// Public method.
		if (0 != (__bits & 0x0001))
			ff.add(MethodFlag.PUBLIC);
	
		// Private method.
		if (0 != (__bits & 0x0002))
			ff.add(MethodFlag.PRIVATE);
	
		// Protected method.
		if (0 != (__bits & 0x0004))
			ff.add(MethodFlag.PROTECTED);
	
		// Static method.
		if (0 != (__bits & 0x0008))
			ff.add(MethodFlag.STATIC);
	
		// Final method.
		if (0 != (__bits & 0x0010))
			ff.add(MethodFlag.FINAL);
	
		// Synchronized method.
		if (0 != (__bits & 0x0020))
			ff.add(MethodFlag.SYNCHRONIZED);
	
		// Bridge method.
		if (0 != (__bits & 0x0040))
			ff.add(MethodFlag.BRIDGE);
	
		// Variable argument method.
		if (0 != (__bits & 0x0080))
			ff.add(MethodFlag.VARARGS);
	
		// Native method.
		if (0 != (__bits & 0x0100))
			ff.add(MethodFlag.NATIVE);
	
		// Abstract method.
		if (0 != (__bits & 0x0400))
			ff.add(MethodFlag.ABSTRACT);
	
		// Strict floating point method.
		if (0 != (__bits & 0x0800))
			ff.add(MethodFlag.STRICT);
	
		// Synthetic method.
		if (0 != (__bits & 0x1000))
			ff.add(MethodFlag.SYNTHETIC);
	
		// Build flags
		try
		{
			return new MethodFlags(__oc, ff);
		}
		
		// {@squirreljme.error AY03 Invalid method flags.}
		catch (InvalidFlagsException e)
		{
			throw new ClassFormatException("AY03", e);
		}
	}
}

