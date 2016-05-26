// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath;

/**
 * This is a provider for {@link ClassUnit}s which are then constructed into
 * {@link ClassPath}s used for execution.
 *
 * If a class unit does not change, then it must use the same reference.
 *
 * @since 2016/05/25
 */
public interface ClassUnitProvider
{
	/**
	 * Returns the array of class units which are available by this provider.
	 *
	 * @return The array of class units.
	 * @since 2016/05/25
	 */
	public abstract ClassUnit[] classUnits();
}

