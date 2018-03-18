// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.cldc.system.type.Array;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.LcdCallback;
import cc.squirreljme.runtime.lcdui.server.LcdDisplayable;
import java.util.HashMap;
import java.util.Map;

/**
 * This manages callbacks and allows them to be made for tasks, it also
 * handles the dirty work needed for the callbacks so that each implementation
 * does not have to guess at what is done.
 *
 * @since 2018/03/18
 */
public final class LcdCallbackManager
{
	/** Callbacks for each individual task. */
	private final Map<SystemTask, RemoteMethod> _callbacks =
		new HashMap<>();
	
	/**
	 * Registers a callback for the given task.
	 *
	 * @param __task The owning task.
	 * @param __m The method to callback to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public final void registerCallback(SystemTask __task, RemoteMethod __m)
		throws NullPointerException
	{
		if (__task == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Store the callback
		this._callbacks.put(__task, __m);
	}
	
	/**
	 * Specifies that the given displayable should be painted.
	 *
	 * @param __d The displayable to paint.
	 * @param __cx The clipping X coordinate.
	 * @param __cy The clipping Y coordinate.
	 * @param __cw The clipping width.
	 * @param __ch The clipping height.
	 * @param __buf The buffer to send the result into after drawing.
	 * @param __pal The palette to use for the remote buffer.
	 * @param __bw The buffer width.
	 * @param __bh The buffer height.
	 * @param __alpha Is an alpha channel being used?
	 * @param __pitch The number of elements for the width in the buffer.
	 * @param __offset The offset into the buffer to the actual image data.
	 * @throws NullPointerException On null arguments except for {@code __pal}.
	 * @since 2018/03/18
	 */
	public void displayablePaint(LcdDisplayable __d, int __cx, int __cy,
		int __cw, int __ch, Array __buf, IntegerArray __pal, int __bw,
		int __bh, boolean __alpha, int __pitch, int __offset)
		throws NullPointerException
	{
		if (__d == null || __buf == null)
			throw new NullPointerException("NARG");
		
		// Get the method to call for the given task, if it does not exist
		// then ignore it
		RemoteMethod rm = this._callbacks.get(__d.task());
		if (rm == null)
			return;
		
		// Perform the call
		rm.<VoidType>invoke(VoidType.class, LcdCallback.DISPLAYABLE_PAINT,
			__d.handle(), __cx, __cy, __cw, __ch, __buf, __pal, __bw, __bh,
			__alpha, __pitch, __offset);
	}
}

