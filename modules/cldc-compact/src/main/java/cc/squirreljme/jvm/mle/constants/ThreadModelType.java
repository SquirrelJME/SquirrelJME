// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * This represents the thread model type.
 *
 * @since 2021/05/07
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface ThreadModelType
{
	/** Single cooperatively threaded. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SINGLE_THREAD_COOP =
		0;
	
	/** Single threaded, with preemption. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SINGLE_THREAD_PREEMPT =
		1;
	
	/** Simultaneous Multi-threaded. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MULTI_THREAD =
		2;
	
	/** The number of threading models. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_MODELS =
		3;
}
