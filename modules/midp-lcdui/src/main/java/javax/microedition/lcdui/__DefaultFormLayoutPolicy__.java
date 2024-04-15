// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is the default layout policy for a single form.
 *
 * @since 2021/11/26
 */
final class __DefaultFormLayoutPolicy__
	extends FormLayoutPolicy
{
	/**
	 * Initializes the default form layout policy.
	 * 
	 * @param __form The form this is for.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/26
	 */
	__DefaultFormLayoutPolicy__(Form __form)
		throws NullPointerException
	{
		super(__form);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/26
	 */
	@Override
	protected void doLayout(int __viewportX, int __viewportY,
		int __viewportW, int __viewportH, int[] __totalSize)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/26
	 */
	@Override
	protected Item getTraverse(Item __i, int __dir)
	{
		throw Debugging.todo();
	}
}
