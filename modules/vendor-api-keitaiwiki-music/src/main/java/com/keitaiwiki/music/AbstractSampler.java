// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.keitaiwiki.music;

import cc.squirreljme.jvm.mle.brackets.AudioStreamBracket;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Bridge between SquirrelJME's audio stream support and the library's
 * streaming renderer.
 *
 * @since 2025/05/07
 */
@SquirrelJMEVendorApi
public abstract class AbstractSampler
	implements AudioStreamRenderer, Sampler
{
	/**
	 * {@inheritDoc}
	 * @since 2025/05/07
	 */
	@Override
	public int render(@NotNull AudioStreamBracket __stream, int __format,
		@Range(from = 0, to = Integer.MAX_VALUE) int __rate,
		@Range(from = 0, to = Integer.MAX_VALUE) int __channels,
		@NotNull Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
	{
		throw Debugging.todo();
	}
}
