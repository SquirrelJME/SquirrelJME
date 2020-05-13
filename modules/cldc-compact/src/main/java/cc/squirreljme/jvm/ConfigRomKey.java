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
public interface ConfigRomKey
{
	/** End of configuration. */
	byte END =
		0;
	
	/**
	 * Java VM Version.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#UTF}
	 * @since 2019/06/14
	 */
	byte JAVA_VM_VERSION =
		1;
	
	/**
	 * Java VM Name.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#UTF}
	 * @since 2019/06/14
	 */
	byte JAVA_VM_NAME =
		2;
	
	/**
	 * Java VM Vendor.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#UTF}
	 * @since 2019/06/14
	 */
	byte JAVA_VM_VENDOR =
		3;
	
	/**
	 * Java VM E-Mail.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#UTF}
	 * @since 2019/06/14
	 */
	byte JAVA_VM_EMAIL =
		4;
	
	/**
	 * Java VM URL.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#UTF}
	 * @since 2019/06/14
	 */
	byte JAVA_VM_URL =
		5;
	
	/**
	 * The guest depth.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#INTEGER}
	 * @since 2019/06/14
	 */
	@Deprecated
	byte GUEST_DEPTH =
		6;
	
	/**
	 * Main class.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#UTF}
	 * @since 2019/06/14
	 */
	byte MAIN_CLASS =
		7;
	
	/**
	 * Main program arguments.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#UTF_LIST}
	 * @since 2019/06/14
	 */
	byte MAIN_ARGUMENTS =
		8;
	
	/**
	 * Is this a MIDlet being launched?
	 *
	 * @squirreljme.configtype {@link ConfigRomType#BOOLEAN}
	 * @since 2019/06/14
	 */
	byte IS_MIDLET =
		9;
	
	/**
	 * Define system property.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#KEY_VALUE_PAIR}
	 * @since 2019/06/14
	 */
	byte DEFINE_PROPERTY =
		10;
	
	/**
	 * Classpath to use.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#UTF_LIST}
	 * @since 2019/06/14
	 */
	byte CLASS_PATH =
		11;
	
	/**
	 * System call static field pointer.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#LONG}
	 * @since 2019/06/14
	 */
	byte SYSCALL_STATIC_FIELD_POINTER =
		12;
	
	/**
	 * System call method pointer.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#LONG}
	 * @since 2019/06/14
	 */
	byte SYSCALL_CODE_POINTER =
		13;
	
	/**
	 * System call pool pointer.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#LONG}
	 * @since 2019/06/14
	 */
	byte SYSCALL_POOL_POINTER =
		14;
	
	/**
	 * The built-in encoding to use, see {@link BuiltInEncoding}.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#INTEGER}
	 * @since 2019/06/14
	 */
	byte BUILT_IN_ENCODING =
		15;
	
	/**
	 * The MicroEdition Configuration used, see {@link MicroeditionConfig}.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#INTEGER}
	 * @since 2019/06/14
	 */
	byte MICROEDITION_CONFIG =
		16;
	
	/**
	 * The type of line ending to use, see {@link LineEndingType}.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#INTEGER}
	 * @since 2019/06/14
	 */
	byte LINE_ENDING =
		17;
	
	/**
	 * The built-in locale the system is using, see {@link BuiltInLocale}.
	 *
	 * @squirreljme.configtype {@link ConfigRomType#INTEGER}.
	 * @since 2020/05/12
	 */
	byte BUILT_IN_LOCALE =
		18;
	
	/** Number of available configuration options. */
	byte NUM_OPTIONS =
		19;
}

