// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This is an indicator that an exception was thrown.
 *
 * @since 2018/10/06
 */
@SquirrelJMEVendorApi
final class __ExceptionThrown__
{
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	public final boolean equals(Object __o)
	{
		return __o == this || (__o instanceof __ExceptionThrown__);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	public final int hashCode()
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	public final String toString()
	{
		return "ExceptionThrown";
	}
}

