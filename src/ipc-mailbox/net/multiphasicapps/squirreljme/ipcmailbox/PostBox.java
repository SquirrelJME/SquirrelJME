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
 * This is a mailbox which is part of a post office (think PO Box), a mailbox
 * keeps a queue of letters which may be read off as a queue.
 *
 * @since 2016/10/13
 */
public final class PostBox
{
	/** The owning post office. */
	protected final PostOffice postoffice;
	
	/**
	 * Initializes the mailbox.
	 *
	 * @param __po The owning post office.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/13
	 */
	PostBox(PostOffice __po)
		throws NullPointerException
	{
		// Check
		if (__po == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.postoffice = __po;
	}
}

