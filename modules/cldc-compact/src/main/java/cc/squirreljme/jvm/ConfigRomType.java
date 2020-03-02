// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This represents an option in the configuration ROM.
 *
 * @since 2019/06/14
 */
public interface ConfigRomType
{
	/** End of configuration. */
	byte END =
		0;
	
	/** Java VM Version. */
	byte JAVA_VM_VERSION =
		1;
	
	/** Java VM Name. */
	byte JAVA_VM_NAME =
		2;
	
	/** Java VM Vendor. */
	byte JAVA_VM_VENDOR =
		3;
	
	/** Java VM E-Mail. */
	byte JAVA_VM_EMAIL =
		4;
	
	/** Java VM URL. */
	byte JAVA_VM_URL =
		5;
	
	/** The guest depth. */
	byte GUEST_DEPTH =
		6;
	
	/** Main class. */
	byte MAIN_CLASS =
		7;
	
	/** Main program arguments. */
	byte MAIN_ARGUMENTS =
		8;
	
	/** Is this a MIDlet? */
	byte IS_MIDLET =
		9;
	
	/** Define system propertly. */
	byte DEFINE_PROPERTY =
		10;
	
	/** Classpath to use. */
	byte CLASS_PATH =
		11;
	
	/** System call static field pointer. */
	byte SYSCALL_STATIC_FIELD_POINTER =
		12;
	
	/** System call method pointer. */
	byte SYSCALL_CODE_POINTER =
		13;
	
	/** System call pool pointer. */
	byte SYSCALL_POOL_POINTER =
		14;
	
	/** Number of available options. */
	byte NUM_OPTIONS =
		15;
}

