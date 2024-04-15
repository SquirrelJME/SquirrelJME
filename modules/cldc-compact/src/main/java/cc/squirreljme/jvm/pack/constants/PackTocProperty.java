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
 * Properties for the pack file table of content entries.
 *
 * @since 2020/12/12
 */
public interface PackTocProperty
{
	/** Flags for the JAR, a set of {@link PackTocFlag}. */
	byte INT_FLAGS =
		0;
	
	/** Hash code of the entry. */
	byte HASHCODE_NAME =
		1;
	
	/** The name of the library. */
	byte OFFSET_NAME =
		2;
	
	/** The size of the library name. */
	byte SIZE_NAME =
		3;
	
	/** Offset to the JAR data. */
	byte OFFSET_DATA =
		4;
	
	/** The size of the JAR data. */
	byte SIZE_DATA =
		5;
	
	/** The properties available. */
	byte NUM_PACK_TOC_PROPERTIES =
		6;
}
