// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

/**
 * This represents the type of sections which exist.
 *
 * @since 2017/06/27
 */
public final class SectionType
	implements Comparable<SectionType>
{
	/** The text section. */
	public static final SectionType TEXT =
		new SectionType("text", 0);
	
	/** The data section. */
	public static final SectionType DATA =
		new SectionType("data", 0);
	
	/**
	 * Initializes the section type.
	 *
	 * @param __s The section name.
	 * @param __i The index of the section.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/28
	 */
	public SectionType(String __s, int __i)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/28
	 */
	@Override
	public int compareTo(SectionType __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/28
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/28
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/28
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

