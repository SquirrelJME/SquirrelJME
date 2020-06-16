// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.fbui;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.Framebuffer;
import cc.squirreljme.jvm.IPCCallback;
import cc.squirreljme.jvm.IPCManager;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.VMType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * This class contains the state of the user interface. It may call the native
 * frame buffer system calls to perform updates or otherwise.
 *
 * @since 2020/01/12
 */
public final class UIState
	implements IPCCallback
{
	/** User interface state. */
	private static UIState _UI_STATE;
	
	/** Get capabilities of the display. */
	protected final int capabilities;
	
	/** The format of the framebuffer. */
	protected final byte format;
	
	/** Is native IPC used for the framebuffer? */
	protected final boolean useipc;
	
	/** Has screen flipping capability. */
	protected final boolean canflip;
	
	/** Is this a color screen? */
	protected final boolean iscolor;
	
	/** The currently bound display. */
	protected Display display;
	
	/** The current drawable state. */
	protected UIDrawerState drawerstate;
	
	/**
	 * Only internally initialized.
	 *
	 * @since 2020/01/12
	 */
	private UIState()
	{
		// This will be forwarded at a later date when SpringCoat is more
		// stable
		Debugging.todoNote("Fixup UIState init on non-SummerCoat");
		if (VMType.SUMMERCOAT != RuntimeShelf.vmType())
			throw new IllegalArgumentException("SMCT");
		
		// Get display format
		this.format = (byte)Assembly.sysCallV(SystemCallIndex.FRAMEBUFFER,
			Framebuffer.CONTROL_FORMAT);
		
		// Get display capabilities
		int capabilities = Assembly.sysCallV(SystemCallIndex.FRAMEBUFFER,
			Framebuffer.CONTROL_GET_CAPABILITIES);
		this.capabilities = capabilities;
		
		// Quick capability setting
		this.useipc =
			((capabilities & Framebuffer.CAPABILITY_IPC_EVENTS) != 0);
		this.canflip = 
			((capabilities & Framebuffer.CAPABILITY_SCREEN_FLIP) != 0);
		this.iscolor =
			((capabilities & Framebuffer.CAPABILITY_COLOR) != 0);
		
		// Register callback for the UI so we get events and such
		// Note that even if we do event handling in this program because the
		// JVM does not have one, we still register for other portions using
		// them
		IPCManager.register(Framebuffer.IPC_ID, this);
	}
	
	/**
	 * Binds this display to the current state.
	 *
	 * @param __d The display to bind.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/01/12
	 */
	public final void bindDisplay(Display __d)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		this.display = __d;
	}
	
	/**
	 * Returns the capabilities of the framebuffer.
	 *
	 * @return The framebuffer capabilities.
	 * @since 2020/01/15
	 */
	public final int capabilities()
	{
		return this.capabilities;
	}
	
	/**
	 * Is the display currently flipped?
	 *
	 * @return If the display is flipped.
	 * @since 2020/01/15
	 */
	public final boolean displayFlipped()
	{
		// If the display cannot be flipped, this always returns false
		if (!this.canflip)
			return false;
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the current display height.
	 *
	 * @return The display height.
	 * @since 2020/01/15
	 */
	public final int displayHeight()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Is this a color display?
	 *
	 * @return If this is a color display.
	 * @since 2020/01/15
	 */
	public final boolean displayIsColor()
	{
		return this.iscolor;
	}
	
	/**
	 * Returns the number of unique colors.
	 *
	 * @return The number of unique colors.
	 * @since 2020/01/15
	 */
	public final int displayUniqueColors()
	{
		int format = this.format;
		switch (format)
		{
			case Framebuffer.FORMAT_INTEGER_RGB888:	return 16777216;
			case Framebuffer.FORMAT_BYTE_INDEXED:	return 256;
			case Framebuffer.FORMAT_SHORT_RGB565:	return 66560;
			case Framebuffer.FORMAT_PACKED_ONE:		return 2;
			case Framebuffer.FORMAT_PACKED_TWO:		return 4;
			case Framebuffer.FORMAT_PACKED_FOUR:	return 16;
			
			default:
				throw new todo.OOPS();
		}
	}
	
	/**
	 * Returns the current display width.
	 *
	 * @return The display width.
	 * @since 2020/01/15
	 */
	public final int displayWidth()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the current drawer state.
	 *
	 * @return The current drawer state.
	 * @since 2020/01/15
	 */
	public final UIDrawerState drawerState()
	{
		return this.drawerstate;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/28
	 */
	@Override
	public long ipcCall(int __tid, int __ipcid, int __a, int __b,
		int __c, int __d, int __e, int __f, int __g, int __h)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Requests that the display be repainted.
	 *
	 * @since 2020/01/15
	 */
	public final void repaint()
	{
		this.repaint(0, 0, this.displayWidth(), this.displayHeight());
	}
	
	/**
	 * Requests that the display be repainted.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2020/01/15
	 */
	public final void repaint(int __x, int __y, int __w, int __h)
	{
		if (this.useipc)
			Assembly.sysCall(SystemCallIndex.FRAMEBUFFER,
				Framebuffer.CONTROL_REPAINT_REQUEST,
				__x, __y, __w, __h);
		
		// IPC not supported, so we manage our own event queue and such
		else
			throw new todo.TODO();
	}
	
	/**
	 * Sets the displayable to be drawn.
	 *
	 * @param __d The displayable to draw.
	 * @return The new drawing state.
	 * @since 2020/01/15
	 */
	public final UIDrawerState setDisplayable(Displayable __d)
	{
		UIDrawerState rv;
		
		// Nothing
		if (__d == null)
			rv = null;
			
		// List
		else if (__d instanceof List)
			rv = new DrawingList((List)__d);
		
		// Canvas
		else if (__d instanceof Canvas)
			rv = new DrawingCanvas((Canvas)__d);
		
		// Unknown
		else
			throw new todo.OOPS(__d.getClass().getName());
		
		// Set state and return
		this.drawerstate = rv;
		return rv;
	}
	
	/**
	 * Sets the system title.
	 *
	 * @param __s The title to set.
	 * @since 2020/01/15
	 */
	public final void setTitle(String __s)
	{
		char[] chars = (__s == null ? "SquirrelJME" : __s).toCharArray();
		
		// Tell the framebuffer to set the title as needed
		long pointer = Assembly.objectToPointer(chars);
		Assembly.sysCall(SystemCallIndex.FRAMEBUFFER,
			Framebuffer.CONTROL_SET_TITLE,
			Assembly.longUnpackHigh(pointer), Assembly.longUnpackLow(pointer));
		
		// Touch
		if (chars.length > 0)
			chars[0] = chars[0];
	}
	
	/**
	 * Returns the instance of the user interface state.
	 *
	 * @return The instance of the user interface state.
	 * @since 2020/01/12
	 */
	public static final UIState getInstance()
	{
		UIState rv = UIState._UI_STATE;
		if (rv != null)
			return rv;
		
		// We may need to create an instance
		synchronized (UIState.class)
		{
			// Try again
			rv = UIState._UI_STATE;
			if (rv != null)
				return rv;
			
			// Initialize new state
			UIState._UI_STATE = (rv = new UIState());
			
			return rv;
		}
	}
}

