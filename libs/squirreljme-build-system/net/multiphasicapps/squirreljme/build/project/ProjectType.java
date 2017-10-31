// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.project;

/**
 * This represents the type of library or application that the project is, this
 * determines how it is used and depended upon.
 *
 * @since 2017/10/31
 */
public enum ProjectType
{
	/** APIs which implement configurations, profiles, and standards. */
	API,
	
	/** Liblets which are only included by midlets and APIs. */
	LIBLET,
	
	/** Midlets which are actual applications. */
	MIDLET,
	
	/** End. */
	;
}

