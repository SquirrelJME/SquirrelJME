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
import cc.squirreljme.jdwp.JDWPCommandSetReferenceType;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.jdwp.JDWPPacket;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
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
	/** The method flags. */
	protected final MethodFlags flags;
	
	/** The method name. */
	protected final MethodName name;
	
	/** The method type. */
	protected final MethodDescriptor type;
	
	/** The class this is in. */
	protected final InfoClass inClass;
	
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
		
		this.inClass = __inClass;
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
		return true;
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
		if (!__state.capabilities.has(JDWPCapability.CAN_GET_BYTECODES))
		{
			__value.set(null);
			return;
		}
		
		// Byte code will be much more intelligent if the constant pool
		// could be obtained
		Pool[] constantPool = new Pool[]{null};
		if (__state.capabilities.has(JDWPCapability.CAN_GET_CONSTANT_POOL))
			try (JDWPPacket out = __state.request(
				JDWPCommandSet.REFERENCE_TYPE,
				JDWPCommandSetReferenceType.CONSTANT_POOL))
			{
				// Write the class ID
				out.writeId(this.inClass.id);
				
				// Send it
				__state.sendThenWait(out, Utils.TIMEOUT,
					(__ignored, __reply) -> {
						// Read entry count
						int count = __reply.readInt();
						
						// Read in the data
						int length = __reply.readInt();
						byte[] data = __reply.readFully(length);
						
						// Decode pool
						try (InputStream in = new ByteArrayInputStream(data);
							 DataInputStream din = new DataInputStream(in))
						{
							// Run the decoder
							Pool decoded = Pool.decode(din, count);
							
							// Store
							synchronized (constantPool)
							{
								constantPool[0] = decoded;
							}
						}
						catch (IOException __e)
						{
							__e.printStackTrace();
						}
					}, (__ignored, __reply) -> {
					});
			}
		
		// Request byte code from the method
		try (JDWPPacket out = __state.request(
			JDWPCommandSet.METHODS,
			JDWPCommandSetMethod.BYTE_CODES))
		{
			// Write the class and method IDs
			out.writeId(this.inClass.id);
			out.writeId(this.id);
			
			// Send it
			__state.sendThenWait(out, Utils.TIMEOUT,
				(__ignored, __reply) -> {
					__value.set(null);
				}, (__ignored, __reply) -> {
				});
		}
	}
}
