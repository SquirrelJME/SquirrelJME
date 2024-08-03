// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.vm.springcoat.brackets.RefLinkHolder;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import net.multiphasicapps.classfile.ClassName;

/**
 * This is the base for ghost objects which do not have a defined class type
 * or anything associated with a class object.
 *
 * @since 2020/05/30
 */
public abstract class AbstractGhostObject
	implements SpringObject
{
	/** Which type does this represent? */
	protected final SpringClass represents;
	
	/**
	 * Initializes the base object with the type it represents.
	 * 
	 * @param __machine The machine used.
	 * @param __rep The type this represents.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/03
	 */
	public AbstractGhostObject(SpringMachine __machine, Class<?> __rep)
		throws NullPointerException
	{
		if (__machine == null || __rep == null)
			throw new NullPointerException("NARG");
		
		this.represents = __machine.classLoader().loadClass(
			new ClassName(__rep.getName().replace('.', '/')));
	}
	
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
	public final SpringClass type()
	{
		return this.represents;
	}
}
