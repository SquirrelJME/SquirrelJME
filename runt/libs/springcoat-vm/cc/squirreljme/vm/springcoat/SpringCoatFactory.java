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

import cc.squirreljme.vm.VMFactory;

/**
 * Factory which creates instances of the SpringCoat virtual machine.
 *
 * @since 2018/11/17
 */
public class SpringCoatFactory
	extends VMFactory
{
	/**
	 * Initializes the factory.
	 *
	 * @since 2018/11/17
	 */
	public SpringCoatFactory()
	{
		super("springcoat");
	}
}

