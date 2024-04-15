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
import cc.squirreljme.jdwp.JDWPCommandSetArrayReference;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.JDWPValueTag;
import cc.squirreljme.jdwp.host.views.JDWPViewObject;
import cc.squirreljme.jdwp.host.views.JDWPViewType;

/**
 * Array references.
 *
 * @since 2021/03/19
 */
public enum JDWPHostCommandSetArrayReference
	implements JDWPCommandHandler
{
	/** Length of array. */
	LENGTH(JDWPCommandSetArrayReference.LENGTH)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/19
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Which object do we want?
			Object array = __controller.readArray(__packet, false);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			rv.writeInt(__controller.viewObject().arrayLength(array));
			
			return rv;
		}
	},
	
	/** Get values. */
	GET_VALUES(JDWPCommandSetArrayReference.GET_VALUES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/19
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			Object array = __controller.readArray(__packet, false);
			
			// Obtain the component type of the array
			JDWPViewType viewType = __controller.viewType();
			Object componentType = viewType.componentType(
				__controller.viewObject().type(array));
			
			// Read requested data indexes
			int off = __packet.readInt();
			int len = __packet.readInt();
			
			// Get array length
			JDWPViewObject viewObject = __controller.viewObject();
			int arrayLength = viewObject.arrayLength(array);
			
			// Invalid index?
			if (off < 0 || len < 0 || (off + len) > arrayLength)
				throw JDWPErrorType.INVALID_LENGTH.toss(array, off + len);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Write compactified array details, the tag if it is primitive
			// (anything that is not L) will be treated as untagged values
			JDWPValueTag tag = JDWPValueTag.fromSignature(
				viewType.signature(componentType));
			rv.writeByte(tag.tag);
			rv.writeInt(len);
			
			// Go through and read all the array values
			for (int i = 0; i < len; i++)
				try (JDWPHostValue value = __controller.value())
				{
					if (!viewObject.readArray(array, off + i, value))
						value.set(tag.defaultValue);
					
					// Write as untagged if available
					__controller.writeValue(rv, value, tag, true);
					
					// Store object for later use
					if (value.get() != null && tag.isObject)
						__controller.getState().items.put(value.get());
				}
			
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
	 * @since 2021/03/19
	 */
	JDWPHostCommandSetArrayReference(JDWPCommand __id)
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
	 * @since 2021/03/19
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
