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
import cc.squirreljme.jdwp.JDWPCommandSetReferenceType;
import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPIdKind;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPId;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.Pool;
import org.jetbrains.annotations.NotNull;

/**
 * Caches information on remote classes and otherwise.
 *
 * @since 2024/01/22
 */
public class InfoClass
	extends Info
{
	/** The name of this class. */
	protected final KnownValue<ClassName> thisName;
	
	/** The constant pool of this class. */
	protected final KnownValue<Pool> constantPool;
	
	/** The methods of this class. */
	protected final KnownValue<InfoMethod[]> methods;
	
	/**
	 * Initializes the base information.
	 *
	 * @param __state The debugger state.
	 * @param __id The ID number of this info.
	 * @since 2024/01/22
	 */
	public InfoClass(DebuggerState __state, JDWPId __id)
		throws NullPointerException
	{
		super(__state, __id, InfoKind.CLASS);
		
		this.thisName = new KnownValue<ClassName>(ClassName.class,
			this::__updateThisName);
		this.constantPool = new KnownValue<Pool>(Pool.class,
			this::__updateConstantPool);
		this.methods = new KnownValue<InfoMethod[]>(InfoMethod[].class,
			this::__updateMethods);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/24
	 */
	@Override
	public int compareTo(@NotNull Info __o)
	{
		// Wrong class?
		if (!(__o instanceof InfoClass))
			return super.compareTo(__o);
		
		String a = Objects.toString(
			this.thisName.getOrDefault(null), "a");
		String b = Objects.toString(((InfoClass)__o).thisName
			.getOrDefault(null), "b");
		
		int rv = a.compareTo(b);
		if (rv != 0)
			return rv;
		
		return super.compareTo(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/24
	 */
	@Override
	protected String internalString()
	{
		return this.thisName.getOrUpdate(this.internalState()).toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/22
	 */
	@Override
	protected boolean internalUpdate(DebuggerState __state)
		throws NullPointerException
	{
		DebuggerState state = this.internalState();
		
		// These can be cached once
		this.thisName.getOrUpdate(state);
		this.constantPool.getOrUpdate(state);
		this.methods.getOrUpdate(state);
		
		return true;
	}
	
	/**
	 * Returns the methods in this class.
	 *
	 * @return The methods in this class.
	 * @since 2024/01/22
	 */
	public InfoMethod[] methods()
	{
		InfoMethod[] methods = this.methods.getOrUpdate(this.internalState());
		if (methods != null)
			return methods.clone();
		
		return new InfoMethod[0];
	}
	
	/**
	 * Returns the name of this class.
	 *
	 * @return The name of this class.
	 * @since 2024/01/22
	 */
	public ClassName thisName()
	{
		return this.thisName.getOrUpdate(this.internalState());
	}
	
	/**
	 * Updates the constant pool of the class.
	 *
	 * @param __state The state this is in.
	 * @param __value The value to be updated.
	 * @since 2024/01/23
	 */
	private void __updateConstantPool(DebuggerState __state,
		KnownValue<Pool> __value)
	{
		// If the VM does not support this, then we cannot do anything about it
		if (!__state.capabilities.has(JDWPCapability.CAN_GET_CONSTANT_POOL))
		{
			__value.set(null);
			return;
		}
			
		// Byte code will be much more intelligent if the constant pool
		// could be obtained
		try (JDWPPacket out = __state.request(
			JDWPCommandSet.REFERENCE_TYPE,
			JDWPCommandSetReferenceType.CONSTANT_POOL))
		{
			// Write the class ID
			out.writeId(this.id);
			
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
						__value.set(Pool.decode(din, count));
					}
					catch (IOException __e)
					{
						__e.printStackTrace();
					}
				}, (__ignored, __reply) -> {
				});
		}
	}
	
	/**
	 * Performs an update of the class methods.
	 *
	 * @param __state The state to update from.
	 * @param __value The value that is being updated.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	private void __updateMethods(DebuggerState __state,
		KnownValue<InfoMethod[]> __value)
		throws NullPointerException
	{
		if (__state == null || __value == null)
			throw new NullPointerException("NARG");
		
		// Request methods in the class
		try (JDWPPacket out = __state.request(JDWPCommandSet.REFERENCE_TYPE,
			JDWPCommandSetReferenceType.METHODS))
		{
			// Write the ID
			out.writeId(this.id.intValue());
			
			// Wait for response
			__state.sendThenWait(out, Utils.TIMEOUT, (__ignored, __reply) -> {
				int count = __reply.readInt();
				
				// Get method storage
				StoredInfo<InfoMethod> stored =
					__state.storedInfo.getMethods();
			
				// Fill in method results
				InfoMethod[] result = new InfoMethod[count];
				for (int i = 0; i < count; i++)
				{
					// Read method information
					JDWPId methodId = __reply.readId(JDWPIdKind.METHOD_ID);
					MethodName name =
						new MethodName(__reply.readString());
					MethodDescriptor type =
						new MethodDescriptor(__reply.readString());
					
					// Read flags, 0xF means it is synthetic, but we should
					// ignore this here
					MethodFlags flags =
						new MethodFlags(__reply.readInt() & (~0xF0000000));
					
					// Setup method
					result[i] = stored.get(__state, methodId,
						this, name, type, flags);
				}
				
				// Set value
				__value.set(result);
			}, (__ignored, __fail) -> {
			});
		}
	}
	
	/**
	 * Updates the name of this class. 
	 *
	 * @param __state The debugger state.
	 * @param __known The known value being updated.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/22
	 */
	private void __updateThisName(DebuggerState __state,
		KnownValue<ClassName> __known)
		throws NullPointerException
	{
		if (__state == null || __known == null)
			throw new NullPointerException("NARG");
		
		try (JDWPPacket out = __state.request(JDWPCommandSet.REFERENCE_TYPE,
			JDWPCommandSetReferenceType.SIGNATURE))
		{
			// Write the ID
			out.writeId(this.id.intValue());
			
			// Wait for response
			__state.sendThenWait(out, Utils.TIMEOUT, (__ignored, __reply) -> {
				String value = __reply.readString();
				
				__known.set(new FieldDescriptor(value).className());
			}, (__ignored, __fail) -> {
			});
		}
	}
}
