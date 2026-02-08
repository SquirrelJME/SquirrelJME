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
import java.security.AccessController;

/**
 * Represents the type of description used for the VM.
 *
 * @since 2020/06/17
 */
@SquirrelJMEVendorApi
public interface VMDescriptionType
{
	/** Unspecified. */
	@SquirrelJMEVendorApi
	byte UNSPECIFIED =
		0;
	
	/** The VM version. */
	@SquirrelJMEVendorApi
	byte VM_VERSION =
		1;
	
	/** The VM name. */
	@SquirrelJMEVendorApi
	byte VM_NAME =
		2;
	
	/** The VM Vendor. */
	@SquirrelJMEVendorApi
	byte VM_VENDOR =
		3;
	
	/** The VM E-mail. */
	@SquirrelJMEVendorApi
	byte VM_EMAIL =
		4;
	
	/** The VM URL. */
	@SquirrelJMEVendorApi
	byte VM_URL =
		5;
	
	/** The executable path of the VM. */
	@SquirrelJMEVendorApi
	byte EXECUTABLE_PATH =
		6;
	
	/** The operating system name. */
	@SquirrelJMEVendorApi
	byte OS_NAME =
		7;
	
	/** The operating system version. */
	@SquirrelJMEVendorApi
	byte OS_VERSION =
		8;
	
	/** The operating system architecture. */
	@SquirrelJMEVendorApi
	byte OS_ARCH =
		9;
	
	/**
	 * The current virtual machine security policy, this is used by
	 * {@link AccessController}.
	 */
	@SquirrelJMEVendorApi
	byte VM_SECURITY_POLICY =
		10;
	
	/** Single lines of legal text and copyrights used for ports. */
	@SquirrelJMEVendorApi
	byte THIRD_PARTY_LEGAL_LINE =
		11;
	
	/** Full document of legal text, with entire licenses. */
	@SquirrelJMEVendorApi
	byte THIRD_PARTY_LEGAL_DOCUMENT =
		12;
	
	/** The path separator used. */
	@SquirrelJMEVendorApi
	byte PATH_SEPARATOR =
		13;
	
	/** The virtual machine info. */
	@SquirrelJMEVendorApi
	byte VM_INFO =
		14;
	
	/** Unknown. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_UNKNOWN =
		15;
	
	/** The cache directory. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_CACHE = 
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 1;
	
	/** The config directory. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_CONFIG =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 2;
	
	/** The data directory. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_DATA =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 3;
	
	/** The state directory. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_STATE =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 4;
	
	/** The native library directory. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_NATIVES =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 5;
	
	/** Executable directory. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_EXEC =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 6;
	
	/** Temporary directory. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_TEMPORARY =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 7;
	
	/** The libraries directory. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_LIBRARIES =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 8;
	
	/** The non-volatile storage directory. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_BUCKET_DATA =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 9;
	
	/** The extra bucket directory. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_BUCKET_EXTRA =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 10;
	
	/** The number of default directory types. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_NUM_TYPES =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 11;
	
	/** Default directory reserved: 12. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_RESERVED_12 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 12;
	
	/** Default directory reserved: 13. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_RESERVED_13 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 13;
	
	/** Default directory reserved: 14. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_RESERVED_14 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 14;
	
	/** Default directory reserved: 15. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_RESERVED_15 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 15;
	
	/** Default directory reserved: 16. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_RESERVED_16 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 16;
	
	/** Default directory reserved: 17. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_RESERVED_17 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 17;
	
	/** Default directory reserved: 18. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_RESERVED_18 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 18;
	
	/** Default directory reserved: 19. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_RESERVED_19 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 19;
	
	/** Default directory reserved: 20. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_RESERVED_20 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 20;
	
	/** The number of reserved directories. */
	@SquirrelJMEVendorApi
	byte DEFAULT_DIR_NUM_RESERVED =
		36;
	
	/** The current number of properties. */
	@SquirrelJMEVendorApi
	byte NUM_TYPES =
		37;
}
