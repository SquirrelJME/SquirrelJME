// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Builder for struct types.
 *
 * @since 2023/06/06
 */
public final class CStructTypeBuilder
{
	
	/**
	 * Builds the structure type.
	 * 
	 * @return The resultant struct.
	 * @since 2023/06/12
	 */
	public CStructType build()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Adds a struct member.
	 * 
	 * @param __type The member type.
	 * @param __name The member name.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/12
	 */
	public CStructTypeBuilder member(CType __type, String __name)
		throws NullPointerException
	{
		return this.member(__type, CIdentifier.of(__name));
	}
	
	/**
	 * Adds a struct member.
	 * 
	 * @param __type The member type.
	 * @param __name The member name.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/12
	 */
	public CStructTypeBuilder member(CType __type, CIdentifier __name)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
}
