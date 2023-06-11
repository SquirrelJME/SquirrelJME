// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.event;

/**
 * The stepping size when single stepping.
 *
 * @since 2021/04/28
 */
public enum StepSize
{
	/** Single instruction stepping. */
	MIN,
	
	/** Step on line changes, if this happens and lines are available. */
	LINE,
	
	/* End. */
	;
	
	/**
	 * Returns the step size for the given value.
	 * 
	 * @param __v The index to get.
	 * @return The step size for the given value.
	 * @since 2021/04/28
	 */
	public static StepSize of(int __v)
	{
		switch (__v)
		{
			case 1:		return StepSize.LINE;
			case 0:
			default:	return StepSize.MIN;
		}
	}
}
