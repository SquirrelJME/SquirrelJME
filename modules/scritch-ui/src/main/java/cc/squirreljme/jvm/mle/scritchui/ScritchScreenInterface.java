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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;
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
	int dpi(@NotNull ScritchScreenBracket __screen)
		throws MLECallError;
	
	/**
	 * Returns the height of this screen.
	 *
	 * @param __screen The screen to get from.
	 * @return The screen height.
	 * @throws MLECallError If the screen is null or not valid.
	 * @since 2024/03/07
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	int height(@NotNull ScritchScreenBracket __screen)
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
	boolean isBuiltIn(@NotNull ScritchScreenBracket __screen)
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
	boolean isPortrait(@NotNull ScritchScreenBracket __screen)
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
	int id(@NotNull ScritchScreenBracket __screen)
		throws MLECallError;
	
	/**
	 * Returns the width of this screen.
	 *
	 * @param __screen The screen to get from.
	 * @return The screen width.
	 * @throws MLECallError If the screen is null or not valid.
	 * @since 2024/03/07
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	int width(@NotNull ScritchScreenBracket __screen)
		throws MLECallError;
}
