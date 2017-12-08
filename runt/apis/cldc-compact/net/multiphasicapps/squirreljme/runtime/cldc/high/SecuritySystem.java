// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc.high;

import net.multiphasicapps.squirreljme.runtime.cldc.SecurityContext;

/**
 * This class contains the security system which is used to provide access
 * to the current chore's security context. There is no means of obtaining
 * the security context of another chore.
 *
 * @since 2017/12/08
 */
public abstract class SecuritySystem
{
	/**
	 * Returns the current security context for the current chore.
	 *
	 * System chores should always return {@link SystemSecurityContext}
	 * (although this is not completely a requirement).
	 *
	 * User chores should return a {@link ChoreSecurityContext} which inquiries
	 * the system if a given action is permitted.
	 *
	 * @return The current chore's security context.
	 * @since 2017/12/08
	 */
	public abstract SecurityContext current();
}

