// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.callbacks;

import cc.squirreljme.jvm.mle.scritchui.annotation.ScritchEventLoop;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPencilBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;
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
	 * @param __g Pencil graphics for drawing.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @param __special Special value for painting, may be {@code 0} or any
	 * other value if it is meaningful to what is being painted.
	 * @since 2024/03/19
	 */
	@SquirrelJMEVendorApi
	@ScritchEventLoop
	void paint(@NotNull ScritchComponentBracket __component,
		@NotNull ScritchPencilBracket __g,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sh,
		int __special);
}
