// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

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
	
	/** SVG. */
	int LOAD_SVG =
		16;
	
	/** All types. */
	int ALL_TYPES =
		NativeImageLoadType.LOAD_PNG |
		NativeImageLoadType.LOAD_GIF | NativeImageLoadType.LOAD_JPEG |
		NativeImageLoadType.LOAD_XPM | NativeImageLoadType.LOAD_SVG;
}
