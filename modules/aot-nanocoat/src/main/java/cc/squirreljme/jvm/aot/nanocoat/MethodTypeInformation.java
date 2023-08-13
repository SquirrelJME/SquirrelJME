// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.classfile.MethodDescriptor;

/**
 * Contains method type information.
 *
 * @since 2023/08/13
 */
public class MethodTypeInformation
{
	/**
	 * Initializes the type information.
	 *
	 * @param __type The type used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/13
	 */
	public MethodTypeInformation(MethodDescriptor __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/13
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/13
	 */
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
}
