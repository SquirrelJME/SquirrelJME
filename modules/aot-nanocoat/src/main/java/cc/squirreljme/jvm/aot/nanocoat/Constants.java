// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CFileName;
import cc.squirreljme.c.CIdentifier;

/**
 * Generic constants for NanoCoat.
 *
 * @since 2023/06/04
 */
public interface Constants
{
	/** JNI header file. */
	CFileName SJME_JNI_HEADER =
		CFileName.of("sjmejni.h");
	
	/** Guard for header/source code. */
	CIdentifier CODE_GUARD =
		CIdentifier.of("SJME_C_CH");
}
