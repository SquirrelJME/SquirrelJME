// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system;

/**
 * This class contains the list of functions which are available to the client
 * processes when it needs to interact with the kernel or perform an action.
 *
 * System functions are either local or require access to the kernel. Local
 * ones may be implemented locally, but they could potentially go to the
 * kernel accordingly.
 *
 * @since 2018/02/21
 */
public enum SystemFunction
{
	/** Invoke public static void main method of a class. */
	INVOKE_STATIC_MAIN,
	
	/** End. */
	;
}

