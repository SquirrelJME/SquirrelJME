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

import net.multiphasicapps.squirreljme.runtime.cldc.high.SecuritySystem;
import net.multiphasicapps.squirreljme.runtime.cldc.SecurityContext;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemSecurityContext;

/**
 * This provides the security system for the host Java SE environment.
 *
 * @since 2017/12/08
 */
public class JavaSecuritySystem
	extends SecuritySystem
{
	/** Always use the same context. */
	protected final SecurityContext context =
		new SystemSecurityContext();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public SecurityContext current()
	{
		return this.context;
	}
}

