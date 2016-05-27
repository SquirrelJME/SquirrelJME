// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

/**
 * This interface is used for interfaces and classes which implement executable
 * code for a single method.
 *
 * @since 2016/05/20
 */
public interface NCIExecutable
{
	/**
	 * Returns the method which owns this native code.
	 *
	 * @return The containing method.
	 * @since 2016/05/20
	 */
	public abstract NCIMethod method();
}

