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
 * Represents the types of images that are supported in image load.
 *
 * @since 2021/12/05
 */
public interface NativeImageLoadType
{
	/** Native loading of PNGs. */
	int LOAD_PNG =
		1;
	
	/** Native loading of GIFs. */
	int LOAD_GIF =
		2;
	
	/** Native loading of JPEGs. */
	int LOAD_JPEG =
		4;
	
	/** Native loading of XPMs. */
	int LOAD_XPM =
		8;
	
	/** All types. */
	int ALL_TYPES =
		NativeImageLoadType.LOAD_PNG |
		NativeImageLoadType.LOAD_GIF | NativeImageLoadType.LOAD_JPEG |
		NativeImageLoadType.LOAD_XPM;
}
