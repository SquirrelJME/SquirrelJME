// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This represents UNIX access modes which may be declared in the ZIP.
 *
 * @since 2016/08/07
 */
@SquirrelJMEVendorApi
public enum ZipUnixAccessMode
	implements ZipEntryAttribute
{
	/** User read. */
	@SquirrelJMEVendorApi
	USER_READ,
	
	/** User write. */
	@SquirrelJMEVendorApi
	USER_WRITE,
	
	/** User execute. */
	@SquirrelJMEVendorApi
	USER_EXECUTE,
	
	/** Group read. */
	@SquirrelJMEVendorApi
	GROUP_READ,
	
	/** Group write. */
	@SquirrelJMEVendorApi
	GROUP_WRITE,
	
	/** Group execute. */
	@SquirrelJMEVendorApi
	GROUP_EXECUTE,
	
	/** Other read. */
	@SquirrelJMEVendorApi
	OTHER_READ,
	
	/** Other write. */
	@SquirrelJMEVendorApi
	OTHER_WRITE,
	
	/** Other execute. */
	@SquirrelJMEVendorApi
	OTHER_EXECUTE,
	
	/** End. */
	;
}

