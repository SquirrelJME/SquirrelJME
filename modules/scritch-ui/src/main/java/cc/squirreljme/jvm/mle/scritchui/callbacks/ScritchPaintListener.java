// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.callbacks;

import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * This is called when a paint request should be performed.
 *
 * @since 2024/03/19
 */
@SquirrelJMEVendorApi
public interface ScritchPaintListener
	extends ScritchListener
{
	/**
	 * Callback that is used to draw the given component.
	 *
	 * @param __component The component to draw on.
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
	 * @since 2024/03/19
	 */
	@SquirrelJMEVendorApi
	@Async.Execute
	void paint(@NotNull ScritchComponentBracket __component,
		@MagicConstant(valuesFromClass = UIPixelFormat.class) int __pf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bh,
		@NotNull Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Nullable int[] __pal,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sx,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sy,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sh,
		int __special);
}
