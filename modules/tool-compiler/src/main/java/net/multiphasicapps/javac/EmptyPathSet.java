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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import net.multiphasicapps.collections.EmptyIterator;

/**
 * This is the empty path set and is used to provide no actual files, it is
 * the default path set used for input.
 *
 * @since 2017/11/28
 */
public final class EmptyPathSet
	implements CompilerPathSet
{
	/** The cache for the path set. */
	private static Reference<EmptyPathSet> _SET;
	
	/**
	 * Not publically created.
	 *
	 * @since 2017/11/28
	 */
	private EmptyPathSet()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public void close()
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public CompilerInput input(String __n)
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.erorr AQ06 Cannot obtain the specified file from
		// the empty path set. (The name of the file)}
		throw new NoSuchInputException(String.format("AQ06 %s", __n));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public Iterator<CompilerInput> iterator()
	{
		return EmptyIterator.<CompilerInput>empty();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public String toString()
	{
		return "EmptyPathSet";
	}
	
	/**
	 * Returns the same instance of the empty path set.
	 *
	 * @return The same instance of the empty path set.
	 * @since 2017/11/28
	 */
	public static final EmptyPathSet instance()
	{
		Reference<EmptyPathSet> ref = EmptyPathSet._SET;
		EmptyPathSet rv;
		
		if (ref == null || null == (rv = ref.get()))
			EmptyPathSet._SET = new WeakReference<>((rv = new EmptyPathSet()));
		
		return rv;
	}
}

