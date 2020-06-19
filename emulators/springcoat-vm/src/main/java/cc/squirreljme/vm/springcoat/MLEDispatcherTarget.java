// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
