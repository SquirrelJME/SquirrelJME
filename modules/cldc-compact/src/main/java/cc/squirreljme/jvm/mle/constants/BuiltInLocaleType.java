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
 * Represents a locale which is built-in.
 *
 * @since 2020/06/11
 */
@SquirrelJMEVendorApi
public interface BuiltInLocaleType
{
	/** Unspecified. */
	@SquirrelJMEVendorApi
	byte UNSPECIFIED =
		0;
	
	/** English, US. */
	@SquirrelJMEVendorApi
	byte ENGLISH_US =
		1;
}
