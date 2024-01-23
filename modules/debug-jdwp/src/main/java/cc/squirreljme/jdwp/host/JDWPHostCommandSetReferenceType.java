// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPCommand;
import cc.squirreljme.jdwp.JDWPCommandSetReferenceType;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPValueTag;
import cc.squirreljme.jdwp.host.views.JDWPViewType;

/**
 * Reference type command set.
 *
 * @since 2021/03/13
 */
public enum JDWPHostCommandSetReferenceType
	implements JDWPCommandHandler
{
	/** Non-generic signature of a given type. */
	SIGNATURE(JDWPCommandSetReferenceType.SIGNATURE)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__signature(false, __controller, __packet);
		}
	},
	
	/** The class loader used on a class. */
	CLASS_LOADER(JDWPCommandSetReferenceType.CLASS_LOADER)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/20
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __controller.readType(__packet, false);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Make sure the class loader is loaded if used
			Object loader = __controller.viewType().classLoader(type);
			if (loader != null)
				__controller.getState().items.put(loader);
			
			// Write the class loader identifier
			rv.writeId(System.identityHashCode(loader));
			
			return rv;
		}
	},
	
	/** Modifiers for class. */
	MODIFIERS(JDWPCommandSetReferenceType.MODIFIERS)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/20
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __controller.readType(__packet, false);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			rv.writeInt(__controller.viewType().flags(type));
			
			return rv;
		}
	},
	
	/** Fields without generic. */
	FIELDS(JDWPCommandSetReferenceType.FIELDS)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/30
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__fields(false, __controller, __packet);
		}
	},
	
	/** Methods without generic. */
	METHODS(JDWPCommandSetReferenceType.METHODS)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/30
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__methods(false, __controller, __packet);
		}
	},
	
	/** Static field values. */
	STATIC_FIELD_VALUE(JDWPCommandSetReferenceType.STATIC_FIELD_VALUE)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/15
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which class does this refer to?
			JDWPViewType viewType = __controller.viewType();
			Object type = __controller.readType(__packet, false);
			
			// Read in all field indexes and check for their validity
			int numFields = __packet.readInt();
			int[] fields = new int[numFields];
			for (int i = 0; i < numFields; i++)
			{
				int fieldDx = __packet.readId();
				if (!viewType.isValidField(type, fieldDx))
					throw JDWPErrorType.INVALID_FIELD_ID.toss(type, fieldDx);
				
				fields[i] = fieldDx;
			}
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Write field mappings
			rv.writeInt(numFields);
			for (int i = 0; i < numFields; i++)
				try (JDWPHostValue value = __controller.value())
				{
					// Determine the field type and its tag
					String fieldSig = viewType.fieldSignature(type, fields[i]);
					JDWPValueTag tag = JDWPValueTag.fromSignature(fieldSig);
					
					// Read the field value, fallback if not valid
					if (!viewType.readValue(type, fields[i], value))
						value.set(tag.defaultValue);
					
					// Always write as tagged value
					__controller.writeValue(rv, value, tag, false);
					
					// Store object for later use
					if (value.get() != null && tag.isObject)
						__controller.getState().items.put(value.get());
				}
			
			return rv;
		}
	},
	
	/** Source file. */
	SOURCE_FILE(JDWPCommandSetReferenceType.SOURCE_FILE)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __controller.readType(__packet, false);
			
			// Does this have a source file?
			String sourceFile = __controller.viewType().sourceFile(type);
			if (sourceFile == null)
				throw JDWPErrorType.ABSENT_INFORMATION.toss(type,
					System.identityHashCode(type));
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			rv.writeString(sourceFile);
			
			return rv;
		}
	},
	
	/** Interfaces. */
	INTERFACES(JDWPCommandSetReferenceType.INTERFACES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __controller.readType(__packet, false);
			
			// Get every interface
			Object[] interfaces = __controller.viewType().interfaceTypes(type);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Write all the interfaces
			rv.writeInt(interfaces.length);
			for (Object impl : interfaces)
			{
				__controller.writeObject(rv, impl);
				
				// Record interface so it is known
				__controller.getState().items.put(impl);
			}
			
			return rv;
		}
	},
	
	/** Class object of type. */
	CLASS_OBJECT(JDWPCommandSetReferenceType.CLASS_OBJECT)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/20
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __controller.readType(__packet, false);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			Object instance = __controller.viewType().instance(type);
			__controller.getState().items.put(instance);
			
			__controller.writeObject(rv, instance);
			
			return rv;
		}
	},
	
	/** Signature with generic (class). */
	SIGNATURE_WITH_GENERIC(JDWPCommandSetReferenceType.SIGNATURE_WITH_GENERIC)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__signature(true, __controller, __packet);
		}
	},
	
	/** Fields with generic types. */
	FIELDS_WITH_GENERIC(JDWPCommandSetReferenceType.FIELDS_WITH_GENERIC)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__fields(true, __controller, __packet);
		}
	},
	
	/** Methods with generic types. */
	METHODS_WITH_GENERIC(JDWPCommandSetReferenceType.METHODS_WITH_GENERIC)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__methods(true, __controller, __packet);
		}
	},
	
	/** Class file version. */
	CLASS_FILE_VERSION(JDWPCommandSetReferenceType.CLASS_FILE_VERSION)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/20
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __controller.readType(__packet, false);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Always Java 1.7
			rv.writeInt(51);
			rv.writeInt(0);
			
			return rv;
		}
	},
	
	/** Read the raw constant pool of a class. */
	CONSTANT_POOL(JDWPCommandSetReferenceType.CONSTANT_POOL)
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/01/20
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object type = __controller.readType(__packet, false);
			
			// Read information
			int count = __controller.viewType().constantPoolCount(type);
			byte[] raw = __controller.viewType().constantPoolRaw(type);
			
			// If not existent, this likely a primitive or native type
			if (count < 0 || raw == null)
				return __controller.reply(
					__packet.id(), JDWPErrorType.ABSENT_INFORMATION);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			rv.writeInt(count);
			rv.writeInt(raw.length);
			rv.write(raw, 0, raw.length);
			
			return rv;
		}
	},
	
	/* End. */
	;
	
	/** The base command. */
	public final JDWPCommand command;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/13
	 */
	JDWPHostCommandSetReferenceType(JDWPCommand __id)
	{
		this.command = __id;
		this.id = __id.debuggerId();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/23
	 */
	@Override
	public final JDWPCommand command()
	{
		return this.command;
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
	
	/**
	 * Handles standard or generic field information.
	 * 
	 * @param __generic Write generic fields?
	 * @param __controller The controller used.
	 * @param __packet The packet to read from.
	 * @return The resultant packet.
	 * @throws JDWPException If this is not valid.
	 * @since 2021/04/30
	 */
	JDWPPacket __fields(boolean __generic, JDWPHostController __controller,
		JDWPPacket __packet)
		throws JDWPException
	{
		// Which class does this refer to?
		JDWPViewType viewType = __controller.viewType();
		Object type = __controller.readType(__packet, false);
		
		JDWPPacket rv = __controller.reply(
			__packet.id(), JDWPErrorType.NO_ERROR);
		
		// Do not allow reading the fields of class of weird things can happen
		if ("Ljava/lang/Class;".equals(viewType.signature(type)))
		{
			rv.writeInt(0);
			return rv;
		}
		
		// Write number of fields
		int[] fields = viewType.fields(type);
		rv.writeInt(fields.length);
		
		// Write information on each method
		for (int fieldDx : fields)
		{
			// Information about the method
			rv.writeId(fieldDx);
			rv.writeString(viewType.fieldName(type, fieldDx));
			rv.writeString(viewType.fieldSignature(type, fieldDx));
			
			// Generics are not used in SquirrelJME, ignore
			if (__generic)
				rv.writeString("");
			
			// Modifier flags
			rv.writeInt(viewType.fieldFlags(type, fieldDx));
		}
		
		return rv;
	}
	
	/**
	 * Handles standard or generic method information.
	 * 
	 * @param __generic Write generic methods?
	 * @param __controller The controller used.
	 * @param __packet The packet to read from.
	 * @return The resultant packet.
	 * @throws JDWPException If this is not valid.
	 * @since 2021/04/30
	 */
	JDWPPacket __methods(boolean __generic,
		JDWPHostController __controller, JDWPPacket __packet)
		throws JDWPException
	{
		// Which class does this refer to?
		JDWPViewType viewType = __controller.viewType();
		Object type = __controller.readType(__packet, false);
		
		JDWPPacket rv = __controller.reply(
			__packet.id(), JDWPErrorType.NO_ERROR);
		
		// Write number of methods
		int[] methods = viewType.methods(type);
		rv.writeInt(methods.length);
		
		// Write information on each method
		for (int methodDx : methods)
		{
			// Information about the method
			rv.writeId(methodDx);
			rv.writeString(viewType.methodName(type, methodDx));
			rv.writeString(viewType.methodSignature(type, methodDx));
			
			// Generics are not used in SquirrelJME, ignore
			if (__generic)
				rv.writeString("");
			
			// Modifier flags
			rv.writeInt(viewType.methodFlags(type, methodDx));
		}
		
		return rv;
	}
	
	/**
	 * Returns the signature of a given type.
	 * 
	 * @param __generic Is this a generic signature request?
	 * @param __controller The controller used.
	 * @param __packet The packet to read from.
	 * @return The resultant packet.
	 * @throws JDWPException If this could not be handled.
	 * @since 2021/04/14
	 */
	JDWPPacket __signature(boolean __generic, JDWPHostController __controller,
		JDWPPacket __packet)
		throws JDWPException
	{
		Object type = __controller.readType(__packet, false);
		
		// Write the normal class signature
		JDWPPacket rv = __controller.reply(
			__packet.id(), JDWPErrorType.NO_ERROR);
		
		rv.writeString(__controller.viewType().signature(type));
		if (__generic)
			rv.writeString("");
		
		return rv;
	}
}
