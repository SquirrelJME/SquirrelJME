// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.callbacks;

import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Audio stream snooping callbacks.
 *
 * @since 2026/01/08
 */
@SquirrelJMEVendorApi
public interface AudioStreamSnoop
{
	/**
	 * This is called when a MIDI event occurs.
	 *
	 * @param __type The type of event.
	 * @param __data1 First data byte.
	 * @param __data2 Second data byte.
	 * @since 2026/01/08
	 */
	@SquirrelJMEVendorApi
	void midiEvent(int __type, int __data1, int __data2);
	
	/**
	 * Called when the end result of the data is ready to be sent to the
	 * sound card.
	 *
	 * @param __format The format of the stream.
	 * @param __rate The rate of the stream.
	 * @param __channels The number of channels to render.
	 * @param __buf The buffer to the data.
	 * @param __off The offset into the buffer.
	 * @param __len The length of the buffer.
	 * @since 2026/01/08
	 */
	@SquirrelJMEVendorApi
	void pcmBuffer(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __format,
		@Range(from = 0, to = Integer.MAX_VALUE) int __rate,
		@Range(from = 0, to = Integer.MAX_VALUE) int __channels,
		@NotNull Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len);
}
