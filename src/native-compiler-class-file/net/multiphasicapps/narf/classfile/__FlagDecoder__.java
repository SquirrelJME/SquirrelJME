// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classfile;

import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCIClassFlag;
import net.multiphasicapps.narf.classinterface.NCIClassFlags;
import net.multiphasicapps.narf.classinterface.NCIFieldFlag;
import net.multiphasicapps.narf.classinterface.NCIFieldFlags;

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
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * Decodes the class flag set.
	 *
	 * @param __i The input flag field.
	 * @since 2016/04/23
	 */
	static NCIClassFlags __class(int __i)
	{
		Set<NCIClassFlag> fl = new HashSet<>();
		
		// Public?
		if (0 != (__i & 0x0001))
			fl.add(NCIClassFlag.PUBLIC);
		
		// Final?
		if (0 != (__i & 0x0010))
			fl.add(NCIClassFlag.FINAL);
		
		// Super?
		if (0 != (__i & 0x0020))
			fl.add(NCIClassFlag.SUPER);
		
		// Interface?
		if (0 != (__i & 0x0200))
			fl.add(NCIClassFlag.INTERFACE);
		
		// Synthetic?
		if (0 != (__i & 0x1000))
			fl.add(NCIClassFlag.SYNTHETIC);
		
		// Annotation?
		if (0 != (__i & 0x2000))
			fl.add(NCIClassFlag.ANNOTATION);
		
		// Enumeration?
		if (0 != (__i & 0x4000))
			fl.add(NCIClassFlag.ENUM);
		
		// Build it
		return new NCIClassFlags(fl);
	}
	
	/**
	 * Parses the flags for a field.
	 *
	 * @param __oc The outer class.
	 * @param __bits The input bits.
	 * @return The field flags.
	 * @since 2016/04/26
	 */
	static NCIFieldFlags __field(NCIClass __oc, int __bits)
	{
		// Target set
		Set<NCIFieldFlag> ff = new HashSet<>();
		
		// Enumeration?
		if (0 != (__bits & 0x4000))
			ff.add(NCIFieldFlag.ENUM);
		
		// Final?
		if (0 != (__bits & 0x0010))
			ff.add(NCIFieldFlag.FINAL);
		
		// Private?
		if (0 != (__bits & 0x0002))
			ff.add(NCIFieldFlag.PRIVATE);
		
		// Protected?
		if (0 != (__bits & 0x0004))
			ff.add(NCIFieldFlag.PROTECTED);
		
		// Public?
		if (0 != (__bits & 0x0001))
			ff.add(NCIFieldFlag.PUBLIC);
		
		// Static?
		if (0 != (__bits & 0x0008))
			ff.add(NCIFieldFlag.STATIC);
		
		// Synthetic?
		if (0 != (__bits & 0x1000))
			ff.add(NCIFieldFlag.SYNTHETIC);
		
		// Transient?
		if (0 != (__bits & 0x0080))
			ff.add(NCIFieldFlag.TRANSIENT);
		
		// Volatile?
		if (0 != (__bits & 0x0040))
			ff.add(NCIFieldFlag.VOLATILE);
		
		// Build flags
		return new NCIFieldFlags(__oc, ff);
	}
}

