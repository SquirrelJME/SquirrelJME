// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Represents a bucket which is of a standard domain.
 *
 * @since 2025/04/14
 */
@SquirrelJMEVendorApi
public interface StandardBucketType
{
	/** The data bucket. */
	@SquirrelJMEVendorApi
	byte DATA_BUCKET =
		0;
	
	/** The library bucket. */
	@SquirrelJMEVendorApi
	byte LIBRARIES_BUCKET =
		1;
	
	/** The extra bucket. */
	@SquirrelJMEVendorApi
	byte EXTRA_BUCKET =
		2;
	
	/** The number of standard buckets. */
	@SquirrelJMEVendorApi
	byte NUM_BUCKETS =
		3;
}
