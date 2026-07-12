// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Compatibility flags which violate Java ME standards and are very specific
 * to single sets of devices and/or applications. These options should rarely,
 * if ever, be used and are always disabled by default. Any use of these
 * flags indicates a defect in the application you are using, it is
 * recommended to contact the vendor of that application and request a fix.
 * 
 * New IDs are always incremental and not a bit-field, due to the large number
 * of potential devices, applications, and flags.
 *
 * @since 2025/11/27
 */
@SquirrelJMEVendorApi
public interface CompatibilityId
{
	/** Unknown compatibility flag. */
	@SquirrelJMEVendorApi
	byte UNKNOWN =
		0;
	
	/**
	 * {@squirreljme.compatibility 1 Some Konami demos perform some checks
	 * to ensure that they are running only on specific Konami demo devices.
	 * If enabled, this enables support for those checks.}
	 */
	@SquirrelJMEVendorApi
	byte KONAMI_DEMO_CHECK =
		1;
	
	/**
	 * {@squirreljme.compatibility 2 Some titles violate the LCDUI
	 * specification by illegally storing the Graphics context when they
	 * should not, this forces a buffer to be used.}
	 */
	@SquirrelJMEVendorApi
	byte FORCE_LCDUI_BUFFER =
		2;
}
