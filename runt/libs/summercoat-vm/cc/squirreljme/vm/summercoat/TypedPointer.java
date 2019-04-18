// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This represents a typed pointer which specifies a class and pointer value.
 *
 * @since 2019/04/18
 */
public final class TypedPointer
{
	/** The class type. */
	public final LoadedClass type;
	
	/** The pointer value. */
	public final int pointer;
	
	/**
	 * Initializes the pointer.
	 *
	 * @param __t The type used.
	 * @param __p The pointer value.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/18
	 */
	public TypedPointer(LoadedClass __t, int __p)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this.type = __t;
		this.pointer = __p;
	}
}

