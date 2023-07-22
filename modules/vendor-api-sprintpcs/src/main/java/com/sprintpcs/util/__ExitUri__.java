// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sprintpcs.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import javax.microedition.midlet.MIDlet;

/**
 * Not Described.
 *
 * @since 2022/08/28
 */
final class __ExitUri__
	implements AutoCloseable
{
	/** The URI to visit on close. */
	protected final String uri;
	
	/**
	 * Exits to the given URI.
	 * 
	 * @param __uri The URI to go to.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/20
	 */
	public __ExitUri__(String __uri)
		throws NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		this.uri = __uri;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/28
	 */
	@Override
	public void close()
	{
		MIDlet midlet = ActiveMidlet.optional();
		
		// Try to launch the URI
		if (midlet != null)
			try
			{
				midlet.platformRequest(this.uri);
			}
			catch (Exception ignored)
			{
			}
	}
}
