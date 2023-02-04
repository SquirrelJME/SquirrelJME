// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.pack.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Represents a property of a pack file.
 *
 * @since 2020/12/09
 */
@Exported
public interface PackProperty
{
	/** The property based version ID. */
	byte INT_PACK_VERSION_ID =
		0;
	
	/** Number of JARs. */
	@Exported
	byte COUNT_TOC =
		1;
	
	/** Table of contents offset. */
	@Exported
	byte OFFSET_TOC =
		2;
	
	/** Table of contents size. */
	@Exported
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
	@Exported
	byte TIME_DATE_HIGH =
		5;
	
	/** Epoch millis of creation date (low). */
	@Exported
	byte TIME_DATE_LOW =
		6;
	
	/** The size of the entire ROM. */
	@Exported
	byte ROM_SIZE =
		7;
	
	/** The main class for the launcher. */
	@Exported
	byte STRING_LAUNCHER_MAIN_CLASS =
		8;
	
	/** The arguments to initialize the launcher. */
	@Exported
	byte STRINGS_LAUNCHER_ARGS =
		9;
	
	/** The number of launcher args that exist. */
	@Exported
	byte COUNT_LAUNCHER_ARGS =
		10;
	
	/** The class path to initialize the launcher. */
	@Exported
	byte INTEGERS_LAUNCHER_CLASSPATH =
		11;
	
	/** The number of class path entries for the launcher. */
	@Exported
	byte COUNT_LAUNCHER_CLASSPATH =
		12;
	
	/** The main class for the boot loader. */
	@Exported
	byte STRING_BOOTLOADER_MAIN_CLASS =
		13;
	
	/** The class path for the boot loader. */
	@Exported
	byte INTEGERS_BOOTLOADER_CLASSPATH =
		14;
	
	/** The number of items in the boot loader class path. */
	@Exported
	byte COUNT_BOOTLOADER_CLASSPATH =
		15;
	
	/** One of {@link PackFlag}. */
	@Exported
	byte BITFIELD_PACK_FLAGS =
		16;
	
	/** The number of pack properties. */
	@Exported
	byte NUM_PACK_PROPERTIES =
		17;
}
