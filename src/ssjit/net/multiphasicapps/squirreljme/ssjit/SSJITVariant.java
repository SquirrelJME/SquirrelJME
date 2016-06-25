// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit;

/**
 * This interface is used in the specification of variants. A variant may
 * change how code is generated slightly by providing an alternative means of
 * code generation depending on the variant.
 *
 * @since 2016/06/25
 */
public interface SSJITVariant
{
	/**
	 * Returns the name of the variant.
	 *
	 * @return The variant name.
	 * @since 2016/06/25
	 */
	public abstract String variant();
}

