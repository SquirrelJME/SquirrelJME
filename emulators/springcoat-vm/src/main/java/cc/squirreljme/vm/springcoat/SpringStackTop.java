// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * Represents the top of a long or double.
 *
 * @since 2018/09/09
 */
public final class SpringStackTop
{
	/** The top element representation. */
	public static final SpringStackTop TOP =
		new SpringStackTop();
	
	/**
	 * Internally used.
	 *
	 * @since 2018/09/09
	 */
	private SpringStackTop()
	{
	}
}

