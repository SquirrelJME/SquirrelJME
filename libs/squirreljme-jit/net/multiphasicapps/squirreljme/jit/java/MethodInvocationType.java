// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

/**
 * This represents the type of invocation that may be performed on a method.
 *
 * @since 2017/09/16
 */
public enum MethodInvocationType
{
	/** Interface method. */
	INTERFACE,
	
	/** Special call. */
	SPECIAL,
	
	/** Static call. */
	STATIC,
	
	/** Virtual call. */
	VIRTUAL,
	
	;
}

