// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

/**
 * This is the type of phi that a given slot uses.
 *
 * @since 2016/04/11
 */
public enum CPPhiType
{
	/** Not a phi function at all. */
	NOT,
	
	/** Possible phi function, one that may be one but is not known. */
	POSSIBLE,
	
	/** This is a standard phi function. */
	PHI,
	
	/** End. */
	;
}

