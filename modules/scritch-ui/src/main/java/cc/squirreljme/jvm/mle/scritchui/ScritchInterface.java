// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchBaseBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Main interface for ScritchUI, all the logic calls are made through this
 * initially.
 *
 * @since 2024/02/29
 */
@SquirrelJMEVendorApi
public interface ScritchInterface
	extends ScritchApiInterface
{
	/**
	 * Returns the generic choice interface.
	 *
	 * @return The generic choice interface.
	 * @since 2024/07/16
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchChoiceInterface choice();
	
	/**
	 * Returns the generic component interface.
	 *
	 * @return The generic component interface.
	 * @since 2024/03/16
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchComponentInterface component();
	
	/**
	 * Returns the generic container interface.
	 *
	 * @return The generic container interface.
	 * @since 2024/03/16
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchContainerInterface container();
	
	/**
	 * Deletes the given object.
	 *
	 * @param __object The object to delete.
	 * @throws MLECallError If the object is {@code null} or not valid to be
	 * deleted.
	 * @since 2024/07/20
	 */
	@SquirrelJMEVendorApi
	void objectDelete(@NotNull ScritchBaseBracket __object)
		throws MLECallError;
	
	/**
	 * Returns the interface which contains information on the environment.
	 *
	 * @return The environment interface.
	 * @since 2024/03/07
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchEnvironmentInterface environment();
	
	/**
	 * Returns the event loop interface.
	 *
	 * @return The event loop interface.
	 * @since 2024/03/16
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchEventLoopInterface eventLoop();
	
	/**
	 * Creates a hardware reference bracket to the native hardware graphics.
	 * 
	 * @param __pf The {@link UIPixelFormat} used for the draw.
	 * @param __bw The buffer width, this is the scanline width of the buffer.
	 * @param __bh The buffer height.
	 * @param __buf The target buffer to draw to, this is cast to the correct
	 * buffer format.
	 * @param __pal The color palette, may be {@code null}. 
	 * @param __sx Starting surface X coordinate.
	 * @param __sy Starting surface Y coordinate.
	 * @param __sw Surface width.
	 * @param __sh Surface height.
	 * @return The bracket capable of drawing hardware accelerated graphics.
	 * @throws MLECallError If the requested graphics are not valid.
	 * @since 2020/09/25
	 */
	@SquirrelJMEVendorApi
	PencilBracket hardwareGraphics(
		@MagicConstant(valuesFromClass = UIPixelFormat.class) int __pf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __bh,
		@NotNull Object __buf,
		@Nullable int[] __pal,
		int __sx, int __sy,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sh)
		throws MLECallError;
	
	/**
	 * Returns the list interface.
	 *
	 * @return The interface for lists.
	 * @since 2024/07/16
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchListInterface list();
	
	/**
	 * Interface for menu manipulation.
	 *
	 * @return The menu manipulation Api.
	 * @since 2024/07/20
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchMenuInterface menu();
	
	/**
	 * Returns the interface for generic paintables.
	 *
	 * @return Returns the interface for generic paintables.
	 * @since 2024/07/16
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchPaintableInterface paintable();
	
	/**
	 * Return the interface for panels.
	 *
	 * @return The panel interface.
	 * @since 2024/03/16
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchPanelInterface panel();
	
	/**
	 * Returns the screen interface.
	 *
	 * @return The screen interface.
	 * @since 2024/03/10
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchScreenInterface screen();
	
	/**
	 * Returns the window interface.
	 *
	 * @return The window interface.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchWindowInterface window();
}
