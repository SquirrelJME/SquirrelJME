// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Error types for JDWP.
 *
 * @since 2021/03/12
 */
public interface JDWPErrorType
{
	/** No error. */
	byte NO_ERROR = 
		0;
	
	/** Not implemented. */
	byte NOT_IMPLEMENTED =
		99;
}
