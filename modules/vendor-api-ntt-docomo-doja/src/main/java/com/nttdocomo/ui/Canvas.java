// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Canvas for showing free-form raster graphics and otherwise.
 *
 * @see javax.microedition.lcdui.Canvas
 * @since 2021/11/30
 */
@Api
public abstract class Canvas
	extends Frame
{
	/** The number of key groups. */
	@SquirrelJMEVendorApi
	private static final byte _KEY_GROUPS =
		2;
	
	/** The max number of permitted keys. */
	@SquirrelJMEVendorApi
	private static final byte _MAX_KEYS =
		32 * Canvas._KEY_GROUPS;
	
	/** The native Java Canvas. */
	final __MIDPCanvas__ _midpCanvas = new __MIDPCanvas__(
		new WeakReference<>(this));
	
	/** The timers which are associated with the canvas. */
	final Map<Integer, Reference<ShortTimer>> _shortTimers =
		new LinkedHashMap<>();
	
	/** The current key group states. */
	final int[] _keyGroups = 
		new int[Canvas._KEY_GROUPS];
	
	/**
	 * Paints the given canvas.
	 *
	 * @param __g The graphics to use for drawing.
	 * @since 2024/03/05
	 */
	@Api
	public abstract void paint(Graphics __g);
	
	/**
	 * Initializes the base canvas.
	 *
	 * @since 2022/02/14
	 */
	@Api
	public Canvas()
	{
		// Needed to initialize the command listener 
		this.__postConstruct();
	}
	
	/**
	 * Returns the graphics object that is used for drawing onto the canvas.
	 *
	 * @return A {@link Graphics} object for drawing onto the canvas surface.
	 * @since 2022/02/25
	 */
	@Api
	public Graphics getGraphics()
	{
		// Use the backing double buffered graphics, but without a draw
		return new Graphics(
			this._midpCanvas._doubleBuffer.getGraphics(this.getWidth(),
				this.getHeight()), this._bgColor,
				new __LockFlush__(this));
	}
	
	/**
	 * The same as {@link #getKeypadState(int)} with a group of zero.
	 *
	 * @return The same as {@link #getKeypadState(int)} with a group of zero.
	 * @since 2024/08/12
	 */
	@Api
	public int getKeypadState()
	{
		return this.getKeypadState(0);
	}
	
	/**
	 * Returns the current state of the keypad, that is which keys are pressed
	 * down as a bitmask. The group is essentially a multiplier to the bit
	 * shift of 32, as only 32 keys can be stored at once. This is to allow
	 * access to higher valued keys. As an example {@link Display#KEY_0}
	 * is in group zero bit zero. Whereas
	 * the key {@link Display#KEY_CLEAR} is in group one bit zero.
	 *
	 * @param __group The group to obtain the key from.
	 * @return The bitmask of the keys.
	 * @throws IllegalArgumentException If the group is negative.
	 * @since 2024/08/12
	 */
	@Api
	public int getKeypadState(int __group)
		throws IllegalArgumentException
	{
		if (__group < 0)
			throw new IllegalArgumentException("NEGV");
		
		// Only valid up to a certain point, always zero otherwise
		if (__group >= Canvas._KEY_GROUPS)
			return 0;
		
		// Return group
		synchronized (this)
		{
			return this._keyGroups[__group];
		}
	}
	
	/**
	 * Processes a given event.
	 *
	 * @param __type The event type, from {@link Display}.
	 * @param __param The event parameter, typically the key that has been
	 * pressed but is dependent on {@code __type}.
	 * @since 2024/06/24
	 */
	@Api
	public void processEvent(int __type, int __param)
	{
		// This is implemented by subclasses and as such does nothing
		// by default
	}
	
	@Api
	public void repaint()
	{
		this.__displayable().repaint();
	}
	
	@Api
	public void repaint(int __x, int __y, int __w, int __h)
	{
		this.__displayable().repaint(__x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@Override
	__MIDPCanvas__ __displayable()
	{
		return this._midpCanvas;
	}
	
	/**
	 * Called when a key is pressed or release.
	 *
	 * @param __press Was this pressed?
	 * @param __id The key ID.
	 * @since 2024/08/12
	 */
	@SquirrelJMEVendorApi
	final void __key(boolean __press, int __id)
	{
		// Store in key groups?
		if (__id >= 0 && __id < Canvas._MAX_KEYS)
			synchronized (this)
			{
				int[] keyGroups = this._keyGroups;
				
				int groupId = __id / 32;
				int bitId = 1 << (__id % 32);
				
				// Set or clear?
				if (__press)
					keyGroups[groupId] |= bitId;
				else
					keyGroups[groupId] &= ~bitId;
			}
		
		// Forward to event processor
		this.processEvent((__press ? Display.KEY_PRESSED_EVENT :
			Display.KEY_RELEASED_EVENT), __id);
	}
}
