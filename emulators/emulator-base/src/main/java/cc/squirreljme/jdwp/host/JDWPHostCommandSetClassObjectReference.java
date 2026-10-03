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
import cc.squirreljme.jdwp.JDWPCommandSetClassObjectReference;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.host.views.JDWPViewObject;
import cc.squirreljme.jdwp.host.views.JDWPViewType;

/**
 * Handles class object references.
 *
 * @since 2021/04/30
 */
public enum JDWPHostCommandSetClassObjectReference
	implements JDWPCommandHandler
{
	/** The actual type of the class object. */
	REFLECTED_TYPE(JDWPCommandSetClassObjectReference.REFLECTED_TYPE)
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
			Object object = __controller.readObject(__packet, false);
			
			// This must be Class<?>...
			JDWPViewType viewType = __controller.viewType();
			JDWPViewObject viewObject = __controller.viewObject();
			if (viewObject == null || !viewObject.isValid(viewObject))
			{
				Object objectType = __controller.viewObject().type(object);
				
				// Ensure this is correct
				if (objectType == null || !viewType.isValid(objectType) ||
					!"Ljava/lang/Class;"
						.equals(viewType.signature(objectType)))
					throw JDWPErrorType.INVALID_OBJECT.toss(object,
						System.identityHashCode(object),
						new Throwable("Not a Class object."));
			}
			
			// Ask the viewer to get the type for this Class reference
			Object found = viewType.typeOfClassInstance(object);
			if (found != null && viewType.isValid(found))
				__controller.getState().items.put(found);
			
			// Could not directly get, so search through all types to find
			if (found == null || !viewType.isValid(found))
				for (Object type : __controller.allTypes(false))
				{
					// Is this a match?
					Object maybe = viewType.instance(type);
					if (maybe == object)
					{
						found = type;
						break; 
					}
				}
			
			// Not found?
			if (found == null || !viewType.isValid(found))
				throw JDWPErrorType.INVALID_OBJECT.toss(object,
					System.identityHashCode(object),
					new Throwable("No matching type found."));
					
			// Make sure it is known
			__controller.getState().items.put(found);
			
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Write the class type and reference to that type
			__controller.writeTaggedId(rv, found);
			
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
	 * @since 2021/04/30
	 */
	JDWPHostCommandSetClassObjectReference(JDWPCommand __id)
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
	 * @since 2021/04/30
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
