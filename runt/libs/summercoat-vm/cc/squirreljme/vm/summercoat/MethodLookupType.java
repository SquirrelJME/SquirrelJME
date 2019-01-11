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
	/** As instance method, if it is one. */
	INSTANCE,
	
	/** As purely static method. */
	STATIC,
	
	/** End. */
	;
}

