// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.pack.constants;

import cc.squirreljme.jvm.pack.JarRom;
import cc.squirreljme.jvm.pack.TableOfContents;
import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Flags for {@link TableOfContents} in {@link JarRom}.
 *
 * @since 2020/12/08
 */
@Exported
public interface JarTocFlag
{
	/** A class that can be executed. */
	@Exported
	byte EXECUTABLE_CLASS =
		1;
	
	/** A resource entry. */
	@Exported
	byte RESOURCE =
		2;
	
	/** Manifest resource. */
	@Exported
	byte MANIFEST =
		4;
	
	/** Is this a boot class? */
	byte BOOT =
		8;
	
	/** Is a standard class file and not a SummerCoat class. */
	@Exported
	byte STANDARD_CLASS =
		16;
	
	/** Compressed entry? */
	byte COMPRESSED =
		32;
}
