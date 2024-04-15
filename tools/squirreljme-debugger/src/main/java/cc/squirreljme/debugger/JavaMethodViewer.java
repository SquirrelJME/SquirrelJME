// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Java method viewer using SquirrelJME's {@link Method}.
 *
 * @since 2024/01/21
 */
public class JavaMethodViewer
	implements MethodViewer
{
	/** The method to view. */
	protected final Method method;
	
	/** Cached instructions. */
	private volatile InstructionViewer[] _instructions;
	
	/**
	 * Initializes the method viewer.
	 *
	 * @param __method The method to view.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/21
	 */
	public JavaMethodViewer(Method __method)
		throws NullPointerException
	{
		if (__method == null)
			throw new NullPointerException("NARG");
		
		this.method = __method;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/21
	 */
	@Override
	public ClassName inClass()
	{
		return this.method.inClass();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/21
	 */
	@Override
	public boolean isAbstract()
	{
		return this.method.flags().isAbstract();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/21
	 */
	@Override
	public boolean isNative()
	{
		return this.method.flags().isNative();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/21
	 */
	@Override
	public MethodNameAndType methodNameAndType()
	{
		return this.method.nameAndType();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/21
	 */
	@Override
	public InstructionViewer[] instructions()
	{
		// Cannot be native or abstract
		if (this.isAbstract() || this.isNative())
			return null;
		
		// Did we already wrap everything?
		InstructionViewer[] result = this._instructions;
		if (result != null)
			return result.clone();
		
		// Get the byte code for the method
		ByteCode byteCode = this.method.byteCode();
		
		// Get the instructions and wrap them all
		int count = byteCode.instructionCount();
		result = new InstructionViewer[count];
		for (int i = 0; i < count; i++)
			result[i] = new JavaInstructionViewer(byteCode.getByIndex(i));
		
		// Cache for later and return the result
		this._instructions = result;
		return result.clone();
	}
}
