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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

/**
 * This is the main frame within an i-mode application, it is equivalent to
 * MIDP's {@link Displayable}.
 *
 * @see Displayable
 * @since 2021/11/30
 */
@Api
public abstract class Frame
{
	/** The left soft key. */
	@Api
	public static final int SOFT_KEY_1 = 0;
	
	/** The right soft key. */
	@Api
	public static final int SOFT_KEY_2 = 1;
	
	/** The number of soft keys which are valid. */
	static final int _NUM_SOFT_KEYS = 2;
	
	/** The actual soft key commands. */
	final Command[] _softKeys;
	
	/** The background color of the display. */
	final __BGColor__ _bgColor;
	
	/**
	 * Base constructor.
	 *
	 * @since 2021/11/30
	 */
	Frame()
	{
		// Setup soft keys
		Command[] softKeys = new Command[Frame._NUM_SOFT_KEYS];
		this._softKeys = softKeys;
		
		// Add them now
		for (int i = 0; i < Frame._NUM_SOFT_KEYS; i++)
			softKeys[i] = new Command("", Command.ITEM, i);
		
		throw Debugging.todo();
		/*
		// Use default background color
		UIBackend backend = UIBackendFactory.getInstance(true);
		int defaultBgColor = backend.metric(backend.displays()[0],
			UIMetricType.COLOR_CANVAS_BACKGROUND) | 0xFF_000000;
		this._bgColor = new __BGColor__(defaultBgColor);
		
		 */
	}
	
	/**
	 * Returns the {@link Displayable} this wraps.
	 *
	 * @return The MIDP {@link Displayable} used.
	 * @since 2021/11/30
	 */
	abstract Displayable __displayable();
	
	/**
	 * Returns the height of the current frame.
	 *
	 * @return The height of the current frame.
	 * @since 2021/11/30
	 */
	@Api
	public int getHeight()
	{
		return this.__displayable().getHeight();
	}
	
	/**
	 * Returns the width of the current frame.
	 *
	 * @return The width of the current frame.
	 * @since 2021/11/30
	 */
	@Api
	public int getWidth()
	{
		return this.__displayable().getWidth();
	}
	
	@Api
	public void setBackground(int __c)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the label for a soft key.
	 *
	 * @param __key The key to set.
	 * @param __label The label for the key.
	 * @since 2021/11/30
	 */
	@Api
	public void setSoftLabel(int __key, String __label)
	{
		if (__key < 0 || __key >= Frame._NUM_SOFT_KEYS)
			throw Debugging.todo("Handle soft key %d?", __key);
		
		Displayable displayable = this.__displayable();
		Command softKey = this._softKeys[__key];
		
		// If a layout policy for the soft keys has not been set, set it now
		if (displayable.getCommandLayoutPolicy() == null)
			displayable.setCommandLayoutPolicy(new __SoftKeyLayout__());
		
		// Setup command and show it
		if (__label != null && !__label.isEmpty())
		{
			// Change label
			softKey.setLabel(__label);
			
			// Show it
			displayable.addCommand(softKey);
		}
		
		// It goes away
		else
			displayable.removeCommand(softKey);
	}
	
	/**
	 * Must be called after construction so SquirrelJME can implement more
	 * operations.
	 *
	 * @since 2022/02/14
	 */
	final void __postConstruct()
	{
		// Add the listener for commands
		this.__displayable().setCommandListener(
			new __ShoulderButtonEmitter__(new WeakReference<>(this)));
	}
}
