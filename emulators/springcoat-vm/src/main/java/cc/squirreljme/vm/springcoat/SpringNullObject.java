// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.vm.springcoat.brackets.RefLinkHolder;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;

/**
 * Represents the null object.
 *
 * @since 2018/09/08
 */
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
		throw new SpringVirtualMachineException("BK1e");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/05/31
	 */
	@Override
	public RefLinkHolder refLink()
	{
		throw new SpringVirtualMachineException("NULL has no refLink.");
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

