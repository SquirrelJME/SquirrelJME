// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * Contains and stores audio information for future playback.
 *
 * @since 2025/05/05
 */
@Api
public interface MediaSound
	extends MediaResource
{
	/** Property key which controls 3D sound. */
	@Api
	String AUDIO_3D_RESOURCES =
		"3d.resources";
}
