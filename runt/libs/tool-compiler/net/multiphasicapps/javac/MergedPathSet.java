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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is a path set which allows multiple path sets to be merged into one
 * so that the later ones replace the earlier ones.
 *
 * @since 2018/03/06
 */
public final class MergedPathSet
	implements CompilerPathSet 
{
	/** The input sets. */
	private final CompilerPathSet[] _sets;
	
	/**
	 * Initializes the merged path set.
	 *
	 * @param __sets The sets to merge together.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/06
	 */
	public MergedPathSet(CompilerPathSet... __sets)
		throws NullPointerException
	{
		if (__sets == null)
			throw new NullPointerException("NARG");
		
		// Ensure there are no null values
		__sets = __sets.clone();
		for (CompilerPathSet cps : __sets)
			if (cps == null)
				throw new NullPointerException("NARG");
		
		this._sets = __sets;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final void close()
		throws CompilerException
	{
		CompilerException tossed = null;
		for (CompilerPathSet cps : this._sets)
			try
			{
				cps.close();
			}
			catch (CompilerException e)
			{
				// {@squirreljme.error AQ0h Could not close the merged path
				// set.}
				if (tossed == null)
					tossed = new CompilerException("AQ0h");
				
				tossed.addSuppressed(tossed);
			}
		
		if (tossed != null)
			throw tossed;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final Iterator<CompilerInput> iterator()
		throws CompilerException
	{
		// Use a map since the compiler input might not refer to the same
		// file even if the same name is used
		Map<String, CompilerInput> rv = new LinkedHashMap<>();
		
		// The latest version of an input is returned in the iterator for
		// a given name
		for (CompilerPathSet cps : this._sets)
			for (CompilerInput in : cps)
				rv.put(in.name(), in);
		
		// Return view of the map
		Collection<CompilerInput> values = rv.values();
		return Arrays.<CompilerInput>asList(
			values.<CompilerInput>toArray(new CompilerInput[values.size()])).
			iterator();
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
		
		// Go through the final sets first because those replace the earlier
		// ones
		CompilerPathSet[] sets = this._sets;
		for (int i = sets.length - 1; i >= 0; i--)
			try
			{
				return sets[i].input(__n);
			}
			catch (NoSuchInputException e)
			{
			}
		
		// {@squirreljme.error AQ0i Could not find the specified input in the
		// merged path set. (The input)}
		throw new NoSuchInputException(String.format("AQ0i %s", __n));
	}
}

