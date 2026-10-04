// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;
import java.security.AccessController;

/**
 * Represents the type of description used for the VM.
 *
 * @since 2020/06/17
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface VMDescriptionType
{
	/** Unspecified. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNSPECIFIED =
		0;
	
	/** The VM version. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VM_VERSION =
		1;
	
	/** The VM name. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VM_NAME =
		2;
	
	/** The VM Vendor. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VM_VENDOR =
		3;
	
	/** The VM E-mail. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VM_EMAIL =
		4;
	
	/** The VM URL. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VM_URL =
		5;
	
	/** The executable path of the VM. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte EXECUTABLE_PATH =
		6;
	
	/** The operating system name. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte OS_NAME =
		7;
	
	/** The operating system version. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte OS_VERSION =
		8;
	
	/** The operating system architecture. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte OS_ARCH =
		9;
	
	/**
	 * The current virtual machine security policy, this is used by
	 * {@link AccessController}.
	 */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VM_SECURITY_POLICY =
		10;
	
	/** Single lines of legal text and copyrights used for ports. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte THIRD_PARTY_LEGAL_LINE =
		11;
	
	/** Full document of legal text, with entire licenses. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte THIRD_PARTY_LEGAL_DOCUMENT =
		12;
	
	/** The path separator used. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte PATH_SEPARATOR =
		13;
	
	/** The virtual machine info. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte VM_INFO =
		14;
	
	/** Unknown. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_UNKNOWN =
		15;
	
	/** The cache directory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_CACHE = 
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 1;
	
	/** The config directory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_CONFIG =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 2;
	
	/** The data directory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_DATA =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 3;
	
	/** The state directory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_STATE =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 4;
	
	/** The native library directory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_NATIVES =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 5;
	
	/** Executable directory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_EXEC =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 6;
	
	/** Temporary directory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_TEMPORARY =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 7;
	
	/** The libraries directory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_LIBRARIES =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 8;
	
	/** The non-volatile storage directory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_BUCKET_DATA =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 9;
	
	/** The extra bucket directory. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_BUCKET_EXTRA =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 10;
	
	/** The number of default directory types. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_NUM_TYPES =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 11;
	
	/** Default directory reserved: 12. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_RESERVED_12 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 12;
	
	/** Default directory reserved: 13. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_RESERVED_13 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 13;
	
	/** Default directory reserved: 14. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_RESERVED_14 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 14;
	
	/** Default directory reserved: 15. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_RESERVED_15 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 15;
	
	/** Default directory reserved: 16. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_RESERVED_16 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 16;
	
	/** Default directory reserved: 17. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_RESERVED_17 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 17;
	
	/** Default directory reserved: 18. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_RESERVED_18 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 18;
	
	/** Default directory reserved: 19. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_RESERVED_19 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 19;
	
	/** Default directory reserved: 20. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_RESERVED_20 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 20;
	
	/** The number of reserved directories. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte DEFAULT_DIR_NUM_RESERVED =
		36;
	
	/** The SquirrelJME Native API Version. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SQUIRRELJME_API_VERSION =
		37;
	
	/** The current number of properties. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_TYPES =
		38;
}
