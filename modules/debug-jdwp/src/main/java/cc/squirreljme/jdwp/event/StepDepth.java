// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.event;

/**
 * The stepping depth used with stepping.
 *
 * @since 2021/04/28
 */
public enum StepDepth
{
	/** Step into any method calls and suspend into the given frame. */
	INTO,
	
	/** Step over any method calls and stay on the same frame. */
	OVER,
	
	/** Step out of the current frame. */
	OUT,
	
	/* End. */
	;
	
	/**
	 * Returns the step depth for the given value.
	 * 
	 * @param __v The index to get.
	 * @return The step depth for the given value.
	 * @since 2021/04/28
	 */
	public static StepDepth of(int __v)
	{
		switch (__v)
		{
			case 1:		return StepDepth.OVER;
			case 2:		return StepDepth.OUT;
			case 0:
			default:	return StepDepth.INTO;
		}
	}
}
