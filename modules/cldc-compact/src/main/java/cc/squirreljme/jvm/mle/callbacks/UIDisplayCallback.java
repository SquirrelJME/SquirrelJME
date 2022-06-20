// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.callbacks;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;

/**
 * This callback is used for any calls the display system makes to applications
 * and otherwise.
 *
 * @since 2020/10/03
 */
public interface UIDisplayCallback
	extends ShelfCallback
{
	/**
	 * This is used to refer to a later invocation, by its ID.
	 * 
	 * @param __displayId The display identifier.
	 * @param __serialId The identity of the serialized call.
	 * @since 2020/10/03
	 */
	void later(int __displayId, int __serialId);
	
	/**
	 * Callback that is used to draw for a given display provided that an
	 * entire display is used for drawing actions.
	 * 
	 * @param __display The display that is being drawn.
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
	void paintDisplay(UIDisplayBracket __display, int __pf, int __bw,
		int __bh, Object __buf, int __offset, int[] __pal, int __sx, int __sy,
		int __sw, int __sh, int __special);
}
