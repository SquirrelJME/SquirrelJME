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
			if (type == null ||
				!JDWPHostCommandSetStringReference._STRING.equals(
					viewType.signature(type)))
				throw JDWPErrorType.INVALID_STRING.toss(object,
					System.identityHashCode(object));
			
			// Request the string data
			String str = viewObject.readString(object);
			if (str == null)
				throw JDWPErrorType.INVALID_STRING.toss(object,
					System.identityHashCode(object));
			
			// Report string value
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			rv.writeString(str);
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
