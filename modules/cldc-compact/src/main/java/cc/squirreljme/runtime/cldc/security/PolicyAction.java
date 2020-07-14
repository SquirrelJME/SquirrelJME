// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.security;

/**
 * This represents the action to perform for the given policy.
 *
 * @since 2020/07/09
 */
public enum PolicyAction
{
	/** Deny the policy. */
	DENY,
	
	/** Allow the policy. */
	ALLOW,
	
	/* End. */
	;
}
