// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.callbacks;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * An extension of {@link AudioStreamRenderer} which allows for control of
 * playback.
 *
 * @since 2025/05/04
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface AudioStreamPlayer
	extends AudioStreamRenderer
{
}
