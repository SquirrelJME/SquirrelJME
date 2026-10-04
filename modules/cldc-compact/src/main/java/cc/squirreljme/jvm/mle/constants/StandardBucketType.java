// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * Represents a bucket which is of a standard domain.
 *
 * @since 2025/04/14
 */
public interface StandardBucketType
{
	/** The data bucket. */
	byte DATA_BUCKET =
		0;
	
	/** The library bucket. */
	byte LIBRARIES_BUCKET =
		1;
	
	/** The extra bucket. */
	byte EXTRA_BUCKET =
		2;
	
	/** The number of standard buckets. */
	byte NUM_BUCKETS =
		3;
}
