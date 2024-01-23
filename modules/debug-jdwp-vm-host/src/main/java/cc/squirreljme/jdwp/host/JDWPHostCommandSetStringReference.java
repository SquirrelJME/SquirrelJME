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
import cc.squirreljme.jdwp.JDWPCommandSetStringReference;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.host.views.JDWPViewObject;
import cc.squirreljme.jdwp.host.views.JDWPViewType;

/**
 * String reference command set.
 *
 * @since 2021/03/20
 */
public enum JDWPHostCommandSetStringReference
	implements JDWPCommandHandler
{
	/** Return the value of the string. */
	VALUE(JDWPCommandSetStringReference.VALUE)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/20
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Get the desired object
			Object object = __controller.readObject(__packet, false);
			
			// Is this the string type?
			JDWPViewObject viewObject = __controller.viewObject();
			JDWPViewType viewType = __controller.viewType();
			Object type = viewObject.type(object);
			if (type == null || !JDWPHostCommandSetStringReference._STRING.equals(
				viewType.signature(type)))
				throw JDWPErrorType.INVALID_STRING.toss(object,
					System.identityHashCode(object));
			
			// Locate the char field index
			int charFieldDx = JDWPHostUtils.findFieldId(viewType, type,
				JDWPHostCommandSetStringReference._STRING_CHARS, "[C");
			
			// Is missing? We do not know how strings work then
			if (charFieldDx < 0)
				throw JDWPErrorType.NOT_IMPLEMENTED.toss(type,
					charFieldDx);
			
			// Load the character array
			Object charArray;
			try (JDWPHostValue value = __controller.value())
			{
				// Is this a valid field?
				if (!viewObject.readValue(object, charFieldDx, value))
					throw JDWPErrorType.INVALID_STRING.toss(object, charFieldDx);
				
				// Missing?
				charArray = value.get();
				if (charArray == null)
					throw JDWPErrorType.INVALID_STRING.toss(object, charFieldDx);
			}
			
			// How big is this string?
			int strLen = viewObject.arrayLength(charArray);
			if (strLen < 0)
				throw JDWPErrorType.INVALID_STRING.toss(object, strLen);
			
			// Load in characters
			char[] chars = new char[strLen];
			for (int i = 0; i < strLen; i++)
				try (JDWPHostValue value = __controller.value())
				{
					// Is this value valid?
					if (!viewObject.readArray(charArray, i, value))
						value.set(JDWPHostCommandSetStringReference._BAD_CHAR);
					
					// Try to map the value
					Object raw = value.get();
					if (raw instanceof Character)
						chars[i] = (char)raw;
					else if (raw instanceof Number)
						chars[i] = (char)((Number)raw).intValue();
					
					// Could not read it so treat as unknown
					else
						chars[i] = JDWPHostCommandSetStringReference._BAD_CHAR;
				}
			
			// Report string value
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			rv.writeString(new String(chars));
			return rv;
		}
	},
	
	/* End. */
	;
	
	/** The base command. */
	public final JDWPCommand command;
	
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
	JDWPHostCommandSetStringReference(JDWPCommand __id)
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
	 * @since 2021/03/20
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
