// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
