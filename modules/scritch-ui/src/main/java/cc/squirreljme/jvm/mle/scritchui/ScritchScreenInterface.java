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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Interface which is used to interact with display screens.
 *
 * @since 2024/03/07
 */
@SquirrelJMEVendorApi
public interface ScritchScreenInterface
	extends ScritchApiInterface
{
	/**
	 * Returns the DPI of the screen.
	 *
	 * @param __screen The screen to get from.
	 * @return The screen DPI.
	 * @throws MLECallError If the screen is null or not valid.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	int screenDpi(@NotNull ScritchScreenBracket __screen)
		throws MLECallError;
	
	/**
	 * Returns the bounds of the screen, this includes its relative position
	 * to the origin point of all screen on multiscreen devices.
	 *
	 * @param __screen The screen to get the bounds of.
	 * @param __for The optional component for screen context.
	 * @param __pixels The pixel bounds ({@code int[4]{x, y, w, h}}).
	 * @param __mm The millimeter bounds ({@code int[4]{x, y, w, h}}).
	 * @throws MLECallError On null arguments and/or if the screen is not
	 * valid.
	 * @since 2025/12/23
	 */
	@SquirrelJMEVendorApi
	void screenGetBounds(@NotNull ScritchScreenBracket __screen,
		@Nullable ScritchComponentBracket __for,
		@NotNull int[] __pixels, @NotNull int[] __mm)
		throws MLECallError;
	
	/**
	 * Is this screen built into the device or is it detachable?
	 *
	 * @param __screen The screen to get from.
	 * @return If the screen is built in or not.
	 * @throws MLECallError If the screen is null or not valid.
	 * @since 2024/03/10
	 */
	@SquirrelJMEVendorApi
	boolean screenIsBuiltIn(@NotNull ScritchScreenBracket __screen)
		throws MLECallError;
	
	/**
	 * Is the screen in portrait orientation?
	 *
	 * @param __screen The screen to check.
	 * @return If the screen is in portrait orientation.
	 * @throws MLECallError If the screen is null or not valid.
	 * @since 2024/03/11
	 */
	@SquirrelJMEVendorApi
	boolean screenIsPortrait(@NotNull ScritchScreenBracket __screen)
		throws MLECallError;
	
	/**
	 * The ID of this screen.
	 * 
	 * @param __screen The screen to get from.
	 * @return The screen ID.
	 * @throws MLECallError If the screen is null or not valid.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	int screenId(@NotNull ScritchScreenBracket __screen)
		throws MLECallError;
}
