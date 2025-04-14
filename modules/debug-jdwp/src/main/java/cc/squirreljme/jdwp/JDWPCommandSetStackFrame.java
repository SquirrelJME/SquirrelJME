// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Command set for stack frames.
 *
 * @since 2021/03/15
 */
public enum JDWPCommandSetStackFrame
	implements JDWPCommand
{
	/** Get stack frame values. */
	GET_VALUES(1),
	
	/** The current object of the current frame. */
	THIS_OBJECT(3),
		
	/* End. */
	;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/15
	 */
	JDWPCommandSetStackFrame(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/15
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
