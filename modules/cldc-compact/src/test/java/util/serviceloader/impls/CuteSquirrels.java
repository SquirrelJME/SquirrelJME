// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util.serviceloader.impls;

import util.serviceloader.ServiceThingy;

/**
 * Test service for the service loader.
 *
 * @since 2018/12/06
 */
public class CuteSquirrels
	implements ServiceThingy
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public String toString()
	{
		return "Cute squirrels!";
	}
}

