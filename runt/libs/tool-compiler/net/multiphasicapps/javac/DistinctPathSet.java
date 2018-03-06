// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.util.Arrays;
import java.util.Iterator;

/**
 * This is a path set which uses distinct compiler inputs to provide input
 * for the compiler.
 *
 * @since 2018/03/06
 */
public final class DistinctPathSet
	implements CompilerPathSet
{
	/** The inputs. */
	private final CompilerInput[] _input;
	
	/**
	 * Initializes the distinct path set.
	 *
	 * @param __in The inputs to use for the path set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public DistinctPathSet(CompilerInput... __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Ensure there are no null values
		__in = __in.clone();
		for (CompilerInput ci : __in)
			if (ci == null)
				throw new NullPointerException("NARG");
		
		this._input = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final void close()
		throws CompilerException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final Iterator<CompilerInput> iterator()
		throws CompilerException
	{
		return Arrays.<CompilerInput>asList(this._input).iterator();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final CompilerInput input(String __n)
		throws CompilerException, NoSuchInputException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		for (CompilerInput ci : this._input)
			if (__n.equals(ci.name()))
				return ci;
		
		// {@squirreljme.error AQ0j Could not find the specified input in the
		// distinct path set. (The input)}
		throw new NoSuchInputException(String.format("AQ0j %s", __n));
	}
}

