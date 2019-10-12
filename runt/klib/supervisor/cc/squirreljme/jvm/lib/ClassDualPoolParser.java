// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

/**
 * This class manages the parsing of the standard individual constant pools
 * to the combined run-time and static dual-pool setup.
 *
 * @see ClassPoolParser
 * @since 2019/10/12
 */
public final class ClassDualPoolParser
{
	/** The class level static pool. */
	protected final ClassPoolParser classpool;
	
	/** The run-time pool. */
	protected final ClassPoolParser runtimepool;
	
	/**
	 * Initializes the dual class pool parser.
	 *
	 * @param __cl The static class pool.
	 * @param __rt The run-time class pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/12
	 */
	public ClassDualPoolParser(ClassPoolParser __cl, ClassPoolParser __rt)
		throws NullPointerException
	{
		if (__cl == null || __rt == null)
			throw new NullPointerException("NARG");
		
		this.classpool = __cl;
		this.runtimepool = __rt;
	}
}

