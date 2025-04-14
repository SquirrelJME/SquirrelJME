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
 * This represents the thread model type.
 *
 * @since 2021/05/07
 */
@SquirrelJMEVendorApi
public interface ThreadModelType
{
	/** Invalid model. */
	@SquirrelJMEVendorApi
	byte INVALID =
		0;
	
	/** Single cooperatively threaded. */
	@SquirrelJMEVendorApi
	byte SINGLE_COOP_THREAD =
		1;
	
	/** Simultaneous Multi-threaded. */
	@SquirrelJMEVendorApi
	byte SIMULTANEOUS_MULTI_THREAD =
		2;
	
	/** The number of threading models. */
	@SquirrelJMEVendorApi
	byte NUM_MODELS =
		3;
}
