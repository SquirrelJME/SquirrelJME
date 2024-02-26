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
import cc.squirreljme.jdwp.JDWPCommandSetClassType;
import cc.squirreljme.jdwp.JDWPCommandSetMethod;
import cc.squirreljme.jdwp.JDWPCommandSetReferenceType;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodFlag;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.Pool;

/**
 * Not Described.
 *
 * @since 2024/01/22
 */
public class InfoMethod
	extends Info
{
	/** The class this is in. */
	protected final InfoClass inClass;
	
	/** The method flags. */
	protected final KnownValue<MethodFlags> flags;
	
	/** The method name. */
	protected final KnownValue<MethodName> name;
	
	/** The method type. */
	protected final KnownValue<MethodDescriptor> type;
	
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
		
		// The class is required to be known
		if (__inClass == null)
			throw new NullPointerException("NARG");
		
		this.inClass = __inClass;
		
		// Generic updaters
		this.name = new KnownValue<MethodName>(MethodName.class,
			__name, this::__updateGeneric);
		this.type = new KnownValue<MethodDescriptor>(MethodDescriptor.class,
			__type, this::__updateGeneric);
		this.flags = new KnownValue<MethodFlags>(MethodFlags.class,
			__flags, this::__updateGeneric);
		
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
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/24
	 */
	@Override
	protected String internalString()
	{
		DebuggerState state = this.internalState();
		
		MethodName name = this.name.getOrUpdateSync(state);
		MethodDescriptor type = this.type.getOrUpdateSync(state);
		
		return String.format("%s:%s", name, type);
	}
	
	/**
	 * Retrieves the byte code of the method.
	 *
	 * @param __state The state.
	 * @param __value The value being updated.
	 * @param __sync The callback when the value is known.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/23
	 */
	private void __updateByteCode(DebuggerState __state,
		KnownValue<InfoByteCode> __value,
		KnownValueCallback<InfoByteCode> __sync)
		throws NullPointerException
	{
		if (__state == null || __value == null)
			throw new NullPointerException("NARG");
		
		// Only need to calculate this once
		if (__value.isKnown())
		{
			if (__sync != null)
				__sync.sync(__state, __value);
			return;
		}
		
		// If the VM does not have the capability to get byte code, then
		// we cannot actually do this
		// If the class was garbage collected we cannot do much here
		// or if the method is abstract or native
		InfoClass inClass = this.inClass;
		MethodFlags flags = this.flags.getOrUpdateSync(__state);
		if (!__state.capabilities.has(JDWPCapability.CAN_GET_BYTECODES) ||
			(flags != null && (flags.isAbstract() || flags.isNative())))
		{
			__value.set(null);
			return;
		}
		
		// Byte code will be much more intelligent if the constant pool
		// could be obtained
		KnownValue<Pool> constantPool = inClass.constantPool;
		
		// Request byte code from the method
		try (JDWPPacket out = __state.request(
			JDWPCommandSet.METHODS,
			JDWPCommandSetMethod.BYTE_CODES))
		{
			// Write the class and method IDs
			out.writeId(inClass.id);
			out.writeId(this.id);
			
			// Send it
			__state.sendKnown(out, __value, __sync,
				(__ignored, __reply) -> {
					// Get byte code storage
					StoredInfo<InfoByteCode> stored =
						__state.storedInfo.getByteCodes();
					
					// Read length and the byte code data
					int length = __reply.readInt();
					byte[] data = new byte[length];
					__reply.readFully(data, 0, length);
					
					// Read in the byte code data
					__value.set(stored.get(__state, this.id,
						this, constantPool, data));
				}, ReplyHandler.IGNORED);
		}
	}
	
	/**
	 * Updates the generic method information.
	 *
	 * @param __state The state used.
	 * @param __value Ignored.
	 * @param __sync The callback to execute when done.
	 * @since 2024/01/27
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void __updateGeneric(DebuggerState __state,
		KnownValue<?> __value, KnownValueCallback<?> __sync)
	{
		KnownValue<MethodName> name = this.name;
		KnownValue<MethodDescriptor> type = this.type;
		KnownValue<MethodFlags> flags = this.flags;
		
		// If we already have this information then we do not need to get it
		// again as it is constant
		if (name.isKnown() && type.isKnown() && flags.isKnown())
			return;
		
		// We get this information by requesting the methods in a class, which
		// will update this method's values
		this.inClass.methods.update(__state, (__ignored1, __ignored2) -> {
		});
	}
}
