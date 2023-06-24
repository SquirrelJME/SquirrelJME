// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.common;

import cc.squirreljme.c.CFileName;
import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.c.std.CStdIntNumberType;

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
	
	/** {@code jint} conversion. */
	CStdIntNumberType JINT_C =
		CStdIntNumberType.INT32;
	
	/** {@code jlong} conversion. */
	CStdIntNumberType JLONG_C =
		CStdIntNumberType.INT32;
}
