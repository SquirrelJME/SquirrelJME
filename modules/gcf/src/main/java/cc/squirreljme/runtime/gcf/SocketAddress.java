// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This interface represents a generic address type.
 *
 * @since 2019/05/06
 */
@SquirrelJMEVendorApi
public interface SocketAddress
{
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	@SquirrelJMEVendorApi
	boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	@SquirrelJMEVendorApi
	int hashCode();
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	@SquirrelJMEVendorApi
	String toString();
}

