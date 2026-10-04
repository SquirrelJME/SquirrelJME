// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Represents a bucket which is of a standard domain.
 *
 * @since 2025/04/14
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface StandardBucketType
{
	/** The data bucket. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DATA_BUCKET =
		0;
	
	/** The library bucket. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte LIBRARIES_BUCKET =
		1;
	
	/** The extra bucket. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte EXTRA_BUCKET =
		2;
	
	/** The number of standard buckets. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_BUCKETS =
		3;
}
