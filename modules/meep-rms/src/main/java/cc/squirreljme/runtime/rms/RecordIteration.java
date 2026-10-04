// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.jvm.suite.SuiteIdentifier;

/**
 * Represents a single iteration to allow access over
 * a {@link RecordStoreSession}.
 *
 * @since 2025/04/23
 */
public class RecordIteration
{
	/** THe bucket this is in. */
	public final BucketBracket bucket;
	
	/** The base name for the record files. */
	public final String baseName;
	
	/** The owner of the record. */
	public final SuiteIdentifier owner;
	
	/** The name of the record. */
	public final String name;
	
	public RecordIteration(BucketBracket __bucket, String __baseName,
		SuiteIdentifier __owner, String __name)
		throws NullPointerException
	{
		this.bucket = __bucket;
		this.baseName = __baseName;
		this.owner = __owner;
		this.name = __name;
	}
}
