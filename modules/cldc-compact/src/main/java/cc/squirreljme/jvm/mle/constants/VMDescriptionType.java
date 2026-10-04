// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import java.security.AccessController;

/**
 * Represents the type of description used for the VM.
 *
 * @since 2020/06/17
 */
public interface VMDescriptionType
{
	/** Unspecified. */
	byte UNSPECIFIED =
		0;
	
	/** The VM version. */
	byte VM_VERSION =
		1;
	
	/** The VM name. */
	byte VM_NAME =
		2;
	
	/** The VM Vendor. */
	byte VM_VENDOR =
		3;
	
	/** The VM E-mail. */
	byte VM_EMAIL =
		4;
	
	/** The VM URL. */
	byte VM_URL =
		5;
	
	/** The executable path of the VM. */
	byte EXECUTABLE_PATH =
		6;
	
	/** The operating system name. */
	byte OS_NAME =
		7;
	
	/** The operating system version. */
	byte OS_VERSION =
		8;
	
	/** The operating system architecture. */
	byte OS_ARCH =
		9;
	
	/**
	 * The current virtual machine security policy, this is used by
	 * {@link AccessController}.
	 */
	byte VM_SECURITY_POLICY =
		10;
	
	/** Single lines of legal text and copyrights used for ports. */
	byte THIRD_PARTY_LEGAL_LINE =
		11;
	
	/** Full document of legal text, with entire licenses. */
	byte THIRD_PARTY_LEGAL_DOCUMENT =
		12;
	
	/** The path separator used. */
	byte PATH_SEPARATOR =
		13;
	
	/** The virtual machine info. */
	byte VM_INFO =
		14;
	
	/** Unknown. */
	byte DEFAULT_DIR_UNKNOWN =
		15;
	
	/** The cache directory. */
	byte DEFAULT_DIR_CACHE = 
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 1;
	
	/** The config directory. */
	byte DEFAULT_DIR_CONFIG =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 2;
	
	/** The data directory. */
	byte DEFAULT_DIR_DATA =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 3;
	
	/** The state directory. */
	byte DEFAULT_DIR_STATE =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 4;
	
	/** The native library directory. */
	byte DEFAULT_DIR_NATIVES =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 5;
	
	/** Executable directory. */
	byte DEFAULT_DIR_EXEC =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 6;
	
	/** Temporary directory. */
	byte DEFAULT_DIR_TEMPORARY =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 7;
	
	/** The libraries directory. */
	byte DEFAULT_DIR_LIBRARIES =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 8;
	
	/** The non-volatile storage directory. */
	byte DEFAULT_DIR_BUCKET_DATA =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 9;
	
	/** The extra bucket directory. */
	byte DEFAULT_DIR_BUCKET_EXTRA =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 10;
	
	/** The number of default directory types. */
	byte DEFAULT_DIR_NUM_TYPES =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 11;
	
	/** Default directory reserved: 12. */
	byte DEFAULT_DIR_RESERVED_12 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 12;
	
	/** Default directory reserved: 13. */
	byte DEFAULT_DIR_RESERVED_13 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 13;
	
	/** Default directory reserved: 14. */
	byte DEFAULT_DIR_RESERVED_14 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 14;
	
	/** Default directory reserved: 15. */
	byte DEFAULT_DIR_RESERVED_15 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 15;
	
	/** Default directory reserved: 16. */
	byte DEFAULT_DIR_RESERVED_16 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 16;
	
	/** Default directory reserved: 17. */
	byte DEFAULT_DIR_RESERVED_17 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 17;
	
	/** Default directory reserved: 18. */
	byte DEFAULT_DIR_RESERVED_18 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 18;
	
	/** Default directory reserved: 19. */
	byte DEFAULT_DIR_RESERVED_19 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 19;
	
	/** Default directory reserved: 20. */
	byte DEFAULT_DIR_RESERVED_20 =
		VMDescriptionType.DEFAULT_DIR_UNKNOWN + 20;
	
	/** The number of reserved directories. */
	byte DEFAULT_DIR_NUM_RESERVED =
		36;
	
	/** The current number of properties. */
	byte NUM_TYPES =
		37;
}
