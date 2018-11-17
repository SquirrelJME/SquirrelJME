// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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

