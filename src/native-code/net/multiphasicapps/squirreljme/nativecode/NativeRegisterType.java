// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

/**
 * This interface is associated with basic register types.
 *
 * @since 2016/09/02
 */
public interface NativeRegisterType
{
	/**
	 * Is this a floating point type?
	 *
	 * @return {@code true} if a floating point type.
	 * @since 2016/09/09
	 */
	public abstract boolean isFloat();
	
	/**
	 * Returns the number of bytes that are needed to store the value.
	 *
	 * @return The number of bytes needed to store.
	 * @since 2016/09/09
	 */
	public abstract int bytes();
}

