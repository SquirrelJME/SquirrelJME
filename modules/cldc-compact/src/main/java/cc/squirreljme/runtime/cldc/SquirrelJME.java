// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

/**
 * Contains information on SquirrelJME.
 *
 * @since 2018/12/05
 */
public interface SquirrelJME
{
	/** The version of this SquirrelJME runtime. */
	String RUNTIME_VERSION =
		"0.3.0";
	
	/** The microedition platform. */
	String MICROEDITION_PLATFORM =
		"SquirrelJME/0.3.0";
	
	/** The major version. */
	byte MAJOR_VERSION =
		0;
	
	/** The minor version. */
	byte MINOR_VERSION =
		3;
	
	/** The release version. */
	byte RELEASE_VERSION =
		0;
}

