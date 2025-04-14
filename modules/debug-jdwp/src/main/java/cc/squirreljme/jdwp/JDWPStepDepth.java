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
 * The stepping depth used with stepping.
 *
 * @since 2021/04/28
 */
public enum JDWPStepDepth
	implements JDWPHasId
{
	/** Step into any method calls and suspend into the given frame. */
	INTO(0),
	
	/** Step over any method calls and stay on the same frame. */
	OVER(1),
	
	/** Step out of the current frame. */
	OUT(2),
	
	/* End. */
	;
	
	/** The step depth id. */
	public final int id;
	
	/**
	 * Initializes the step depth.
	 *
	 * @param __id The ID used.
	 * @since 2024/01/26
	 */
	JDWPStepDepth(int __id)
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
	 * Returns the step depth for the given value.
	 * 
	 * @param __v The index to get.
	 * @return The step depth for the given value.
	 * @since 2021/04/28
	 */
	public static JDWPStepDepth of(int __v)
	{
		switch (__v)
		{
			case 1:		return JDWPStepDepth.OVER;
			case 2:		return JDWPStepDepth.OUT;
			case 0:
			default:	return JDWPStepDepth.INTO;
		}
	}
}
