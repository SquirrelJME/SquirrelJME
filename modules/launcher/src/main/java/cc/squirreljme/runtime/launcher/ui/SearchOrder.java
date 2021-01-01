// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

/**
 * The preferred sort order for applications.
 *
 * @since 2020/12/29
 */
public enum SearchOrder
{
	/** The SquirrelJME Name of the application. */
	SQUIRRELJME_NAME,
	
	/** The display name of the application. */
	DISPLAY_NAME,
	
	/** The suite name of the application. */
	SUITE_NAME,
	
	/** The main class. */
	MAIN_CLASS,
	
	/* End. */
	;
}
