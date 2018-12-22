// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.build.vm;

import cc.squirreljme.builder.support.vm.VMMain;

/**
 * Compatibility entry point.
 *
 * @since 2018/12/22
 */
public class Main
{
	/**
	 * Compatible entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2018/12/22
	 */
	public static void main(String... __args)
	{
		VMMain.main(__args);
	}
}

