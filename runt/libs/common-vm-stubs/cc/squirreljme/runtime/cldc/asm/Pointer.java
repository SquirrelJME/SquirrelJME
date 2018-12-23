// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

/**
 * This represents a pointer type and contains a pointer value.
 *
 * This class represents a virtual object which is replaced by the compiler to
 * represent a native pointer.
 *
 * @since 2017/12/27
 */
public final class Pointer
{
	/**
	 * Not used.
	 *
	 * @since 2017/12/27
	 */
	private Pointer()
	{
	}
	
	/**
	 * This translates an address represented in the given long value to a
	 * pointer address.
	 *
	 * @param __a The address to translate.
	 * @return The pointer of that address.
	 * @since 2017/12/27
	 */
	public static native Pointer longToPointer(long __a);
}

