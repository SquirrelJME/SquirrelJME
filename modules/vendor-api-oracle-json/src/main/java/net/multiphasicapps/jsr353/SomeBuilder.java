// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

/**
 * This represents some kind of builder for single stack purposes.
 *
 * @since 2014/08/02
 */
public class SomeBuilder
{
	/** Lock for builder. */
	protected final Object lock;
	
	/**
	 * Initializes the locking object.
	 *
	 * @since 2014/08/05
	 */
	protected SomeBuilder()
	{
		// Init lock
		this.lock = new Object();
	}
}

