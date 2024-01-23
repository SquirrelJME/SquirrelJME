// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.host.views.JDWPViewObject;
import cc.squirreljme.jdwp.host.views.JDWPViewType;

/**
 * Object reference command set.
 *
 * @since 2021/03/14
 */
public enum CommandSetObjectReference
	implements JDWPCommand
{
	/** The type that an object is. */
	REFERENCE_TYPE(1)
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
			// Obtain the object and the type of that object
			Object object = __packet.readObject(__controller, false);
			Object type = (__controller.viewType().isValid(object) ?
				object : __controller.viewObject().type(object));
			
			// Register it for future reference
			__controller.state.items.put(object);
			if (type != null)
				__controller.state.items.put(type);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
				
			// Write the details of this class
			rv.writeTaggedId(__controller, type);
			
			return rv;
		}
	},
	
	/** Get field values. */
	GET_VALUES(2)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/17
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPViewObject viewObject = __controller.viewObject();
			JDWPViewType viewType = __controller.viewType();
			
			// The type is needed to ensure the field class is valid
			Object object = __packet.readObject(__controller, false);
			Object type = viewObject.type(object);
			
			// Read in all field indexes and check for their validity
			int numFields = __packet.readInt();
			int[] fields = new int[numFields];
			for (int i = 0; i < numFields; i++)
			{
				int fieldDx = __packet.readId();
				if (!viewType.isValidField(type, fieldDx))
					throw ErrorType.INVALID_FIELD_ID.toss(type, fieldDx);
				
				fields[i] = fieldDx;
			}
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Write field mappings
			rv.writeInt(numFields);
			for (int i = 0; i < numFields; i++)
				try (JDWPValue value = __controller.value())
				{
					// Determine the field type and its tag
					String fieldSig = viewType.fieldSignature(type, fields[i]);
					JDWPValueTag tag = JDWPValueTag.fromSignature(fieldSig);
					
					// Read the field value, fallback if not valid
					if (!viewObject.readValue(object, fields[i], value))
						value.set(tag.defaultValue);
					
					// Always write as tagged value
					rv.writeValue(__controller, value, tag, false);
					
					// Store object for later use
					if (value.get() != null && tag.isObject)
						__controller.state.items.put(value.get());
				}
			
			return rv;
		}
	},
	
	/** Is this object garbage collected? */
	IS_COLLECTED(9)
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
			// Read the value but do nothing for it
			__packet.readObject(__controller, false);
			
			// If we still know about this object it was not GCed
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeBoolean(false);
			
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
	 * @since 2021/03/14
	 */
	CommandSetObjectReference(int __id)
	{
		this.id = __id;
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
