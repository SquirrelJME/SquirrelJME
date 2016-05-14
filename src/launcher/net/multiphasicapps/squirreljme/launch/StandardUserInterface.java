// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch;

/**
 * This class uses the kernel interface to provide a user interface.
 *
 * @since 2016/05/14
 */
public abstract class StandardUserInterface
{
	/** The launcher to really use. */
	protected final Kernel launcher;
	
	/**
	 * Initializes the launcher controller.
	 *
	 * @param __an The launcher to use for interaction.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/15
	 */
	StandardUserInterface(Kernel __an)
		throws NullPointerException
	{
		// Check
		if (__an == null)
			throw new NullPointerException("NARG");
		
		// Setup
		launcher = __an;
	}
	
	/**
	 * Indicates that controller specific updating should be performed.
	 *
	 * @since 2016/05/14
	 */
	public abstract void update();
}

