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

import net.multiphasicapps.descriptors.FieldSymbol;

/**
 * This represents a field.
 *
 * @since 2016/04/22
 */
public interface NCIField
	extends NCIMember<NCIFieldID, NCIFieldFlags>
{
	/**
	 * Returns the constant value of this field.
	 *
	 * @return The field constant value or {@code null} if there is none.
	 * @since 2016/04/26
	 */
	public abstract Object constantValue();
}

