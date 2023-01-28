// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;
import java.security.AccessController;

/**
 * Represents the type of description used for the VM.
 *
 * @since 2020/06/17
 */
@Exported
public interface VMDescriptionType
{
	/** Unspecified. */
	@Exported
	byte UNSPECIFIED =
		0;
	
	/** The VM version. */
	@Exported
	byte VM_VERSION =
		1;
	
	/** The VM name. */
	@Exported
	byte VM_NAME =
		2;
	
	/** The VM Vendor. */
	@Exported
	byte VM_VENDOR =
		3;
	
	/** The VM E-mail. */
	@Exported
	byte VM_EMAIL =
		4;
	
	/** The VM URL. */
	@Exported
	byte VM_URL =
		5;
	
	/** The executable path of the VM. */
	@Exported
	byte EXECUTABLE_PATH =
		6;
	
	/** The operating system name. */
	@Exported
	byte OS_NAME =
		7;
	
	/** The operating system version. */
	@Exported
	byte OS_VERSION =
		8;
	
	/** The operating system architecture. */
	@Exported
	byte OS_ARCH =
		9;
	
	/**
	 * The current virtual machine security policy, this is used by
	 * {@link AccessController}.
	 */
	@Exported
	byte VM_SECURITY_POLICY =
		10;
	
	/** The current number of properties. */
	@Exported
	byte NUM_TYPES =
		11;
}
