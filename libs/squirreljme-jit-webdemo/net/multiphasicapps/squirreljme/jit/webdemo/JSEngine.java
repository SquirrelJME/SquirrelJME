// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.webdemo;

import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.jit.ActiveCacheState;
import net.multiphasicapps.squirreljme.jit.CacheState;
import net.multiphasicapps.squirreljme.jit.DataType;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITStateAccessor;
import net.multiphasicapps.squirreljme.jit.Register;
import net.multiphasicapps.squirreljme.jit.TranslationEngine;
import net.multiphasicapps.squirreljme.linkage.MethodLinkage;

/**
 * This is the translation engine for the compile to JavaScript target.
 *
 * @since 2017/03/14
 */
public class JSEngine
	extends TranslationEngine
{
	/**
	 * Initializes the Javascript engine.
	 *
	 * @param __conf The target configuration.
	 * @param __sa The accessor to the state.
	 * @since 2017/03/14
	 */
	public JSEngine(JITConfig __conf, JITStateAccessor __sa)
	{
		super(__conf, __sa);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/14
	 */
	@Override
	public void bindStateForEntry(ActiveCacheState __cs)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/14
	 */
	@Override
	public void invokeMethod(CacheState __in, ActiveCacheState __out,
		MethodLinkage __ml, ActiveCacheState.Slot __rv,
		CacheState.Slot[] __args)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/16
	 */
	@Override
	public boolean isRegisterArgument(Register __r)
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/16
	 */
	@Override
	public boolean isRegisterSaved(Register __r)
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/16
	 */
	@Override
	public boolean isRegisterTemporary(Register __r)
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/14
	 */
	@Override
	public void slotCount(int __ms, int __ml)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/18
	 */
	@Override
	public void storeSlotToStack(CacheState.Slot __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/14
	 */
	@Override
	public DataType toDataType(StackMapType __t)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
}

