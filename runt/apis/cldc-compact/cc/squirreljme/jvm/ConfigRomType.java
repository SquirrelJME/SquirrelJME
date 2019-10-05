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
	public static final byte END =
		0;
	
	/** Java VM Version. */
	public static final byte JAVA_VM_VERSION =
		1;
	
	/** Java VM Name. */
	public static final byte JAVA_VM_NAME =
		2;
	
	/** Java VM Vendor. */
	public static final byte JAVA_VM_VENDOR =
		3;
	
	/** Java VM E-Mail. */
	public static final byte JAVA_VM_EMAIL =
		4;
	
	/** Java VM URL. */
	public static final byte JAVA_VM_URL =
		5;
	
	/** The guest depth. */
	public static final byte GUEST_DEPTH =
		6;
	
	/** Main class. */
	public static final byte MAIN_CLASS =
		7;
	
	/** Main program arguments. */
	public static final byte MAIN_ARGUMENTS =
		8;
	
	/** Is this a MIDlet? */
	public static final byte IS_MIDLET =
		9;
	
	/** Define system propertly. */
	public static final byte DEFINE_PROPERTY =
		10;
	
	/** Classpath to use. */
	public static final byte CLASS_PATH =
		11;
	
	/** System call static field pointer. */
	public static final byte SYSCALL_STATIC_FIELD_POINTER =
		12;
	
	/** System call method pointer. */
	public static final byte SYSCALL_CODE_POINTER =
		13;
	
	/** System call pool pointer. */
	public static final byte SYSCALL_POOL_POINTER =
		14;
	
	/** Number of available options. */
	public static final byte NUM_OPTIONS =
		15;
}

