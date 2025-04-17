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
	
	/**
	 * Overwrites data within the file but does not allow extra data to be
	 * added at the end, files remain a constant size. Writes outside will
	 * be silently discarded.
	 */
	@SquirrelJMEVendorApi
	byte OVERWRITE_DISCARD =
		1;
	
	/**
	 * If writing exceeds the end of the file, it will be expanded to include
	 * the extra data. Data before the end of file can be overwritten.
	 */
	@SquirrelJMEVendorApi
	byte APPEND =
		2;
	
	/** Similar to append except that earlier data cannot be overwritten. */
	@SquirrelJMEVendorApi
	byte APPEND_ONLY =
		3;
	
	/** The number of bucket writing modes. */
	@SquirrelJMEVendorApi
	byte NUM_MODES =
		4;
}
