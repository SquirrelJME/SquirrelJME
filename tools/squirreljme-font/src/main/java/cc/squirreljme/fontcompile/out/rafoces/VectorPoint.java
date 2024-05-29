// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rafoces;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents a single point vector.
 *
 * @since 2024/05/29
 */
public class VectorPoint
{
	/** Is this a hole? */
	public final boolean hole;
	
	/** The X coordinate. */
	public final int x;
	
	/** The Y coordinate. */
	public final int y;
	
	/**
	 * Initializes the vector point.
	 *
	 * @param __x The starting X position.
	 * @param __y The starting Y position.
	 * @param __hole Is this a hole?
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/29
	 */
	public VectorPoint(int __x, int __y, boolean __hole)
		throws NullPointerException
	{
		this.x = __x;
		this.y = __y;
		this.hole = __hole;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/27
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/27
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/29
	 */
	@Override
	public String toString()
	{
		return String.format("%c(%d, %d)",
			(this.hole ? 'h' : 'F'), this.x, this.y);
	}
}
