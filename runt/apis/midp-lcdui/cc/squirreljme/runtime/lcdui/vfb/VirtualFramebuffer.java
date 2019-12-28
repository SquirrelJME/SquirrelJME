// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.vfb;

import cc.squirreljme.jvm.IPCCallback;

/**
 * This is a virtual framebuffer which may be used by non-SquirrelJME JVMs and
 * by higher level JVMs to allow for IPC based graphics to be used.
 *
 * @since 2019/12/28
 */
public final class VirtualFramebuffer
{
	/** The default width. */
	public static final int DEFAULT_WIDTH =
		320;
	
	/** The default height. */
	public static final int DEFAULT_HEIGHT =
		200;
	
	/** The callback to invoke with screen actions. */
	protected final IPCCallback ipc;
	
	/**
	 * Initializes the virtual framebuffer.
	 *
	 * @param __ipc The callback to forward events to.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/28
	 */
	public VirtualFramebuffer(IPCCallback __ipc)
		throws NullPointerException
	{
		if (__ipc == null)
			throw new NullPointerException("NARG");
		
		// Set IPC to use
		this.ipc = __ipc;
	}
	
	/**
	 * Executes the framebuffer property system call.
	 *
	 * @param __args The call arguments.
	 * @return The result of the property.
	 * @since 2019/12/28
	 */
	public final long framebufferProperty(int... __args)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Executes the framebuffer property system call.
	 *
	 * @param __pid The property ID.
	 * @param __args The call arguments.
	 * @return The result of the property.
	 * @since 2019/12/28
	 */
	public final long framebufferProperty(int __pid, int... __args)
	{
		throw new todo.TODO();
	}
}

