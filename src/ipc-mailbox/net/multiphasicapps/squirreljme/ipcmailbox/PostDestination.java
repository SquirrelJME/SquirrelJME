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

import net.multiphasicapps.squirreljme.midletid.MidletVersion;

/**
 * This is used as a class which identifies destinations where post offices
 * can be setup for communication.
 *
 * @since 2016/10/13
 */
public final class PostDestination
	extends PostBase
{
	/** The name of the server. */
	protected final String name;
	
	/** The version the server uses. */
	protected final MidletVersion version;
	
	/** Authorized mode? */
	protected final boolean authmode;
	
	/**
	 * Initializes the post destination.
	 *
	 * @since 2016/10/13
	 */
	public PostDestination(String __name, MidletVersion __ver, boolean __am)
		throws NullPointerException
	{
		// Check
		if (__name == null || __ver == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.version = __ver;
		this.authmode = __am;
	}
}

