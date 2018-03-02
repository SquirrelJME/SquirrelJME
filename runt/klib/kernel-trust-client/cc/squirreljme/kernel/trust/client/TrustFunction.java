// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.trust.client;

/**
 * Functions for the trust manager.
 *
 * @since 2018/01/18
 */
public enum TrustFunction
{
	/** Obtain an untrusted trust. */
	GET_UNTRUSTED_TRUST,
	
	/** Check if the trust exists by its id. */
	CHECK_TRUST_ID,
	
	/** End. */
	;
}

