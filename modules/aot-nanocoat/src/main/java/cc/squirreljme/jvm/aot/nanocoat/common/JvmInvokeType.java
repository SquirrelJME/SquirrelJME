// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.common;

/**
 * The type of invocation being performed.
 *
 * @since 2023/08/17
 */
public enum JvmInvokeType
{
	/** Static. */
	STATIC,
	
	/** Virtual (normal invocation). */
	VIRTUAL,
	
	/** Interface. */
	INTERFACE,
	
	/** Special. */
	SPECIAL,
	
	/* End. */
	;
}
