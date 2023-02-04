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
 * Properties for Jar table of contents.
 *
 * @since 2020/12/08
 */
@Exported
public interface JarTocProperty
{
	/** Flags for the property. */
	@Exported
	byte INT_FLAGS =
		0;
	
	/** Hash code of the entry. */
	@Exported
	byte HASHCODE_NAME =
		1;
	
	/** The name of the resource. */
	@Exported
	byte OFFSET_NAME =
		2;
	
	/** The size of the resource name. */
	@Exported
	byte SIZE_NAME =
		3;
	
	/** Offset to the resource data. */
	@Exported
	byte OFFSET_DATA =
		4;
	
	/** The size of the resource data. */
	@Exported
	byte SIZE_DATA =
		5;
	
	/** The properties available. */
	@Exported
	byte NUM_JAR_TOC_PROPERTIES =
		6;
}
