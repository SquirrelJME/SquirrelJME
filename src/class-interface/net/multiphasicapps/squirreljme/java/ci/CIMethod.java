// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This represents a method.
 *
 * @since 2016/04/22
 */
public interface CIMethod
	extends CIMember<CIMethodID, CIMethodFlags>, CIAccessibleObject
{
	/**
	 * Returns the code attribute of this method or {@code null} if there is
	 * none.
	 *
	 * @return The code attribute or {@code null} if there is none.
	 * @since 2016/04/27
	 */
	public abstract CICodeAttribute code();
	
	/**
	 * Returns the native code data for this method or {@code null} if there
	 * is no native code.
	 *
	 * @return The native code data.
	 * @since 2016/05/20
	 */
	public abstract CINativeCode nativeCode();
}

