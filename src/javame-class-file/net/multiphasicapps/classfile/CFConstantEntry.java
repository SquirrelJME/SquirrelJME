// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represents the base class for all constant pool entries.
 *
 * @since 2016/03/15
 */
public abstract class CFConstantEntry
	implements CFConstantEntryKind
{
	/** The owning pool. */
	protected final CFConstantPool pool;
	
	/**
	 * Initializes the base of an entry.
	 *
	 * @param __icp The owning constant pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/15
	 */
	CFConstantEntry(CFConstantPool __icp)
		throws NullPointerException
	{
		// Check
		if (__icp == null)
			throw new NullPointerException("NARG");
		
		// Set
		pool = __icp;
	}
	
	/**
	 * Checks the range of a reference to make sure it is within bounds of
	 * an existing entry.
	 *
	 * @param __v The index to check the range for.
	 * @return {@code __v} if the range is valid.
	 * @throws CFFormatException If the range is not valid.
	 * @since 2016/03/15
	 */
	int __rangeCheck(int __v)
		throws CFFormatException
	{
		if (__v > 0 && __v < pool.size())
			return __v;
		
		// {@squirreljme.error CF0d A constant pool entry references an index
		// which is outside of the constant pool.
		// (The input value; The size of the pool)}
		throw new CFFormatException(String.format("CF0d %d %d", __v,
			pool.size()));
	}
}

