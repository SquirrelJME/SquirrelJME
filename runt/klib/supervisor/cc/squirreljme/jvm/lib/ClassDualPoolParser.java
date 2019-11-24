// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import cc.squirreljme.jvm.io.BinaryBlob;

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
	protected final AbstractPoolParser classpool;
	
	/** The run-time pool. */
	protected final AbstractPoolParser runtimepool;
	
	/**
	 * Initializes the dual class pool parser.
	 *
	 * @param __cl The static class pool.
	 * @param __rt The run-time class pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/12
	 */
	public ClassDualPoolParser(AbstractPoolParser __cl,
		AbstractPoolParser __rt)
		throws NullPointerException
	{
		if (__cl == null || __rt == null)
			throw new NullPointerException("NARG");
		
		this.classpool = __cl;
		this.runtimepool = __rt;
	}
	
	/**
	 * Returns the decoded entry as any object.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The value.
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/24
	 */
	public final Object entry(boolean __rt, int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		// The zero index is always null!
		if (__dx == 0)
			return null;
		
		// Need these pools
		AbstractPoolParser sp = this.classpool,
			rp = this.runtimepool,
			ap = (__rt ? rp : sp);
		
		todo.DEBUG.note("Zero = %d", ap.entryType(0));
		
		// Depends on the entry type
		int etype = ap.entryType(__dx);
		switch (etype)
		{
			default:
				throw new todo.TODO("Pool " + etype);
		}
	}
	
	/**
	 * Returns the decoded entry as a string.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The value.
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/24
	 */
	public final String entryAsString(boolean __rt, int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return (String)this.entry(__rt, __dx);
	}
}

