// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.util;

/**
 * First leveled factory.
 *
 * @since 2021/08/08
 */
public class FakeFirstDriverFactory
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
		return "fake.first";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/08
	 */
	@Override
	public int priority()
	{
		return MAX_PRIORITY;
	}
}
