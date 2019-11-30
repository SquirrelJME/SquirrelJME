// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import cc.squirreljme.jvm.Constants;

/**
 * This is a utility which allows access to the various fields within
 * {@link cc.squirreljme.jvm.ClassInfo}.
 *
 * @since 2019/11/30
 */
public final class ClassInfoUtility
{
	/** The allocation size. */
	protected final int allocationsize;
	
	/**
	 * Initializes the class info utility.
	 *
	 * @param __as The allocation size
	 * @since 2019/11/30
	 */
	public ClassInfoUtility(int __as)
	{
		this.allocationsize = __as;
	}
	
	/**
	 * Returns the allocation size of instances of this class.
	 *
	 * @return The allocation size of this.
	 * @since 2019/11/30
	 */
	public final int allocationSize()
	{
		return this.allocationsize;
	}
	
	/**
	 * Initializes the {@code ClassInfo} utility.
	 *
	 * @param __cfp The class info parser to use.
	 * @return The utility for {@code ClassInfo}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/30
	 */
	public static final ClassInfoUtility of(ClassFileParser __cfp)
		throws NullPointerException
	{
		if (__cfp == null)
			throw new NullPointerException("NARG");
		
		// Allocation size
		int as = Constants.OBJECT_BASE_SIZE + __cfp.fieldSize(false);
		
		// Initialize now
		return new ClassInfoUtility(as);
	}
}

