// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

/**
 * This is thrown when interpreter classes which belong to different engines
 * are mismatced, classes are to be used only to their specific engine.
 *
 * @since 2016/03/05
 */
public class MismatchedEngineException
	extends JVMEngineException
{
	/**
	 * Initializes exception with no message.
	 *
	 * @since 2016/03/05
	 */
	public MismatchedEngineException()
	{
		super();
	}
}

