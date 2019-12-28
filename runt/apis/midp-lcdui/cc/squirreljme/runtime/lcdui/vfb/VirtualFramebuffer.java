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
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/28
	 */
	public final long framebufferProperty(int... __args)
		throws NullPointerException
	{
		if (__args == null)
			throw new NullPointerException("NARG");
		
		// Depends on the length
		switch (__args.length)
		{
				// Nothing to do?
			case 0:
				return 0;
			
			case 1:
				return this.framebufferProperty(
					__args[0],
					0, 0, 0, 0, 0, 0, 0, 0);
			
			case 2:
				return this.framebufferProperty(
					__args[0],
					__args[1],
					0, 0, 0, 0, 0, 0, 0);
			
			case 3:
				return this.framebufferProperty(
					__args[0],
					__args[1],
					__args[2],
					0, 0, 0, 0, 0, 0);
			
			case 4:
				return this.framebufferProperty(
					__args[0],
					__args[1],
					__args[2],
					__args[3],
					0, 0, 0, 0);
			
			case 5:
				return this.framebufferProperty(
					__args[0],
					__args[1],
					__args[2],
					__args[3],
					__args[4],
					0, 0, 0);
			
			case 6:
				return this.framebufferProperty(
					__args[0],
					__args[1],
					__args[2],
					__args[3],
					__args[4],
					__args[5],
					0, 0);
			
			case 7:
				return this.framebufferProperty(
					__args[0],
					__args[1],
					__args[2],
					__args[3],
					__args[4],
					__args[5],
					__args[6],
					0);
			
			case 8:
				return this.framebufferProperty(
					__args[0],
					__args[1],
					__args[2],
					__args[3],
					__args[4],
					__args[5],
					__args[6],
					__args[7]);
			
			default:
				return 0;
		}
	}
	
	/**
	 * Executes the framebuffer property system call.
	 *
	 * @param __pid The property ID.
	 * @param __args The call arguments.
	 * @return The result of the property.
	 * @since 2019/12/28
	 */
	public final long framebufferProperty(int __pid, int __a, int __b, int __c,
		int __d, int __e, int __g, int __h)
	{
		throw new todo.TODO();
	}
}

