// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Represents the types of images that are supported in image load.
 *
 * @since 2021/12/05
 */
@SquirrelJMEVendorApi
public interface NativeImageLoadType
{
	/** Native loading of PNGs. */
	@SquirrelJMEVendorApi
	int LOAD_PNG =
		1;
	
	/** Native loading of GIFs. */
	@SquirrelJMEVendorApi
	int LOAD_GIF =
		2;
	
	/** Native loading of JPEGs. */
	@SquirrelJMEVendorApi
	int LOAD_JPEG =
		4;
	
	/** Native loading of XPMs. */
	@SquirrelJMEVendorApi
	int LOAD_XPM =
		8;
	
	/** SVG. */
	@SquirrelJMEVendorApi
	int LOAD_SVG =
		16;
	
	/** All types. */
	@SquirrelJMEVendorApi
	int ALL_TYPES =
		NativeImageLoadType.LOAD_PNG |
		NativeImageLoadType.LOAD_GIF | NativeImageLoadType.LOAD_JPEG |
		NativeImageLoadType.LOAD_XPM | NativeImageLoadType.LOAD_SVG;
}
