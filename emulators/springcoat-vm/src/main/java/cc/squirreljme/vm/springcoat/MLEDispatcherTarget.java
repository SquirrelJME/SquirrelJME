// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * This represents a target for MLE dispatching.
 *
 * @since 2020/06/18
 */
public interface MLEDispatcherTarget
{
	/**
	 * Handles the MLE call.
	 *
	 * @param __thread The thread to dispatch.
	 * @param __args The arguments.
	 * @return The result of the call.
	 * @since 2020/06/18
	 */
	Object handle(SpringThreadWorker __thread, Object... __args);
}
