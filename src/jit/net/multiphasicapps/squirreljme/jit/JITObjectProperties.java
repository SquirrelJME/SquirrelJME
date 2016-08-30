// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This is used to provide access to extra properties which may be defined
 * by a special object associated with a configuration.
 *
 * This may be used for example to specify which registers are used and
 * allocated so that information can be placed in the global system property
 * table.
 *
 * @since 2016/08/30
 */
public interface JITObjectProperties
{
	/**
	 * Returns the properties which are associated with this object.
	 *
	 * @return An array of object properties to add to the output system
	 * properties, the elements are in pairs where the first is the key and
	 * the second is the value.
	 * @since 2016/08/30
	 */
	public abstract String[] properties();
}

