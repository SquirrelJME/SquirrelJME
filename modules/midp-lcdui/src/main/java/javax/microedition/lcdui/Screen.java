// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayScale;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;
import cc.squirreljme.runtime.lcdui.scritchui.ScritchLcdUiUtils;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

/**
 * This is the base class for all user interactive displays.
 *
 * It is only recommended to change the screen contents when it is not being
 * displayed.
 *
 * @since 2017/02/28
 */
@Api
public abstract class Screen
	extends Displayable
{
	/**
	 * Initializes the base screen.
	 *
	 * @since 2017/02/28
	 */
	Screen()
	{
	}
	
	/**
	 * Returns the ScritchUI component.
	 *
	 * @return The ScritchUI component.
	 * @since 2024/07/25
	 */
	abstract ScritchComponentBracket __scritchComponent();
	
	/**
	 * Returns the displayable height.
	 *
	 * @return The displayable height.
	 * @since 2024/07/28
	 */
	final int __getHeight()
	{
		// Get direct widget size
		DisplayableState state = this._state;
		if (state.currentDisplay() != null)
			state.scritchApi().component()
				.height(this.__scritchComponent());
		
		// Otherwise, fallback to the owning or default display
		return ScritchLcdUiUtils.lcduiDisplaySize(state,
			true);
	}
	
	/**
	 * Returns the displayable width.
	 *
	 * @return The displayable width.
	 * @since 2024/07/28
	 */
	final int __getWidth()
	{
		// Get direct widget size
		DisplayableState state = this._state;
		if (state.currentDisplay() != null)
			state.scritchApi().component()
				.width(this.__scritchComponent());
		
		// Otherwise, fallback to the owning or default display
		return ScritchLcdUiUtils.lcduiDisplaySize(state,
			false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/25
	 */
	@Override
	@MustBeInvokedByOverriders
	void __execRevalidate(DisplayState __parent)
	{
		// Setup super first
		super.__execRevalidate(__parent);
		
		// Get the display scale to determine how the list should scale
		DisplayScale scale = __parent.display()._scale;
		
		int w = Math.max(1, scale.textureW());
		int h = Math.max(1, scale.textureH());
		
		// Make sure the list has the correct texture size
		DisplayableState state = this._state;
		state.scritchApi().container().setBounds(
			state.scritchPanel(),
			this.__scritchComponent(), 0, 0, w, h);
	}
}


