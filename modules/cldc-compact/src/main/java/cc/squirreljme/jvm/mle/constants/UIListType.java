// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * The type of list to use.
 *
 * @since 2020/10/17
 */
@SquirrelJMEVendorApi
public interface UIListType
{
	/** Only one element may be selected at a time. */
	@SquirrelJMEVendorApi
	byte EXCLUSIVE =
		0;
	
	/**
	 * The item that is focused is always the only one selected, pressing an
	 * action key (like enter/space) will activate the item.
	 */
	@SquirrelJMEVendorApi
	byte IMPLICIT =
		1;
	
	/** Any number of items may be selected. */
	@SquirrelJMEVendorApi
	byte MULTIPLE =
		2;
	
	/** The number of list types. */
	@SquirrelJMEVendorApi
	byte NUM_LIST_TYPES =
		3;
}
