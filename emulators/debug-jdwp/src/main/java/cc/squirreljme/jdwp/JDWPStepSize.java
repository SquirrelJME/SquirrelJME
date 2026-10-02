// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * The stepping size when single stepping.
 *
 * @since 2021/04/28
 */
public enum JDWPStepSize
	implements JDWPHasId
{
	/** Single instruction stepping. */
	MIN(0),
	
	/** Step on line changes, if this happens and lines are available. */
	LINE(1),
	
	/* End. */
	;
	
	/** The identifier. */
	public final int id;
	
	/**
	 * Initializes the step size.
	 *
	 * @param __id The ID used.
	 * @since 2024/01/26
	 */
	JDWPStepSize(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/26
	 */
	@Override
	public int debuggerId()
	{
		return this.id;
	}
	
	/**
	 * Returns the step size for the given value.
	 * 
	 * @param __v The index to get.
	 * @return The step size for the given value.
	 * @since 2021/04/28
	 */
	public static JDWPStepSize of(int __v)
	{
		switch (__v)
		{
			case 1:		return JDWPStepSize.LINE;
			case 0:
			default:	return JDWPStepSize.MIN;
		}
	}
}
