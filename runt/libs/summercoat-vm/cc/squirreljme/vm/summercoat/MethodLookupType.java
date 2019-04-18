// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This represents the type of lookup to perform when locating a method.
 *
 * @since 2019/01/10
 */
public enum MethodLookupType
{
	/** As instance method, invocation starts at the object class. */
	INSTANCE,
	
	/** As purely static method. */
	STATIC,
	
	/** Super method, start at the specified class in the chain and go down. */
	SUPER,
	
	/** End. */
	;
}

