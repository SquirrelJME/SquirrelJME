// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.common;

import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CExpression;
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
	CFileName SJME_NVM_HEADER =
		CFileName.of("sjme/nvm.h");
	
	/** {@code jint} conversion. */
	CStdIntNumberType JINT_C =
		CStdIntNumberType.INT32;
	
	/** {@code jlong} conversion. */
	CStdIntNumberType JLONG_C =
		CStdIntNumberType.INT32;
	
	/** False. */
	CIdentifier FALSE =
		CIdentifier.of("JNI_FALSE");
	
	/** False. */
	CIdentifier TRUE =
		CIdentifier.of("JNI_TRUE");
	
	/** Temporary value. */
	CIdentifier TEMPORARY =
		CIdentifier.of("temp");
	
	/** Start of NanoCoat call? */
	CIdentifier SJME_NANOCOAT_START_CALL =
		CIdentifier.of("SJME_NANOCOAT_START_CALL");
	
	/** End of NanoCoat call? */
	CIdentifier SJME_NANOCOAT_END_CALL =
		CIdentifier.of("SJME_NANOCOAT_END_CALL");
	
	/** Zero value. */
	CExpression ZERO =
		CBasicExpression.of("0");
	
	/** Label for the method outro. */
	CIdentifier OUTRO_LABEL =
		CIdentifier.of("outro");
	
	/** The label for the throw handler. */
	CIdentifier THROW_HANDLER_LABEL =
		CIdentifier.of("throwHandler");
}
