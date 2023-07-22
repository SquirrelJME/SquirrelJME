// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

import cc.squirreljme.runtime.cldc.annotation.Api;

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
@Api
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
	void close()
		throws IOException;
}

