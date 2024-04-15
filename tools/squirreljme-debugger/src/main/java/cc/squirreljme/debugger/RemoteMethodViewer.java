// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Views remote methods.
 *
 * @since 2024/01/22
 */
public class RemoteMethodViewer
	implements MethodViewer
{
	/** The remote method information. */
	protected final InfoMethod info;
	
	/** The debugger state. */
	protected final DebuggerState state;
	
	/**
	 * Initializes the remote method viewer.
	 *
	 * @param __state The state this is in.
	 * @param __info The method information.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	public RemoteMethodViewer(DebuggerState __state, InfoMethod __info)
		throws NullPointerException
	{
		if (__info == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
		this.info = __info;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public ClassName inClass()
	{
		return this.info.inClass.thisName();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public InstructionViewer[] instructions()
	{
		// Return blank if abstract or native
		if (this.isAbstract() || this.isNative())
			return new InstructionViewer[0];
		
		// Fallback to nothing if there is no byte code
		InfoByteCode byteCode = this.info.byteCode.getOrUpdateSync(this.state);
		if (byteCode == null)
			return new InstructionViewer[0];
		
		return byteCode.instructions();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public boolean isAbstract()
	{
		MethodFlags flags = this.info.flags.getOrUpdateSync(
			this.info.internalState());
		if (flags != null)
			return flags.isAbstract();
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public boolean isNative()
	{
		MethodFlags flags = this.info.flags.getOrUpdateSync(
			this.info.internalState());
		if (flags != null)
			return flags.isNative();
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	public MethodNameAndType methodNameAndType()
	{
		InfoMethod info = this.info;
		DebuggerState state = info.internalState();
		
		MethodName name = info.name.getOrUpdateSync(state);
		MethodDescriptor type = info.type.getOrUpdateSync(state);
		
		return new MethodNameAndType(name, type);
	}
}
