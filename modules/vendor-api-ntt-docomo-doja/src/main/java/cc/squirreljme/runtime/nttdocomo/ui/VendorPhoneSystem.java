// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.ui;

import com.nttdocomo.ui.PhoneSystem;

/**
 * Vendor specific phone system IDs.
 *
 * @see PhoneSystem
 * @since 2022/02/14
 */
public interface VendorPhoneSystem
{
	/** Vibrate attribute for F503i and So503i. */
	byte VIBRATE_ATTRIBUTE_F503I_SO503I = 64;
	
	/** Vibration attribute for P503i.. */
	byte VIBRATE_ATTRIBUTE_P503I = 120;
}
