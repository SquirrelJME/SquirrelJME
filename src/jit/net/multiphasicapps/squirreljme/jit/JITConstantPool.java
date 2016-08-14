// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This provides an interface to the constant pool of a class which provides
 * a nicer interface for the output class format.
 *
 * @since 2016/08/12
 */
public final class JITConstantPool
	extends AbstractList<JITConstantEntry>
{
	/** The entry count. */
	protected final int size;
	
	/** Wrapped entries. */
	private final JITConstantEntry[] _entries;
	
	/**
	 * Initializes the constant pool representation.
	 *
	 * @param __dc The owning class decoder.
	 * @param __pool The constant pool to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/12
	 */
	JITConstantPool(__ClassDecoder__ __dc, __ClassPool__ __pool)
		throws NullPointerException
	{
		// Check
		if (__dc == null || __pool == null)
			throw new NullPointerException("NARG");
		
		// Get the configuration, which is needed for when class rewrites are
		// detected
		JITOutputConfig.Immutable config = __dc._jit.config();
		JITClassNameRewrite[] rewrites = config.classNameRewrites();
		
		// Go through all pool entries and setup wrapped entries
		List<JITConstantEntry> entries = new LinkedList<>();
		int on = __pool.size();
		for (int i = 1; i < on; i++)
		{
			throw new Error("TODO");
		}
		
		// Set
		size = entries.size();
		JITConstantEntry[] array = entries.<JITConstantEntry>toArray(
			new JITConstantEntry[size]);
		this._entries = array;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/13
	 */
	@Override
	public JITConstantEntry get(int __i)
	{
		return this._entries[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/13
	 */
	@Override
	public int size()
	{
		return this.size;
	}
}

