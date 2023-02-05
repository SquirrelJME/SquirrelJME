// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.callbacks;

import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
import cc.squirreljme.jvm.mle.constants.UIKeyEventType;
import cc.squirreljme.jvm.mle.constants.UIKeyModifier;
import cc.squirreljme.jvm.mle.constants.UIMouseButton;
import cc.squirreljme.jvm.mle.constants.UIMouseEventType;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Base interface for any callbacks which have a paint operation for drawing
 * a display, form, or item.
 *
 * @since 2023/01/13
 */
@Api
public interface UIDrawableCallback
	extends ShelfCallback
{
	/**
	 * Request to exit the drawable, usually means the application is being
	 * closed or attempted to be closed.
	 *
	 * @param __drawable The form being exited.
	 * @since 2020/09/12
	 */
	@Api
	void exitRequest(UIDrawableBracket __drawable);
	
	/**
	 * This is called on a keyboard/joystick action.
	 *
	 * @param __drawable The item acted on.
	 * @param __event One of {@link UIKeyEventType}.
	 * @param __keyCode Key code for the event.
	 * @param __modifiers Bit mask of {@link UIKeyModifier}.
	 * @since 2020/07/19
	 */
	@Api
	void eventKey(UIDrawableBracket __drawable, int __event,
		int __keyCode, int __modifiers);
	
	/**
	 * This is called on a mouse action.
	 *
	 * @param __drawable The item acted on.
	 * @param __event One of {@link UIMouseEventType}.
	 * @param __button One of {@link UIMouseButton}.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __modifiers Bit mask of {@link UIKeyModifier}.
	 * @since 2020/07/19
	 */
	@Api
	void eventMouse(UIDrawableBracket __drawable, int __event,
		int __button, int __x, int __y, int __modifiers);
	
	/**
	 * Callback that is used to draw a given drawable item.
	 *
	 * @param __drawable The drawable that is being drawn.
	 * @param __pf The {@link UIPixelFormat} used for the draw.
	 * @param __bw The buffer width, this is the scanline width of the buffer.
	 * @param __bh The buffer height.
	 * @param __buf The target buffer to draw to, this is cast to the correct
	 * buffer format.
	 * @param __offset The offset to the start of the buffer.
	 * @param __pal The color palette, may be {@code null}.
	 * @param __sx Starting surface X coordinate.
	 * @param __sy Starting surface Y coordinate.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @param __special Special value for painting, may be {@code 0} or any
	 * other value if it is meaningful to what is being painted.
	 * @since 2022/01/05
	 */
	@Api
	void paint(UIDrawableBracket __drawable, int __pf, int __bw,
		int __bh, Object __buf, int __offset, int[] __pal, int __sx, int __sy,
		int __sw, int __sh, int __special);
}
