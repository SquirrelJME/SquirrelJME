// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import dev.shadowtail.classfile.xlate.ByteCodeHandler;
import dev.shadowtail.classfile.xlate.ByteCodeState;
import dev.shadowtail.classfile.xlate.JavaStackResult;
import dev.shadowtail.classfile.xlate.MathType;
import dev.shadowtail.classfile.xlate.StackJavaType;

/**
 * This contains the handler for the near native byte code.
 *
 * @since 2019/04/06
 */
public final class NearNativeByteCodeHandler
	implements ByteCodeHandler
{
	/** State of the byte code. */
	public final ByteCodeState state =
		new ByteCodeState();
	
	/** Used to build native code. */
	protected final NativeCodeBuilder codebuilder =
		new NativeCodeBuilder();
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void doCopy(JavaStackResult.Input __in,
		JavaStackResult.Output __out)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void doMath(StackJavaType __dt, MathType __mt,
		JavaStackResult.Input __a, JavaStackResult.Input __b,
		JavaStackResult.Output __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void doMath(StackJavaType __dt, MathType __mt,
		JavaStackResult.Input __a, Number __b, JavaStackResult.Output __c)
	{
		this.codebuilder.addMathConst(__dt, __mt, __a.register, __b,
			__c.register);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void instructionFinish()
	{
	}
	
	/**
	 * Sets up before processing the instruction.
	 *
	 * @since 2019/04/07
	 */
	public final void instructionSetup()
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		ByteCodeState state = this.state;
		
		// Setup a label for this current position
		codebuilder.label("java", state.addr);
	}
	
	/**
	 * Returns the result of the translation.
	 *
	 * @return The translation result.
	 * @since 2019/04/07
	 */
	public final NativeCode result()
	{
		return this.codebuilder.build();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/06
	 */
	@Override
	public final ByteCodeState state()
	{
		return this.state;
	}
}

