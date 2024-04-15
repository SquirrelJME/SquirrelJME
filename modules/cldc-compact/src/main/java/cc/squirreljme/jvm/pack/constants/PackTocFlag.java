// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.pack.constants;

import cc.squirreljme.jvm.pack.PackRom;
import cc.squirreljme.jvm.pack.TableOfContents;

/**
 * Flags for {@link TableOfContents} in {@link PackRom}.
 *
 * @since 2021/09/03
 */
public interface PackTocFlag
{
	/**
	 * Resource item, not a JAR but a data file, this can be a manifest file
	 * for i-Mode for example.
	 */
	byte RESOURCE =
		1;
}
