// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.sm;

/**
 * This represents the type of structure that the current table references.
 *
 * @since 2016/06/09
 */
public enum StructureType
{
	/** Free space. */
	FREE,
	
	/** An object. */
	OBJECT,
	
	/** An object but one that is constant and cannot be GCed. */
	CONSTANT_OBJECT,
	
	/** End. */
	;
}

