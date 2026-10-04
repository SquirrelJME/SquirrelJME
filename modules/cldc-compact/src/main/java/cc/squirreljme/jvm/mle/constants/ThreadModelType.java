// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * This represents the thread model type.
 *
 * @since 2021/05/07
 */
public interface ThreadModelType
{
	/** Single cooperatively threaded. */
	byte SINGLE_THREAD_COOP =
		0;
	
	/** Single threaded, with preemption. */
	byte SINGLE_THREAD_PREEMPT =
		1;
	
	/** Simultaneous Multi-threaded. */
	byte MULTI_THREAD =
		2;
	
	/** The number of threading models. */
	byte NUM_MODELS =
		3;
}
