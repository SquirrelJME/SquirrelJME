// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;

/**
 * This is the base for ghost objects which do not have a defined class type
 * or anything associated with a class object.
 *
 * @since 2020/05/30
 */
public abstract class AbstractGhostObject
	implements SpringObject
{
	/**
	 * {@inheritDoc}
	 * @since 2020/05/30
	 */
	@Override
	public final SpringMonitor monitor()
	{
		throw new SpringVirtualMachineException(
			"Ghost objects cannot have monitors: " + this.toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/05/31
	 */
	@Override
	public RefLinkHolder refLink()
	{
		throw new SpringVirtualMachineException(
			"Ghost objects cannot have refLinks: " + this.toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/05/30
	 */
	@Override
	public final SpringPointerArea pointerArea()
	{
		throw new SpringVirtualMachineException(
			"Ghost objects cannot have pointers: " + this.toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/05/30
	 */
	@Override
	public final SpringClass type()
	{
		throw new SpringVirtualMachineException(
			"Ghost objects cannot have types: " + this.toString());
	}
}
