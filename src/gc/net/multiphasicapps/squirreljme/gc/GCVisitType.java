// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.gc;

/**
 * This represents how a given object has been visited.
 *
 * @since 2016/06/04
 */
public enum GCVisitType
{
	/** Not visited at all. */
	NONE,
	
	/**
	 * Weakly visited via {@link java.lang.ref.WeakReference}.
	 *
	 * If an object is weakly visited then there should not be any field
	 * traversal.
	 */
	WEAK,
	
	/** Strongly visited via a strong reference (variable, field, or array). */
	STRONG,
	
	/** End. */
	;
}

