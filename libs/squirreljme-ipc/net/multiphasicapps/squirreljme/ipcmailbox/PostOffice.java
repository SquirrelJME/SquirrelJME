// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ipcmailbox;

/**
 * The post office contains two mailboxes which allows letters to be pushed
 * to each other's box.
 *
 * @since 2016/10/13
 */
public final class PostOffice
	extends PostBase
{
	/** Server mailbox. */
	protected final PostBox serverbox;
	
	/** Client mailbox. */
	protected final PostBox clientbox;
	
	/**
	 * Initializes the post office.
	 *
	 * @since 2016/10/13
	 */
	public PostOffice()
	{
		// Setup mailboxes
		this.serverbox = new PostBox(this);
		this.clientbox = new PostBox(this);
	}
	
	/**
	 * Returns the client mailbox.
	 *
	 * @return The cient mailbox.
	 * @since 2016/10/13
	 */
	public PostBox client()
	{
		return this.clientbox;
	}
	
	/**
	 * Returns the server mailbox.
	 *
	 * @return The server mailbox.
	 * @since 2016/10/13
	 */
	public PostBox server()
	{
		return this.serverbox;
	}
	
	/**
	 * Returns the post box on the other side of the post office.
	 *
	 * @param __o The box to get the opposite side of.
	 * @return The box on the other side of the post office.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	final PostBox __otherBox(PostBox __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Return which one?
		PostBox serverbox = this.serverbox, clientbox = this.clientbox;
		if (__o == serverbox)
			return clientbox;
		else if (__o == clientbox)
			return serverbox;
		
		// Should not occur, but it could
		else
			throw new RuntimeException("OOPS");
	}
}

