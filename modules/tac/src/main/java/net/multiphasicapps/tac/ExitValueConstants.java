// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Constants used to define what happens when an exit value is given.
 *
 * @since 2020/03/07
 */
@SquirrelJMEVendorApi
public interface ExitValueConstants
{
	/** Test passes. */
	byte SUCCESS =
		0;
	
	/** Test fails. */
	byte FAILURE =
		1;
	
	/** Test skipped. */
	byte SKIPPED =
		2;
}
