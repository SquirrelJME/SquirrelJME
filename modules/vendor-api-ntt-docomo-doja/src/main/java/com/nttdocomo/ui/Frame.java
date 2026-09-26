// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFElementColor;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayManager;
import cc.squirreljme.runtime.lcdui.scritchui.extra.ExtraDisplayable;
import cc.squirreljme.runtime.lcdui.scritchui.extra.ExtraStateManager;
import cc.squirreljme.runtime.nttdocomo.ui.BGColor;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandLayoutPolicy;
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
	final BGColor _bgColor;
	
	/** The cached displayable. */
	volatile Reference<Displayable> _displayableCache;
	
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
		
		// Use default background color
		this._bgColor = new BGColor(DisplayManager.instance().scritch()
			.environment().lookAndFeel().lafElementColor(null,
				ScritchLAFElementColor.PANEL_BACKGROUND) | 0xFF_000000);
	}
	
	/**
	 * Returns the height of the current frame.
	 *
	 * @return The height of the current frame.
	 * @since 2021/11/30
	 */
	@Api
	public int getHeight()
	{
		// This should never happen, if it does then this means a subclass
		// does not have the Displayable stored in a field
		Displayable d = this.__displayable(Displayable.class);
		if (d == null)
			throw Debugging.oops();
		
		return d.getHeight();
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
		// This should never happen, if it does then this means a subclass
		// does not have the Displayable stored in a field
		Displayable d = this.__displayable(Displayable.class);
		if (d == null)
			throw Debugging.oops();
		
		return d.getWidth();
	}
	
	/**
	 * Sets the background color.
	 *
	 * @param __c The background color to set.
	 * @since 2024/11/02
	 */
	@Api
	public void setBackground(int __c)
	{
		this._bgColor.bgColor = __c;
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
		
		// This should never happen, if it does then this means a subclass
		// does not have the Displayable stored in a field
		Displayable displayable = this.__displayable(Displayable.class);
		if (displayable == null)
			throw Debugging.oops();
		
		
		// If a layout policy for the soft keys has not been set, set it now
		CommandLayoutPolicy layout = displayable.getCommandLayoutPolicy();
		if (layout == null)
			displayable.setCommandLayoutPolicy(new __SoftKeyLayout__());
		
		// Setup command and show it
		Command softKey = this._softKeys[__key];
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
	 * Returns the {@link Displayable} this wraps.
	 *
	 * @param <M> The desired displayable class.
	 * @param __as The desired displayable class.
	 * @return The MIDP {@link Displayable} used, or {@code null} if it is not
	 * known or has been garbage collected.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/09/25
	 */
	@SquirrelJMEVendorApi
	<M extends Displayable> M __displayable(Class<M> __as)
		throws NullPointerException
	{
		if (__as == null)
			throw new NullPointerException("NARG");
		
		// Has this been cached?
		Reference<Displayable> ref = this._displayableCache;
		if (ref != null)
		{
			Displayable rv = ref.get();
			if (rv != null)
				return __as.cast(rv);
		}
		
		// Otherwise, we need to get it from the extra state
		ExtraDisplayable extra = ExtraStateManager.locate(
			ExtraDisplayable.class, this);
		if (extra != null)
		{
			// Is this still valid?
			Displayable rv = extra.get();
			if (rv != null)
				this._displayableCache = new WeakReference<>(rv);
					
			// Make sure it is the class we want
			return __as.cast(rv);
		}
		
		// Was GCed
		return null;
	}
	
	/**
	 * Must be called after construction so SquirrelJME can implement more
	 * operations.
	 *
	 * @since 2022/02/14
	 */
	final void __postConstruct()
	{
		// Has this been disposed?
		Displayable displayable = this.__displayable(
			Displayable.class);
		if (displayable == null)
			throw new UIException(UIException.ILLEGAL_STATE, "GCGC");
		
		// Add the listener for commands
		displayable.setCommandListener(
			new __ShoulderButtonEmitter__(new WeakReference<>(this)));
	}
}
