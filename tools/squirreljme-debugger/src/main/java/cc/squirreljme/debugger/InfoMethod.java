// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPCapability;
import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPCommandSetMethod;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPPacket;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.classfile.Pool;

/**
 * Not Described.
 *
 * @since 2024/01/22
 */
public class InfoMethod
	extends Info
{
	/** The method flags. */
	protected final MethodFlags flags;
	
	/** The method name. */
	protected final MethodName name;
	
	/** The method type. */
	protected final MethodDescriptor type;
	
	/** The class this is in. */
	protected final Reference<InfoClass> inClass;
	
	/** The byte code of this method. */
	protected final KnownValue<InfoByteCode> byteCode;
	
	/**
	 * Initializes the method information.
	 *
	 * @param __state The debugger state.
	 * @param __id The ID number of this info.
	 * @param __inClass The class this is in.
	 * @param __name The method name.
	 * @param __type The method type.
	 * @param __flags The method flags.
	 * @since 2024/01/22
	 */
	public InfoMethod(DebuggerState __state, JDWPId __id, InfoClass __inClass,
		MethodName __name, MethodDescriptor __type, MethodFlags __flags)
		throws NullPointerException
	{
		super(__state, __id, InfoKind.METHOD);
		
		this.inClass = new WeakReference<>(__inClass);
		this.name = __name;
		this.type = __type;
		this.flags = __flags;
		
		this.byteCode = new KnownValue<InfoByteCode>(InfoByteCode.class,
			this::__updateByteCode);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	protected boolean internalUpdate(DebuggerState __state)
		throws NullPointerException
	{
		// Single run updates
		this.byteCode.getOrUpdate(__state);
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/24
	 */
	@Override
	protected String internalString()
	{
		return new MethodNameAndType(this.name, this.type).toString();
	}
	
	/**
	 * Retrieves the byte code of the method.
	 *
	 * @param __state The state.
	 * @param __value The value being updated.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/23
	 */
	private void __updateByteCode(DebuggerState __state,
		KnownValue<InfoByteCode> __value)
		throws NullPointerException
	{
		if (__state == null || __value == null)
			throw new NullPointerException("NARG");
		
		// If the VM does not have the capability to get byte code, then
		// we cannot actually do this
		// If the class was garbage collected we cannot do much here
		// or if the method is abstract or native
		InfoClass inClass = this.inClass.get();
		if (!__state.capabilities.has(JDWPCapability.CAN_GET_BYTECODES) ||
			inClass == null ||
			this.flags.isAbstract() || this.flags.isNative())
		{
			__value.set(null);
			return;
		}
		
		// Byte code will be much more intelligent if the constant pool
		// could be obtained
		Pool constantPool = inClass.constantPool.getOrUpdate(__state);
		
		// Request byte code from the method
		try (JDWPPacket out = __state.request(
			JDWPCommandSet.METHODS,
			JDWPCommandSetMethod.BYTE_CODES))
		{
			// Write the class and method IDs
			out.writeId(inClass.id);
			out.writeId(this.id);
			
			// Send it
			__state.sendThenWait(out, Utils.TIMEOUT,
				(__ignored, __reply) -> {
					// Get byte code storage
					StoredInfo<InfoByteCode> stored =
						__state.storedInfo.getByteCodes();
					
					// Read length and the byte code data
					int length = __reply.readInt();
					byte[] data = __reply.readFully(length);
					
					// Read in the byte code data
					__value.set(stored.get(__state, this.id,
						this, constantPool, data));
				}, (__ignored, __reply) -> {
				});
		}
	}
}
