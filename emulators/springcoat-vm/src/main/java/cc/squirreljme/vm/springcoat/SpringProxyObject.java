// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.RefLinkHolder;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This class allows for interfaces/objects within SpringCoat to be called
 * to an external object or interface that is within the host VM.
 *
 * @since 2021/02/25
 */
public abstract class SpringProxyObject
	implements SpringObject, SpringProxyObjectType
{
	/** The machine used for the proxy. */
	private final SpringMachine machine;
	
	/** The type this is for. */
	private final ClassName type;
	
	/**
	 * Initializes the proxy object.
	 *
	 * @param __type The class type that this is.
	 * @param __machine The machine used for the proxy.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/02/25
	 */
	public SpringProxyObject(Class<?> __type, SpringMachine __machine)
		throws NullPointerException
	{
		this(new ClassName(__type.getName().replace('.', '/')),
			__machine);
	}
	
	/**
	 * Initializes the proxy object.
	 *
	 * @param __type The class type that this is.
	 * @param __machine The machine used for the proxy.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/02/25
	 */
	public SpringProxyObject(ClassName __type, SpringMachine __machine)
		throws NullPointerException
	{
		if (__type == null || __machine == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this.machine = __machine;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/25
	 */
	@Override
	public final SpringMonitor monitor()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/25
	 */
	@Override
	public final RefLinkHolder refLink()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/25
	 */
	@Override
	public final SpringClass type()
	{
		return this.machine.classLoader().loadClass(this.type);
	}
}
