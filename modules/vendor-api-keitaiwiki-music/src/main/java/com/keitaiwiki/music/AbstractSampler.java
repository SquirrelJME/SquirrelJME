// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.keitaiwiki.music;

import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Bridge between SquirrelJME's audio stream support and the library's
 * streaming renderer.
 *
 * @since 2025/05/07
 */
@SquirrelJMEVendorApi
public abstract class AbstractSampler
	implements AudioStreamRenderer, Sampler.Instance
{
	/**
	 * {@inheritDoc}
	 * @since 2025/05/07
	 */
	@Override
	public void render(int __format, int __rate, int __channels, Object __buf,
		int __off, int __len)
	{
		// offset + frames * 2 > samples.length
		float[] buf = (float[])__buf;
		this.render(buf, 0, __len / __channels,
			1.0F, 1.0F, true, true);
		
		// Sum everything
		float sumP = 0.0F;
		float sumN = 0.0F;
		for (int i = 0, n = buf.length; i < n; i++)
		{
			if (buf[i] > 0)
				sumP += buf[i];
			else
				sumN += buf[i];
		}
		Debugging.debugNote("Sums: %g %g", sumN, sumP);
	}
}
