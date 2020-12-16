// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.constants;

/**
 * Properties that are associated with JAR files.
 *
 * @since 2020/12/07
 */
public interface JarProperty
{
	/** The property based version ID. */
	byte INT_JAR_VERSION_ID =
		0;
	
	/** Number of resources. */
	byte COUNT_TOC =
		1;
	
	/** Table of contents offset. */
	byte OFFSET_TOC =
		2;
	
	/** Table of contents size. */
	byte SIZE_TOC =
		3;
	
	/** The manifest index. */
	byte RCDX_MANIFEST =
		4;
	
	/** Boot initializer offset. */
	byte OFFSET_BOOT_INIT =
		5;
	
	/** Boot initializer size. */
	byte OFFSET_BOOT_SIZE =
		6;
	
	/** The entry pool for the starting class, used to reference data. */
	byte MEMHANDLEID_START_POOL =
		7;
	
	/**
	  * The class with the start method
	  * ({@link StaticClassProperty#INT_BOOT_METHOD_INDEX}.
	  */
	byte RCDX_START_CLASS =
		8;
	
	/** Static constant pool offset. */
	byte OFFSET_STATIC_POOL =
		9;
	
	/** Static constant pool size. */
	byte SIZE_STATIC_POOL =
		10;
	
	/** Runtime constant pool offset. */
	byte OFFSET_RUNTIME_POOL =
		11;
	
	/** Runtime constant pool size. */
	byte SIZE_RUNTIME_POOL =
		12;
	
	/** The number of properties in the JAR. */
	byte NUM_JAR_PROPERTIES =
		13;
}
