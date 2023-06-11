// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Errors for {@link PencilShelf}.
 *
 * @since 2023/02/19
 */
@SquirrelJMEVendorApi
public interface PencilShelfError
{
	/** {@link IllegalArgumentException}. */
	@SquirrelJMEVendorApi
	byte ILLEGAL_ARGUMENT =
		1;
	
	/** {@link IllegalStateException}. */
	@SquirrelJMEVendorApi
	byte ILLEGAL_STATE =
		2;
	
	/** {@link java.lang.IndexOutOfBoundsException}. */
	@SquirrelJMEVendorApi
	byte INDEX_OUT_OF_BOUNDS =
		3;
}
