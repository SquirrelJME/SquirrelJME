// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.callbacks;

import cc.squirreljme.jvm.mle.constants.AudioRenderResult;
import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * This callback is used for rendering of audio streams.
 *
 * @since 2025/05/04
 */
@SquirrelJMEVendorApi
public interface AudioStreamRenderer
	extends ShelfCallback
{
	/**
	 * Renders to the given audio stream.
	 *
	 * @param __format The format of the stream.
	 * @param __rate The rate of the stream.
	 * @param __channels The number of channels to render.
	 * @param __buf The buffer to the data.
	 * @param __off The offset into the buffer.
	 * @param __len The length of the buffer.
	 * @since 2025/05/04
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = AudioRenderResult.class)
	void render(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __format,
		@Range(from = 0, to = Integer.MAX_VALUE) int __rate,
		@Range(from = 0, to = Integer.MAX_VALUE) int __channels,
		@NotNull Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len);
}
