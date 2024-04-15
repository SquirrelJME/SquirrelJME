// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.pack.constants;

/**
 * Represents a property of a pack file.
 *
 * @since 2020/12/09
 */
public interface PackProperty
{
	/** The property based version ID. */
	byte INT_PACK_VERSION_ID =
		0;
	
	/** Number of JARs. */
	byte COUNT_TOC =
		1;
	
	/** Table of contents offset. */
	byte OFFSET_TOC =
		2;
	
	/** Table of contents size. */
	byte SIZE_TOC =
		3;
	
	/**
	 * The index of the boot JAR.
	 * 
	 * @deprecated This is being replaced by better means of specifying the
	 * boot JAR and otherwise.
	 */
	@Deprecated
	byte INDEX_BOOT_JAR =
		4;
	
	/** Epoch millis of creation date (high). */
	byte TIME_DATE_HIGH =
		5;
	
	/** Epoch millis of creation date (low). */
	byte TIME_DATE_LOW =
		6;
	
	/** The size of the entire ROM. */
	byte ROM_SIZE =
		7;
	
	/** The main class for the launcher. */
	byte STRING_LAUNCHER_MAIN_CLASS =
		8;
	
	/** The arguments to initialize the launcher. */
	byte STRINGS_LAUNCHER_ARGS =
		9;
	
	/** The number of launcher args that exist. */
	byte COUNT_LAUNCHER_ARGS =
		10;
	
	/** The class path to initialize the launcher. */
	byte INTEGERS_LAUNCHER_CLASSPATH =
		11;
	
	/** The number of class path entries for the launcher. */
	byte COUNT_LAUNCHER_CLASSPATH =
		12;
	
	/** The main class for the boot loader. */
	byte STRING_BOOTLOADER_MAIN_CLASS =
		13;
	
	/** The class path for the boot loader. */
	byte INTEGERS_BOOTLOADER_CLASSPATH =
		14;
	
	/** The number of items in the boot loader class path. */
	byte COUNT_BOOTLOADER_CLASSPATH =
		15;
	
	/** One of {@link PackFlag}. */
	byte BITFIELD_PACK_FLAGS =
		16;
	
	/** The number of pack properties. */
	byte NUM_PACK_PROPERTIES =
		17;
}
