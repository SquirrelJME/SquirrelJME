// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchViewBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchSizeSuggestListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchViewListener;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Interface for ScritchUI viewports.
 *
 * @since 2024/07/29
 */
@SquirrelJMEVendorApi
public interface ScritchViewInterface
	extends ScritchApiInterface
{
	/**
	 * Gets the view rectangle.
	 *
	 * @param __view The view to get.
	 * @param __outRect The resultant rectangle.
	 * @throws MLECallError If the view is invalid; If the rectangle array
	 * is missing or not at least size 4.
	 * @since 2024/07/29
	 */
	@SquirrelJMEVendorApi
	void getView(@NotNull ScritchViewBracket __view,
		@NotNull int[] __outRect)
		throws MLECallError;
	
	/**
	 * Sets the internal area that the view provides a viewport over. 
	 *
	 * @param __view The view to set.
	 * @param __width The width to set.
	 * @param __height The height to set.
	 * @throws MLECallError If the view is not valid; Or if the width and/or
	 * height are zero or negative.
	 * @since 2024/07/29
	 */
	@SquirrelJMEVendorApi
	void setArea(@NotNull ScritchViewBracket __view,
		@Range(from = 1, to = Integer.MAX_VALUE) int __width,
		@Range(from = 1, to = Integer.MAX_VALUE) int __height)
		throws MLECallError;
	
	/**
	 * Sets the viewport of the given view.
	 *
	 * @param __view The view to set.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __width The width.
	 * @param __height The height.
	 * @throws MLECallError If the view is invalid; or if the bounds are
	 * invalid.
	 * @since 2024/07/29
	 */
	@SquirrelJMEVendorApi
	void setView(@NotNull ScritchViewBracket __view,
		@Range(from = 0, to = Integer.MAX_VALUE) int __x,
		@Range(from = 0, to = Integer.MAX_VALUE) int __y,
		@Range(from = 1, to = Integer.MAX_VALUE) int __width,
		@Range(from = 1, to = Integer.MAX_VALUE) int __height)
		throws MLECallError;
	
	/**
	 * Sets the viewport of the given view.
	 *
	 * @param __view The view to set.
	 * @param __inRect The view rectangle.
	 * @throws MLECallError If the view is invalid; or if the bounds are
	 * invalid.
	 * @since 2024/07/29
	 */
	@SquirrelJMEVendorApi
	void setView(@NotNull ScritchViewBracket __view,
		@NotNull int[] __inRect)
		throws MLECallError;
	
	/**
	 * Sets the size suggestion listener for contained components.
	 *
	 * @param __view The view to set for.
	 * @param __listener The listener to call when there is a suggestion.
	 * @throws MLECallError If the view is invalid.
	 * @since 2024/07/29
	 */
	@SquirrelJMEVendorApi
	void setSizeSuggestListener(@NotNull ScritchViewBracket __view,
		@Nullable ScritchSizeSuggestListener __listener)
		throws MLECallError;
	
	/**
	 * Sets the view listener for when the viewport changes position
	 * and/or size.
	 *
	 * @param __view The view to set for.
	 * @param __listener The listener to call when the viewport changes.
	 * @throws MLECallError If the view is invalid.
	 * @since 2024/07/29
	 */
	@SquirrelJMEVendorApi
	void setViewListener(@NotNull ScritchViewBracket __view,
		@Nullable ScritchViewListener __listener)
		throws MLECallError;
}
