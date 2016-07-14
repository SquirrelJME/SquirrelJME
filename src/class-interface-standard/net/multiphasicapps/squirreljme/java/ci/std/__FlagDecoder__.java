// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci.std;

import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.ci.CIClass;
import net.multiphasicapps.squirreljme.java.ci.CIClassFlag;
import net.multiphasicapps.squirreljme.java.ci.CIClassFlags;
import net.multiphasicapps.squirreljme.java.ci.CIFieldFlag;
import net.multiphasicapps.squirreljme.java.ci.CIFieldFlags;
import net.multiphasicapps.squirreljme.java.ci.CIMethodFlag;
import net.multiphasicapps.squirreljme.java.ci.CIMethodFlags;

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
	static CIClassFlags __class(int __i)
	{
		Set<CIClassFlag> fl = new HashSet<>();
		
		// Public?
		if (0 != (__i & 0x0001))
			fl.add(CIClassFlag.PUBLIC);
		
		// Final?
		if (0 != (__i & 0x0010))
			fl.add(CIClassFlag.FINAL);
		
		// Super?
		if (0 != (__i & 0x0020))
			fl.add(CIClassFlag.SUPER);
		
		// Interface?
		if (0 != (__i & 0x0200))
			fl.add(CIClassFlag.INTERFACE);
		
		// Abstract?
		if (0 != (__i & 0x0400))
			fl.add(CIClassFlag.ABSTRACT);
		
		// Synthetic?
		if (0 != (__i & 0x1000))
			fl.add(CIClassFlag.SYNTHETIC);
		
		// Annotation?
		if (0 != (__i & 0x2000))
			fl.add(CIClassFlag.ANNOTATION);
		
		// Enumeration?
		if (0 != (__i & 0x4000))
			fl.add(CIClassFlag.ENUM);
		
		// Build it
		return new CIClassFlags(fl);
	}
	
	/**
	 * Parses the flags for a field.
	 *
	 * @param __oc The outer class.
	 * @param __bits The input bits.
	 * @return The field flags.
	 * @since 2016/04/26
	 */
	static CIFieldFlags __field(CIClass __oc, int __bits)
	{
		// Target set
		Set<CIFieldFlag> ff = new HashSet<>();
		
		// Enumeration?
		if (0 != (__bits & 0x4000))
			ff.add(CIFieldFlag.ENUM);
		
		// Final?
		if (0 != (__bits & 0x0010))
			ff.add(CIFieldFlag.FINAL);
		
		// Private?
		if (0 != (__bits & 0x0002))
			ff.add(CIFieldFlag.PRIVATE);
		
		// Protected?
		if (0 != (__bits & 0x0004))
			ff.add(CIFieldFlag.PROTECTED);
		
		// Public?
		if (0 != (__bits & 0x0001))
			ff.add(CIFieldFlag.PUBLIC);
		
		// Static?
		if (0 != (__bits & 0x0008))
			ff.add(CIFieldFlag.STATIC);
		
		// Synthetic?
		if (0 != (__bits & 0x1000))
			ff.add(CIFieldFlag.SYNTHETIC);
		
		// Transient?
		if (0 != (__bits & 0x0080))
			ff.add(CIFieldFlag.TRANSIENT);
		
		// Volatile?
		if (0 != (__bits & 0x0040))
			ff.add(CIFieldFlag.VOLATILE);
		
		// Build flags
		return new CIFieldFlags(__oc, ff);
	}
	/**
	 * Parses the flags for a method.
	 *
	 * @param __oc The outer class.
	 * @param __bits The input bits.
	 * @return The method flags.
	 * @since 2016/04/26
	 */
	static CIMethodFlags __method(CIClass __oc, int __bits)
	{
		// Target set
		Set<CIMethodFlag> ff = new HashSet<>();
		
		// Public method.
		if (0 != (__bits & 0x0001))
			ff.add(CIMethodFlag.PUBLIC);
	
		// Private method.
		if (0 != (__bits & 0x0002))
			ff.add(CIMethodFlag.PRIVATE);
	
		// Protected method.
		if (0 != (__bits & 0x0004))
			ff.add(CIMethodFlag.PROTECTED);
	
		// Static method.
		if (0 != (__bits & 0x0008))
			ff.add(CIMethodFlag.STATIC);
	
		// Final method.
		if (0 != (__bits & 0x0010))
			ff.add(CIMethodFlag.FINAL);
	
		// Synchronized method.
		if (0 != (__bits & 0x0020))
			ff.add(CIMethodFlag.SYNCHRONIZED);
	
		// Bridge method.
		if (0 != (__bits & 0x0040))
			ff.add(CIMethodFlag.BRIDGE);
	
		// Variable argument method.
		if (0 != (__bits & 0x0080))
			ff.add(CIMethodFlag.VARARGS);
	
		// Native method.
		if (0 != (__bits & 0x0100))
			ff.add(CIMethodFlag.NATIVE);
	
		// Abstract method.
		if (0 != (__bits & 0x0400))
			ff.add(CIMethodFlag.ABSTRACT);
	
		// Strict floating point method.
		if (0 != (__bits & 0x0800))
			ff.add(CIMethodFlag.STRICT);
	
		// Synthetic method.
		if (0 != (__bits & 0x1000))
			ff.add(CIMethodFlag.SYNTHETIC);
	
		// Build flags
		return new CIMethodFlags(__oc, ff);
	}
}

