// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

/**
 * This represents the type of dependency that is utilized.
 *
 * @since 2017/11/17
 */
public enum DependencyType
{
	/** Microedition Configuration. */
	MICROEDITION_CONFIGURATION,
	
	/** Microedition profile. */
	MICROEDITION_PROFILE,
	
	/** Liblet. */
	LIBLET,
	
	/** A standard via a standard string. */
	STANDARD,
	
	/** A service descriptor. */
	SERVICE,
	
	/** SquirrelJME specific project. */
	SQUIRRELJME_PROJECT,
	
	/** End. */
	;
}

