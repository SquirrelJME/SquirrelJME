// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.io;

/**
 * This is attached to a resource which may be closed when it is no longer
 * required.
 *
 * Note that despite being similar to {@link AutoCloseable} there is a subtle
 * difference in that this class must never throw an {@link IOException} if the
 * resource is already closed and must have no effect.
 *
 * @since 2016/04/12
 */
public interface Closeable
	extends AutoCloseable
{
	/**
	 * Attempts to close the resource which may free other resources.
	 *
	 * Unlike {@link AutoCloseable}, if the resource is already closed then
	 * this method should have no effect and not throw a {@link IOException}.
	 *
	 * If it is possible, it is recommended to mark a resource as closed after
	 * freeing the resources before throwing an {@link IOException}. However,
	 * this case should only be considered if this is a top level resource
	 * which relies on no other {@link Closeable} resources.
	 *
	 * @throws IOException If there was an error closing this resource.
	 * @since 2016/04/12
	 */
	@Override
	public abstract void close()
		throws IOException;
}

