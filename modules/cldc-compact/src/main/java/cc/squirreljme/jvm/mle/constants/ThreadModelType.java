// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * This represents the thread model type.
 *
 * @since 2021/05/07
 */
@Exported
public interface ThreadModelType
{
	/** Invalid model. */
	@Exported
	byte INVALID =
		0;
	
	/** Single cooperatively threaded. */
	@Exported
	byte SINGLE_COOP_THREAD =
		1;
	
	/** Simultaneous Multi-threaded. */
	@Exported
	byte SIMULTANEOUS_MULTI_THREAD =
		2;
	
	/** The number of threading models. */
	@Exported
	byte NUM_MODELS =
		3;
}
