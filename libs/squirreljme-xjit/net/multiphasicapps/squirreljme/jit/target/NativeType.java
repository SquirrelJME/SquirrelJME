// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This represents a native type which does not have a definite information
 * type that is stored for a given system and as such will vary for each
 * target. It is highly recommended that the native type map to the CPU's
 * native representation of data.
 *
 * Sub-classes must implement {@code equals} and {@code hashCode} for the
 * correct operation of this class.
 *
 * @since 2017/05/29
 */
public interface NativeType
{
	/**
	 * {@inheritDoc}
	 * @since 2017/05/29
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/29
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * Is this type a pointer?
	 *
	 * @return {@code true} if it is a pointer.
	 * @since 2017/05/29
	 */
	public abstract boolean isPointer();
}

