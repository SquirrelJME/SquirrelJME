// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.util;

import cc.squirreljme.runtime.cldc.lang.ComplexDriverFactory;

/**
 * Last leveled factory.
 *
 * @since 2021/08/08
 */
public class FakeLastDriverFactory
	implements FakeDriverInterface
{
	/**
	 * {@inheritDoc}
	 * @since 2021/08/08
	 */
	@Override
	public <I> I instance(Class<I> __class)
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/08
	 */
	@Override
	public String name()
	{
		return "fake.last";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/08
	 */
	@Override
	public int priority()
	{
		return ComplexDriverFactory.MIN_PRIORITY;
	}
}
