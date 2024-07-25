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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchChoiceBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Interface for the manipulation of generic choice items.
 *
 * @since 2024/07/16
 */
@SquirrelJMEVendorApi
public interface ScritchChoiceInterface
	extends ScritchApiInterface
{
	/**
	 * Deletes the given choice index.
	 *
	 * @param __choice The choice to modify.
	 * @param __atIndex The index to delete.
	 * @throws MLECallError If the choice is not valid; or if the index is
	 * not valid.
	 * @since 2024/07/25
	 */
	@SquirrelJMEVendorApi
	void delete(@NotNull ScritchChoiceBracket __choice,
		@Range(from = 0, to = Integer.MAX_VALUE) int __atIndex)
		throws MLECallError;
	
	/**
	 * Deletes all items within the given choice.
	 *
	 * @param __choice The choice to delete everything from.
	 * @throws MLECallError If the choice is not valid; or if the index is
	 * not valid.
	 * @since 2024/07/25
	 */
	@SquirrelJMEVendorApi
	void deleteAll(@NotNull ScritchChoiceBracket __choice)
		throws MLECallError;
	
	/**
	 * Inserts an empty choice at the given index.
	 *
	 * @param __choice The choice to modify.
	 * @param __atIndex The index to insert at.
	 * @return The index where the item was added.
	 * @throws MLECallError If the choice is not valid; or if the index is
	 * not valid.
	 * @since 2024/07/25
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	int insert(@NotNull ScritchChoiceBracket __choice,
		@Range(from = 0, to = Integer.MAX_VALUE) int __atIndex)
		throws MLECallError;
	
	/**
	 * Sets whether the given choice index is enabled.
	 *
	 * @param __choice The choice to modify.
	 * @param __atIndex The index to modify.
	 * @param __enabled Is the index enabled?
	 * @throws MLECallError If the choice is not valid; or if the index is
	 * not valid.
	 * @since 2024/07/25
	 */
	@SquirrelJMEVendorApi
	void setEnabled(@NotNull ScritchChoiceBracket __choice,
		@Range(from = 0, to = Integer.MAX_VALUE) int __atIndex,
		boolean __enabled)
		throws MLECallError;
	
	/**
	 * Sets whether the given choice index has an image.
	 *
	 * @param __choice The choice to modify.
	 * @param __atIndex The index to modify.
	 * @param __data Buffer data, if {@code null} then the image will be
	 * cleared.
	 * @param __off The offset into the buffer.
	 * @param __scanLen The scanline length.
	 * @param __width The image width.
	 * @param __height The image height.
	 * @throws MLECallError If the choice is not valid; if the index is
	 * not valid; or the image parameters are not valid.
	 * @since 2024/07/25
	 */
	@SquirrelJMEVendorApi
	void setImage(@NotNull ScritchChoiceBracket __choice,
		@Range(from = 0, to = Integer.MAX_VALUE) int __atIndex,
		@Nullable int[] __data,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __scanLen,
		@Range(from = 1, to = Integer.MAX_VALUE) int __width,
		@Range(from = 1, to = Integer.MAX_VALUE) int __height)
		throws MLECallError;
	
	/**
	 * Sets whether the given choice index is selected.
	 *
	 * @param __choice The choice to modify.
	 * @param __atIndex The index to modify.
	 * @param __selected Is the index selected?
	 * @throws MLECallError If the choice is not valid; or if the index is
	 * not valid.
	 * @since 2024/07/25
	 */
	@SquirrelJMEVendorApi
	void setSelected(@NotNull ScritchChoiceBracket __choice,
		@Range(from = 0, to = Integer.MAX_VALUE) int __atIndex,
		boolean __selected)
		throws MLECallError;
	
	/**
	 * Sets The string of the given choice entry.
	 *
	 * @param __choice The choice to modify.
	 * @param __atIndex The index to modify.
	 * @param __string The string to set, {@code null} will clear it.
	 * @throws MLECallError If the choice is not valid; or if the index is
	 * not valid.
	 * @since 2024/07/25
	 */
	@SquirrelJMEVendorApi
	void setString(@NotNull ScritchChoiceBracket __choice,
		@Range(from = 0, to = Integer.MAX_VALUE) int __atIndex,
		@Nullable String __string)
		throws MLECallError;
}
