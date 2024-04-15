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
	
	/** The current number of properties. */
	byte NUM_TYPES =
		13;
}
