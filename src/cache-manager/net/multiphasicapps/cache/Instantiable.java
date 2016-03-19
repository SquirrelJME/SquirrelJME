// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.cache;

/**
 * This interface is used to describe a class or cacheable resource which may
 * be instantiated when it is needed, then thrown out when it is not needed.
 *
 * All implementations of this interface MUST HAVE a no argument constructor.
 * This also means that if used as an inner class then it MUST be a static
 * inner class.
 *
 * @param <O> The owner class of this object (which one owns the cache for
 * initialization.
 * @param <Z> The type of value to initialize for.
 * @since 2016/03/19
 */
public interface Instantiable<O, Z>
{
	/**
	 * Initializes the given object.
	 *
	 * @param __o The owner of the object, may be {@code null} if it is static
	 * for example and no owner makes sense.
	 * @return The value it should create.
	 * @since 2016/03/19
	 */
	public abstract Z initialize(O __o);
}

