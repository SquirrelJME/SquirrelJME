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
 * Specifies the write mode for a bucket.
 *
 * @since 2025/04/16
 */
@SquirrelJMEVendorApi
public interface BucketWriteMode
{
	/**
	 * Overwrites data within the file but does not allow extra data to be
	 * added at the end, files remain a constant size. Writes outside will
	 * cause the write to fail.
	 */
	@SquirrelJMEVendorApi
	byte OVERWRITE =
		0;
	
	/** The data in the file is replaced, the offset must be at zero. */
	@SquirrelJMEVendorApi
	byte TRUNCATE =
		1;
	
	/** The number of bucket writing modes. */
	@SquirrelJMEVendorApi
	byte NUM_MODES =
		2;
}
