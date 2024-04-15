// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

/**
 * This is a bidirectional pipe which allows for sending and receiving on both
 * ends.
 *
 * @since 2024/01/19
 */
public class BidirectionalPipe
{
	/** A to B. */
	protected final UnidirectionalPipe aToB =
		new UnidirectionalPipe();
	
	/** B to A. */
	protected final UnidirectionalPipe bToA =
		new UnidirectionalPipe();
	
	/**
	 * Returns a given side of the pipe.
	 *
	 * @param __b Should the second end be returned?
	 * @return The resultant side of the pipe.
	 * @since 2024/01/19
	 */
	public BidirectionalPipeSide side(boolean __b)
	{
		// Get both sides
		UnidirectionalPipe aToB = this.aToB;
		UnidirectionalPipe bToA = this.bToA;
		
		// Both pipe ends are crossed
		if (!__b)
			return new BidirectionalPipeSide(bToA.in(), aToB.out());
		return new BidirectionalPipeSide(aToB.in(), bToA.out());
	}
}
