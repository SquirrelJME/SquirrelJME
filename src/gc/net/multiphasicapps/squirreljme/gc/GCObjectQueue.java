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
 * This is a queue which permits access to objects
 *
 * @since 2016/06/04
 */
public interface GCObjectQueue
{
	/**
	 * Frees the object that is currently selected in this queue so that its
	 * used memory is available for other threads to use.
	 *
	 * @throws GCException If there is no object currently under this queue.
	 * @since 2016/06/04
	 */
	public abstract void free()
		throws GCException;
	
	/**
	 * Moves the queue to the next object.
	 *
	 * @return {@code true} if a new object was visited, otherwise
	 * {@code false} if there are no more objects to visit.
	 * @throws GCException If the next object could not be determined due to
	 * an unspecified failure.
	 * @since 2016/06/04
	 */
	public abstract boolean next()
		throws GCException;
	
	/**
	 * Marks the current object under this queue as visited by increasing
	 * the visit type.
	 *
	 * @param __vt The type of visit where this object was referenced
	 * @throws GCException If there is no object currently under this queue.
	 * @throws NullPointerException If no type was specified.
	 * @since 2016/06/04
	 */
	public abstract void markVisited(GCVisitType __vt)
		throws GCException, NullPointerException;
	
	/**
	 * Visits all objects which are referenced by this object.
	 *
	 * @return An queue over the references which this object visits.
	 * @throws GCException If there is no object currently under this queue.
	 * @since 2016/06/04
	 */
	public abstract GCObjectQueue visitObjects()
		throws GCException;
	
	/**
	 * Returns the visit type of this object, any objects which are not
	 * visited or weakly visited may be garbage collected.
	 *
	 * @return The type of visit that was previously performed.
	 * @throws GCException If there is no object currently under this queue.
	 * @since 2016/06/04
	 */
	public abstract GCVisitType visitType()
		throws GCException;
}

