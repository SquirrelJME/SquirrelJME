// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This class is used to obtain the client instance of a service.
 *
 * @param <C> The client service to get.
 * @since 2018/01/03
 */
public interface SystemServiceSupplier<C>
{
	/**
	 * Obtains the single instance of the service.
	 *
	 * @return The service instance.
	 * @since 2018/01/03
	 */
	public abstract C get();
}

