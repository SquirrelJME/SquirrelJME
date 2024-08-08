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
	
	/** The current number of properties. */
	@SquirrelJMEVendorApi
	byte NUM_TYPES =
		13;
}
