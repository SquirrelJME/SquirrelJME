// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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
}

