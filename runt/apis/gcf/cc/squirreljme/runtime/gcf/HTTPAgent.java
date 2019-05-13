// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

/**
 * This class manages the HTTP connection data and is able to encode and
 * establish a connection.
 *
 * @since 2019/05/13
 */
public final class HTTPAgent
	implements HTTPSignalListener
{
	/** The remote address. */
	protected final HTTPAddress address;
	
	/** The state tracker. */
	protected final HTTPStateTracker tracker;
	
	/**
	 * Initializes the HTTP agent.
	 *
	 * @param __addr The address.
	 * @param __t The state tracker.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	public HTTPAgent(HTTPAddress __addr, HTTPStateTracker __t)
		throws NullPointerException
	{
		if (__addr == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.address = __addr;
		this.tracker = __t;
	}	
}

