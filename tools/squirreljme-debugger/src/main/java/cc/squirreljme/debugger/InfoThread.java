// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

/**
 * Thread storage information.
 *
 * @since 2024/01/20
 */
public class InfoThread
	extends Info
{
	/** Is this thread started? */
	protected final KnownValue<Boolean> isStarted =
		new KnownValue<>(Boolean.class);
	
	/**
	 * Initializes the base thread.
	 *
	 * @param __id The ID number of this thread.
	 * @since 2024/01/20
	 */
	public InfoThread(int __id)
	{
		super(__id, InfoType.THREAD);
	}
}
