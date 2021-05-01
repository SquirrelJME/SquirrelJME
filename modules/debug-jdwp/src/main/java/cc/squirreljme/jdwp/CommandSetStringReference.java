// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.views.JDWPViewObject;
import cc.squirreljme.jdwp.views.JDWPViewType;

/**
 * String reference command set.
 *
 * @since 2021/03/20
 */
public enum CommandSetStringReference
	implements JDWPCommand
{
	/** Return the value of the string. */
	VALUE(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/20
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Get the desired object
			Object object = __packet.readObject(__controller, false);
			
			// Is this the string type?
			JDWPViewObject viewObject = __controller.viewObject();
			JDWPViewType viewType = __controller.viewType();
			Object type = viewObject.type(object);
			if (type == null || !CommandSetStringReference._STRING.equals(
				viewType.signature(type)))
				throw ErrorType.INVALID_STRING.toss(object,
					System.identityHashCode(object));
			
			// Locate the char field index
			int charFieldDx = -1;
			for (int fieldId : viewType.fields(type))
				if (CommandSetStringReference._STRING_CHARS.equals(
					viewType.fieldName(type, fieldId)))
				{
					charFieldDx = fieldId;
					break;
				}
			
			// Is missing? We do not know how strings work then
			if (charFieldDx < 0)
				throw ErrorType.NOT_IMPLEMENTED.toss(type,
					charFieldDx);
			
			// Load the character array
			Object charArray;
			try (JDWPValue value = __controller.value())
			{
				// Is this a valid field?
				if (!viewObject.readValue(object, charFieldDx, value))
					throw ErrorType.INVALID_STRING.toss(object, charFieldDx);
				
				// Missing?
				charArray = value.get();
				if (charArray == null)
					throw ErrorType.INVALID_STRING.toss(object, charFieldDx);
			}
			
			// How big is this string?
			int strLen = viewObject.arrayLength(charArray);
			if (strLen < 0)
				throw ErrorType.INVALID_STRING.toss(object, strLen);
			
			// Load in characters
			char[] chars = new char[strLen];
			for (int i = 0; i < strLen; i++)
				try (JDWPValue value = __controller.value())
				{
					// Is this value valid?
					if (!viewObject.readArray(charArray, i, value))
						value.set(CommandSetStringReference._BAD_CHAR);
					
					// Try to map the value
					Object raw = value.get();
					if (raw instanceof Character)
						chars[i] = (char)raw;
					else if (raw instanceof Number)
						chars[i] = (char)((Number)raw).intValue();
					
					// Could not read it so treat as unknown
					else
						chars[i] = CommandSetStringReference._BAD_CHAR;
				}
			
			// Report string value
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeString(new String(chars));
			return rv;
		}
	},
	
	/* End. */
	;
	
	/** Badly represented character. */
	private static final char _BAD_CHAR =
		0xFFFD;
	
	/** String character array. */
	private static final String _STRING_CHARS =
		"_chars";
	
	/** String class. */
	private static final String _STRING = 
		"Ljava/lang/String;";
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/20
	 */
	CommandSetStringReference(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/20
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
