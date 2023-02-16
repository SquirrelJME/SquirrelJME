// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import com.nttdocomo.io.ConnectionException;

@Api
public interface MediaResource
{
	/**
	 * Disposes of the resource so that it no longer can be used.
	 *
	 * @throws UIException If the resource has already been disposed.
	 * @since 2022/02/14
	 */
	@Api
	void dispose()
		throws UIException;
	
	/**
	 * Specifies that the media should stop being used and frees up
	 * resources for it. Note that the actual resource in its original
	 * location
	 * is retained for the next {@link #use()} of which this becomes
	 * available.
	 *
	 * @throws UIException If the resource could not be freed.
	 * @since 2022/02/14
	 */
	@Api
	void unuse()
		throws UIException;
	
	/**
	 * Specifies that the resource is to be used now, it will be loaded from
	 * the backing resource and will hold a representation of the data. This
	 * in a sense opens the data.
	 *
	 * @throws ConnectionException If the connection could not be made.
	 * @throws SecurityException If the operation is not supported due to
	 * a security mechanism.
	 * @throws UIException If the resource could not be opened.
	 * @since 2022/02/14
	 */
	@Api
	void use()
		throws ConnectionException, SecurityException, UIException;
}
