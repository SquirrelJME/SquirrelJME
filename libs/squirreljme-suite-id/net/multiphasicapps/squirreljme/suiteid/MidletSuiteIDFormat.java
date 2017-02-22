// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.suiteid;

/**
 * This describes the format that is used in the MIDlet or LIBlet suite
 * identifier.
 *
 * @since 2017/02/22
 */
public enum MidletSuiteIDFormat
{
	/** In the format for intermidlet communication. */
	IMC,
	
	/** The format used for dependencies. */
	DEPENDENCY,
	
	/** The format used in JARs. */
	JAR,
	
	/** End. */
	;
}

