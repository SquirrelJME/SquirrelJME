// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Array references.
 *
 * @since 2021/03/19
 */
public enum CommandSetArrayReference
	implements JDWPCommand
{
	/** Length of array. */
	LENGTH(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/19
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which object do we want?
			JDWPObject object = __controller.state.oldObjects.get(
				__packet.readId());
			if (object == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_OBJECT);
			
			// Not an array?
			if (!(object instanceof JDWPArray))
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_ARRAY);
			JDWPArray array = (JDWPArray)object;
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			rv.writeInt(array.debuggerArrayLength());
			
			return rv;
		}
	},
	
	/** Get values. */
	GET_VALUES(2)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/19
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which object do we want?
			JDWPObject object = __controller.state.oldObjects.get(
				__packet.readId());
			if (object == null)
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_OBJECT);
			
			// Not an array?
			if (!(object instanceof JDWPArray))
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_ARRAY);
			JDWPArray array = (JDWPArray)object;
			
			// Component type?
			String componentType = array.debuggerComponentDescriptor();
			
			// Read requested data indexes
			int offset = __packet.readInt();
			int length = __packet.readInt();
			
			// Invalid index?
			if (offset < 0 || length < 0 ||
				(offset + length) > array.debuggerArrayLength())
				return __controller.__reply(
					__packet.id(), ErrorType.INVALID_LENGTH);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Write compactified array details, the tag if it is primitive
			// (anything that is not L) will be treated as untagged values
			int tag;
			rv.writeByte((tag = (componentType.length() == 1 ?
				componentType.charAt(0) : 'L')));
			rv.writeInt(length);
			
			// Default fallback value if a value could not be read.
			Object fallback = null;
			switch (tag)
			{
				case 'Z': fallback = false; break;
				case 'B': fallback = (byte)0; break;
				case 'S': fallback = (short)0; break;
				case 'C': fallback = '\0'; break;
				case 'I': fallback = 0; break;
				case 'J': fallback = 0L; break;
				case 'F': fallback = 0F; break;
				case 'D': fallback = 0D; break;
			}
			
			// Go through and read all the array values
			for (int i = 0; i < length; i++)
				try (JDWPValue value = __controller.__value())
				{
					// No valid value here? Write a valid placeholder!
					if (!array.debuggerArrayGet(offset + i, value))
						rv.writeValue(fallback, componentType, true);
					else
					{
						// Write as untagged if available
						rv.writeValue(value, componentType, true);
						
						// Store object for later use
						Object rawVal = value.get();
						if (rawVal instanceof JDWPObject)
							__controller.state.oldObjects.put((JDWPObject)rawVal);
					}
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
	 * @since 2021/03/19
	 */
	CommandSetArrayReference(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/19
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
