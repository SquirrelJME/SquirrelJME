// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.vm.springcoat.exceptions.SpringNullPointerException;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;

/**
 * Represents the null object.
 *
 * @since 2018/09/08
 */
@Deprecated
public final class SpringNullObject
	implements SpringObject
{
	/** Single null object reference. */
	public static final SpringNullObject NULL =
		new SpringNullObject();
	
	/**
	 * Only used once.
	 *
	 * @since 2018/09/08
	 */
	private SpringNullObject()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final SpringMonitor monitor()
	{
		// {@squirreljme.error BK1e Cannot obtain the monitor of an object
		// that is null.}
		throw new SpringNullPointerException("BK1e");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/21
	 */
	@Override
	public final SpringPointerArea pointerArea()
	{
		return SpringPointerArea.NULL;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/13
	 */
	@Override
	public ReferenceChainer refChainer()
	{
		throw new SpringVirtualMachineException("Null has no ref chain.");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/13
	 */
	@Override
	public ReferenceCounter refCounter()
	{
		throw new SpringVirtualMachineException("Null has no ref counter.");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/09
	 */
	@Override
	public final SpringClass type()
	{
		return null;
	}
}

