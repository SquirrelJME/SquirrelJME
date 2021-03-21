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
 * Object reference command set.
 *
 * @since 2021/03/14
 */
public enum ObjectReferenceCommandSet
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
			// This can be a large number of sets of objects
			int id = __packet.readId();
			JDWPObjectLike object = __controller.state.getObjectLike(id);
			if (object == null)
			{
				// Debug
				JDWPId any = __controller.state.any(id);
				Debugging.debugNote("JDWP: InvalidRef: %s (0x%08x a %s)",
					any, id, (any == null ? "null" :
						any.getClass().getName()));
				
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_OBJECT);
			}
			
			// The true class may be synthetic but may also be real!
			JDWPClass classy = __controller.state.getObjectLikeClass(object);
			
			// Write the details of this class
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeByte(classy.debuggerClassType().id);
			rv.writeId(classy);
			
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
			// Which object do we want?
			JDWPObject object = __controller.state.objects.get(
				__packet.readId());
			if (object == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_OBJECT);
			
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
			JDWPClass classy = object.debuggerClass();
			if (classy == null)
				classy = __Synthetics__.FAKE_OBJECT;
			
			// Write field mappings
			rv.writeInt(numFields);
			for (int i = 0; i < numFields; i++)
				try (JDWPValue value = __controller.__value())
				{
					// No valid value here? just result in void
					if (!classy.debuggerFieldValue(object, fields[i],
						value))
						rv.writeVoid();
					else
					{
						rv.writeValue(value, fields[i].debuggerMemberType(),
							false);
						
						// Store object for later use
						Object rawVal = value.get();
						if (rawVal instanceof JDWPObject)
							__controller.state.objects.put((JDWPObject)rawVal);
					}
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
			// This could refer to any object
			JDWPObjectLike object = __controller.state.getObjectLike(
				__packet.readId());
			if (object == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_OBJECT);
			
			// No objects get garbage collected as they exist when they
			// exist!
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
	ObjectReferenceCommandSet(int __id)
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
