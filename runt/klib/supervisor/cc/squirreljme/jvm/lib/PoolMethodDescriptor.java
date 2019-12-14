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
 * This represents a method descriptor.
 *
 * @since 2019/12/14
 */
public final class PoolMethodDescriptor
{
	/** The descriptor. */
	protected final String descriptor;
	
	/** The return value. */
	protected final PoolClassName returnvalue;
	
	/** Arguments. */
	private final PoolClassName[] _args;
	
	/**
	 * Initializes the descriptor.
	 *
	 * @param __str The string to use.
	 * @param __rv The return value.
	 * @param __args The arguments.
	 * @throws NullPointerException If no string or arguments were specified.
	 * @since 2019/12/14
	 */
	public PoolMethodDescriptor(String __str, PoolClassName __rv,
		PoolClassName... __args)
		throws NullPointerException
	{
		if (__str == null || __args == null)
			throw new NullPointerException("NARG");
		
		this.descriptor = __str;
		this.returnvalue = __rv;
		
		// Copy arguments
		int numargs = __args.length;
		PoolClassName[] args = new PoolClassName[numargs];
		for (int i = 0; i < numargs; i++)
		{
			PoolClassName a = __args[i];
			if (a == null)
				throw new NullPointerException("NARG");
			
			args[i] = a;
		}
		
		this._args = args;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/14
	 */
	@Override
	public final String toString()
	{
		return this.descriptor;
	}
}

