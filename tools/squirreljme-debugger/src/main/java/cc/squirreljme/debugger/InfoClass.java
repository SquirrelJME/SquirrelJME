// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.jdwp.JDWPCommandSetReferenceType;
import cc.squirreljme.jdwp.JDWPCommandSet;
import cc.squirreljme.jdwp.JDWPIdKind;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPId;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodName;

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
		this.methods = new KnownValue<InfoMethod[]>(InfoMethod[].class,
			this::__updateMethods);
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
		
		// The name of this class
		ClassName thisName = this.thisName();
		
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
					MethodFlags flags =
						new MethodFlags(__reply.readInt());
					
					// Setup method
					result[i] = stored.get(__state, methodId,
						thisName, name, type, flags);
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
