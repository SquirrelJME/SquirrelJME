// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.vfb;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.FramebufferProperty;
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
	
	/** The width. */
	protected final int width;
	
	/** The height. */
	protected final int height;
	
	/** The raw pixel data. */
	protected final int[] pixels;
	
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
		this(__ipc, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	/**
	 * Initializes the virtual framebuffer.
	 *
	 * @param __ipc The callback to forward events to.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/28
	 */
	public VirtualFramebuffer(IPCCallback __ipc, int __w, int __h)
	{
		if (__ipc == null)
			throw new NullPointerException("NARG");
		
		// Set IPC to use
		this.ipc = __ipc;
		
		// Set size
		this.width = __w;
		this.height = __h;
		
		// Initialize raw pixel data
		this.pixels = new int[__w * __h];
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
		// Depends on the property
		switch (__pid)
		{
				// Raw address (since it is simulated, this is not set)
			case FramebufferProperty.ADDRESS:
				return 0;
				
				// Width and scanline length
			case FramebufferProperty.WIDTH:
			case FramebufferProperty.SCANLEN:
				return this.width;
				
				// Height
			case FramebufferProperty.HEIGHT:
				return this.height;
			
				// Flushes the display
			case FramebufferProperty.FLUSH:
				throw new todo.TODO();
				
				// Pixel format is always integer
			case FramebufferProperty.FORMAT:
				return FramebufferProperty.FORMAT_INTEGER_RGB888;
				
				// Scan line length in bytes
			case FramebufferProperty.SCANLEN_BYTES:
				return this.width * 4;
				
				// The number of bytes per pixel
			case FramebufferProperty.BYTES_PER_PIXEL:
				return 4;
				
				// Bits per pixel
			case FramebufferProperty.BITS_PER_PIXEL:
				return 32;
				
				// The number of pixels
			case FramebufferProperty.NUM_PIXELS:
				return this.width * this.height;
				
				// Backlight not supported
			case FramebufferProperty.BACKLIGHT_LEVEL_GET:
			case FramebufferProperty.BACKLIGHT_LEVEL_SET:
			case FramebufferProperty.BACKLIGHT_LEVEL_MAX:
				return 0;
				
				// Upload integer array
			case FramebufferProperty.UPLOAD_ARRAY_INT:
				throw new todo.TODO();
				
				// The backing array object
			case FramebufferProperty.BACKING_ARRAY_OBJECT:
				return Assembly.objectToPointer(this.pixels);
				
				// Unknown
			default:
				return 0;
		}
	}
}

