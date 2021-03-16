// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Reference type command set.
 *
 * @since 2021/03/13
 */
public enum ReferenceTypeCommandSet
	implements JDWPCommand
{
	/** Non-generic signature of a given type. */
	SIGNATURE(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which class does this refer to?
			JDWPClass type = __controller.state.getAnyClass(
				__packet.readId());
			if (type == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_CLASS);
			
			// Write the normal class signature
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeString(type.debuggerFieldDescriptor());
			
			return rv;
		}
	},
	
	/** Static field values. */
	STATIC_FIELD_VALUE(6)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/15
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which class does this refer to?
			JDWPReferenceType type = __controller.state.getReferenceType(
				__packet.readId());
			if (type == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_CLASS);
				
			// Read in all fields
			int numFields = __packet.readInt();
			JDWPField[] fields = new JDWPField[numFields];
			for (int i = 0; i < numFields; i++)
			{
				JDWPField field = __controller.state.fields.get(
					__packet.readId());
				if (field == null)
					return __controller.__reply(
						__packet.id(), ErrorType.INVALID_FIELD_ID);
					
				fields[i] = field;
			}
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// We need the class to communicate with
			JDWPClass classy = type.debuggerClass();
			
			// Write field mappings
			rv.writeInt(numFields);
			for (int i = 0; i < numFields; i++)
				rv.writeValue(classy.debuggerFieldValue(null, fields[i]));
			
			return rv;
		}
	},
	
	/** Source file. */
	SOURCE_FILE(7)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which class does this refer to?
			JDWPReferenceType type = __controller.state.getReferenceType(
				__packet.readId());
			if (type == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_CLASS);
			
			// Does this have a source file?
			String sourceFile = type.debuggerClass().debuggerSourceFile();
			if (sourceFile == null)
				return __controller.__reply(
				__packet.id(), ErrorType.ABSENT_INFORMATION);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			rv.writeString(sourceFile);
			
			return rv;
		}
	},
	
	/** Interfaces. */
	INTERFACES(10)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which class does this refer to?
			JDWPObjectLike type = __controller.state.getObjectLike(
				__packet.readId());
			if (type == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_CLASS);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Write all the interfaces
			JDWPClass[] interfaces = __controller.state
				.getObjectLikeClass(type).debuggerInterfaceClasses();
			rv.writeInt(interfaces.length);
			for (JDWPClass impl : interfaces)
				rv.writeId(impl);
			
			return rv;
		}
	},
	
	/** Signature with generic (class). */
	SIGNATURE_WITH_GENERIC(13)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which class does this refer to?
			JDWPClass type = __controller.state.getAnyClass(
				__packet.readId());
			if (type == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_CLASS);
				
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Only a normal signature is used, since generics are not needed
			rv.writeString(type.debuggerFieldDescriptor());
			rv.writeString("");
			
			return rv;
		}
	},
	
	/** Fields with generic types. */
	FIELDS_WITH_GENERIC(14)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which class does this refer to?
			JDWPObjectLike type = __controller.state
				.getObjectLike(__packet.readId());
			if (type == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_CLASS);
				
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Write number of fields
			JDWPField[] fields = __controller.state.getObjectLikeClass(type)
				.debuggerFields();
			rv.writeInt(fields.length);
			
			// Write information on each method
			for (JDWPField field : fields)
			{
				// Register this method for later lookup
				__controller.state.fields.put(field);
				
				// Information about the method
				rv.writeId(field);
				rv.writeString(field.debuggerMemberName());
				rv.writeString(field.debuggerMemberType());
				
				// Generics are not used in SquirrelJME, ignore
				rv.writeString("");
				
				// Modifier flags
				rv.writeInt(field.debuggerMemberFlags());
			}
			
			return rv;
		}
	},
	
	/** Methods with generic types. */
	METHODS_WITH_GENERIC(15)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which class does this refer to?
			JDWPObjectLike type = __controller.state.getObjectLike(
				__packet.readId());
			if (type == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_CLASS);
				
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Write number of methods
			JDWPMethod[] methods = __controller.state.getObjectLikeClass(type)
				.debuggerMethods();
			rv.writeInt(methods.length);
			
			// Write information on each method
			for (JDWPMethod method : methods)
			{
				// Register this method for later lookup
				__controller.state.methods.put(method);
				
				// Information about the method
				rv.writeId(method);
				rv.writeString(method.debuggerMemberName());
				rv.writeString(method.debuggerMemberType());
				
				// Generics are not used in SquirrelJME, ignore
				rv.writeString("");
				
				// Modifier flags
				rv.writeInt(method.debuggerMemberFlags());
			}
			
			return rv;
		}
	},
	
	/* End. */
	;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/13
	 */
	ReferenceTypeCommandSet(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/13
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
