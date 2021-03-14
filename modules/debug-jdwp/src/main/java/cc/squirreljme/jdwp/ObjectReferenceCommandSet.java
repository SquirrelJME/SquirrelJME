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
			JDWPReferenceType ref = __controller.state
				.getReferenceType(__packet.readId());
			if (ref == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_OBJECT);
			
			// Get the true class type
			JDWPClass classy = ref.debuggerClass(); 
			
			// Write the details of this class
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			rv.writeByte(classy.debuggerClassType().id);
			rv.writeId(classy);
			
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
			JDWPReferenceType type = __controller.state
				.getReferenceType(__packet.readId());
			if (type == null)
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
