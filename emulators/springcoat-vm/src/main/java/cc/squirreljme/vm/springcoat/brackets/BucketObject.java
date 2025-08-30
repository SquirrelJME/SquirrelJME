// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;

/**
 * Represents a {@link BucketBracket}.
 *
 * @since 2025/04/25
 */
public class BucketObject
	extends AbstractGhostObject
{
	/** The bucket this wraps. */
	public final BucketBracket bucket;
	
	/**
	 * Initializes the bucket wrapper.
	 *
	 * @param __machine The machine used.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/25
	 */
	public BucketObject(SpringMachine __machine, BucketBracket __wrapped)
		throws NullPointerException
	{
		super(__machine, BucketBracket.class);
		
		this.bucket = __wrapped;
	}
}
