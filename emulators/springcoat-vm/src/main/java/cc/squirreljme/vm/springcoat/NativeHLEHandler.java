// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This contains the native HLE handler for SpringCoat, all functions that
 * are performed on the MLE layer will be truly implemented here.
 *
 * @since 2020/05/30
 */
public final class NativeHLEHandler
{
	/**
	 * Not used.
	 *
	 * @since 2020/05/30
	 */
	private NativeHLEHandler()
	{
	}
	
	/**
	 * Handles the dispatching of the native method.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __func The function which was called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @since 2020/05/30
	 */
	public static Object dispatch(SpringThreadWorker __thread, String __func,
		Object... __args)
	{
		throw Debugging.todo(__func);
	}
}
