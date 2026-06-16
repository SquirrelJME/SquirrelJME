// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import com.nttdocomo.ui.PhoneSystem;

/**
 * Vendor specific phone system IDs.
 *
 * @see PhoneSystem
 * @since 2022/02/14
 */
@SquirrelJMEVendorApi
public interface VendorPhoneSystem
{
	/** Vibrate attribute for F503i and So503i. */
	@SquirrelJMEVendorApi
	byte VIBRATE_ATTRIBUTE_F503I_SO503I = 
		64;
	
	/** Vibration attribute for P503i.. */
	@SquirrelJMEVendorApi
	byte VIBRATE_ATTRIBUTE_P503I = 
		120;
}
