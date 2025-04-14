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
 * Specifies the byte order that is used.
 *
 * @since 2021/02/09
 */
@SquirrelJMEVendorApi
public interface ByteOrderType
{
	/** Big endian. */
	@SquirrelJMEVendorApi
	byte BIG_ENDIAN =
		0;
		
	/** Little endian. */
	@SquirrelJMEVendorApi
	byte LITTLE_ENDIAN =
		1;
}
