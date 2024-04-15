// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.pack.constants;

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
	
	/** The hash code for the JAR name. */
	byte HASHCODE_NAME =
		4;
	
	/** The offset to the name of this JAR. */
	byte OFFSET_NAME =
		5;
	
	/** The size of the JAR name. */
	byte SIZE_NAME =
		6;
	
	/** The manifest index. */
	byte RCDX_MANIFEST =
		7;
	
	/** Boot initializer offset. */
	byte OFFSET_BOOT_INIT =
		8;
	
	/** Boot initializer size. */
	byte SIZE_BOOT_INIT =
		9;
	
	/** The entry pool for the starting class, used to reference data. */
	byte MEMHANDLEID_START_POOL =
		10;
	
	/**
	  * The class with the start method
	  * ({@link StaticClassProperty#INDEX_BOOT_METHOD}.
	  */
	byte RCDX_START_CLASS =
		11;
	
	/** Static constant pool offset. */
	byte OFFSET_STATIC_POOL =
		12;
	
	/** Static constant pool size. */
	byte SIZE_STATIC_POOL =
		13;
	
	/** Runtime constant pool offset. */
	byte OFFSET_RUNTIME_POOL =
		14;
	
	/** Runtime constant pool size. */
	byte SIZE_RUNTIME_POOL =
		15;
	
	/** The base allocation size of arrays. */
	byte SIZE_BASE_ARRAY =
		16;
	
	/** Attributes for the virtual machine. */
	byte MEMHANDLEID_VM_ATTRIBUTES =
		17;
	
	/** The number of properties in the JAR. */
	byte NUM_JAR_PROPERTIES =
		18;
}
