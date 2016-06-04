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
 * This interface is used to provide means for object queues to be filled by
 * object sources.
 *
 * @since 2016/06/04
 */
public interface GCObjectSources
{
	/**
	 * This provides a queue to the given object queue which provides access to
	 * all objects which have been allocated.
	 *
	 * @param __q The target queue to be filled.
	 * @throws GCException If the queue could not be filled.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/04
	 */
	public abstract void allObjects(GCObjectQueue __q)
		throws GCException, NullPointerException;
}

