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
import cc.squirreljme.jvm.Framebuffer;
import cc.squirreljme.jvm.IPCCallback;

/**
 * This is a virtual framebuffer which may be used by non-SquirrelJME JVMs and
 * by higher level JVMs to allow for IPC based graphics to be used.
 *
 * This assumes that event handling is used.
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
	
	/** Title listener. */
	private FramebufferTitleListener _titlelistener;
	
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
	 * Executes the framebuffer control system call.
	 *
	 * @param __args The call arguments.
	 * @return The result of the property.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/28
	 */
	public final long framebufferControl(int... __args)
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
				return this.framebufferControl(
					__args[0],
					0, 0, 0, 0, 0, 0, 0, 0);
			
			case 2:
				return this.framebufferControl(
					__args[0],
					__args[1],
					0, 0, 0, 0, 0, 0, 0);
			
			case 3:
				return this.framebufferControl(
					__args[0],
					__args[1],
					__args[2],
					0, 0, 0, 0, 0, 0);
			
			case 4:
				return this.framebufferControl(
					__args[0],
					__args[1],
					__args[2],
					__args[3],
					0, 0, 0, 0);
			
			case 5:
				return this.framebufferControl(
					__args[0],
					__args[1],
					__args[2],
					__args[3],
					__args[4],
					0, 0, 0);
			
			case 6:
				return this.framebufferControl(
					__args[0],
					__args[1],
					__args[2],
					__args[3],
					__args[4],
					__args[5],
					0, 0);
			
			case 7:
				return this.framebufferControl(
					__args[0],
					__args[1],
					__args[2],
					__args[3],
					__args[4],
					__args[5],
					__args[6],
					0);
			
			case 8:
				return this.framebufferControl(
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
	 * Executes the framebuffer control system call.
	 *
	 * @param __pid The property ID.
	 * @param __args The call arguments.
	 * @return The result of the property.
	 * @throws IllegalArgumentException If the property was not known.
	 * @since 2019/12/28
	 */
	public final long framebufferControl(int __pid, int __a, int __b, int __c,
		int __d, int __e, int __g, int __h)
		throws IllegalArgumentException
	{
		// Depends on the property
		switch (__pid)
		{
				// Raw address (since it is simulated, this is not set)
			case Framebuffer.CONTROL_ADDRESS:
				return 0;
				
				// Width and scanline length
			case Framebuffer.CONTROL_WIDTH:
			case Framebuffer.CONTROL_SCANLEN:
				return this.width;
				
				// Height
			case Framebuffer.CONTROL_HEIGHT:
				return this.height;
			
				// Flushes the display
			case Framebuffer.CONTROL_FLUSH:
				throw new todo.TODO();
				
				// Pixel format is always integer
			case Framebuffer.CONTROL_FORMAT:
				return Framebuffer.FORMAT_INTEGER_RGB888;
				
				// Scan line length in bytes
			case Framebuffer.CONTROL_SCANLEN_BYTES:
				return this.width * 4;
				
				// The number of bytes per pixel
			case Framebuffer.CONTROL_BYTES_PER_PIXEL:
				return 4;
				
				// Bits per pixel
			case Framebuffer.CONTROL_BITS_PER_PIXEL:
				return 32;
				
				// The number of pixels
			case Framebuffer.CONTROL_NUM_PIXELS:
				return this.width * this.height;
				
				// Backlight not supported
			case Framebuffer.CONTROL_BACKLIGHT_LEVEL_GET:
			case Framebuffer.CONTROL_BACKLIGHT_LEVEL_SET:
			case Framebuffer.CONTROL_BACKLIGHT_LEVEL_MAX:
				return 0;
				
				// Upload integer array
			case Framebuffer.CONTROL_UPLOAD_ARRAY_INT:
				throw new todo.TODO();
				
				// The backing array object
			case Framebuffer.CONTROL_BACKING_ARRAY_OBJECT:
				return Assembly.objectToPointer(this.pixels);
				
				// Returns the capabilities of the display.
			case Framebuffer.CONTROL_GET_CAPABILITIES:
				return Framebuffer.CAPABILITY_TOUCH |
					Framebuffer.CAPABILITY_KEYBOARD |
					Framebuffer.CAPABILITY_IPC_EVENTS |
					Framebuffer.CAPABILITY_COLOR;
				
				// Query acceleration function.
			case Framebuffer.CONTROL_ACCEL_FUNC_QUERY:
				throw new todo.TODO();
			
				// Perform acceleration function.
			case Framebuffer.CONTROL_ACCEL_FUNC_INVOKE:
				throw new todo.TODO();
				
				// Set title
			case Framebuffer.CONTROL_SET_TITLE:
				if (__a != 0)
				{
					FramebufferTitleListener tl = this._titlelistener;
					if (tl != null)
						tl.titleUpdated(
							new String((char[])Assembly.pointerToObject(__a)));
				}
				return 0;
				
				// {@squirreljme.error EB3b Unknown control property.}
			default:
				throw new IllegalArgumentException("EB3b");
		}
	}
	
	/**
	 * Returns the framebuffer pixels.
	 *
	 * @return The framebuffer pixels.
	 * @since 2020/01/18
	 */
	public final int[] pixels()
	{
		return this.pixels;
	}
}

