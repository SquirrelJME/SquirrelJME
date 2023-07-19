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
		/* {@squirreljme.error BK1e Cannot obtain the monitor of an object
		that is null.} */
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
	
	/**
	 * Checks the given type and return the value if it is not {@code null}.
	 * 
	 * @param <T> The type to check.
	 * @param __type The type to check.
	 * @param __arg The argument to check against.
	 * @return Either {@code null} or {@code __arg} if not {@code null}.
	 * @throws ClassCastException If the type is wrong.
	 * @throws NullPointerException If {@code __type} is {@code null}.
	 * @since 2021/12/05
	 */
	public static <T> T checkCast(Class<T> __type, Object __arg)
		throws ClassCastException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		if (__arg == null || __arg == SpringNullObject.NULL)
			return null;
		return __type.cast(__arg);
	}
}

