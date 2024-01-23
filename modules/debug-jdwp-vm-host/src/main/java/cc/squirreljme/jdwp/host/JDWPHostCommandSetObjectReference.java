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
import cc.squirreljme.jdwp.JDWPCommandSetObjectReference;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPValueTag;
import cc.squirreljme.jdwp.host.views.JDWPViewObject;
import cc.squirreljme.jdwp.host.views.JDWPViewType;

/**
 * Object reference command set.
 *
 * @since 2021/03/14
 */
public enum JDWPHostCommandSetObjectReference
	implements JDWPCommandHandler
{
	/** The type that an object is. */
	REFERENCE_TYPE(JDWPCommandSetObjectReference.REFERENCE_TYPE)
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
			// Obtain the object and the type of that object
			Object object = __controller.readObject(__packet, false);
			Object type = (__controller.viewType().isValid(object) ?
				object : __controller.viewObject().type(object));
			
			// Register it for future reference
			__controller.getState().items.put(object);
			if (type != null)
				__controller.getState().items.put(type);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
				
			// Write the details of this class
			__controller.writeTaggedId(rv, type);
			
			return rv;
		}
	},
	
	/** Get field values. */
	GET_VALUES(JDWPCommandSetObjectReference.GET_VALUES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/17
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPViewObject viewObject = __controller.viewObject();
			JDWPViewType viewType = __controller.viewType();
			
			// The type is needed to ensure the field class is valid
			Object object = __controller.readObject(__packet, false);
			Object type = viewObject.type(object);
			
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
					if (!viewObject.readValue(object, fields[i], value))
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
	
	/** Is this object garbage collected? */
	IS_COLLECTED(JDWPCommandSetObjectReference.IS_COLLECTED)
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
			// Read the value but do nothing for it
			__controller.readObject(__packet, false);
			
			// If we still know about this object it was not GCed
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			rv.writeBoolean(false);
			
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
	 * @since 2021/03/14
	 */
	JDWPHostCommandSetObjectReference(JDWPCommand __id)
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
	 * @since 2021/03/14
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
