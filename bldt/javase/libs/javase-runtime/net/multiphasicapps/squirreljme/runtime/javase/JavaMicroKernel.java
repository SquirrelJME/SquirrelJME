// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import net.multiphasicapps.squirreljme.runtime.cldc.ukernel.MicroKernel;

/**
 * This implements the microkernel which is used on the initial Java SE
 * process and not the client processes.
 *
 * @since 2017/12/08
 */
public class JavaMicroKernel
	extends MicroKernel
{
	/**
	 * Initializes the microkernel.
	 *
	 * @since 2017/12/08
	 */
	JavaMicroKernel()
	{
	}
}

