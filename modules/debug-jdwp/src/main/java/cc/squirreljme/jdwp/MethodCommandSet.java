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
 * Method command set.
 *
 * @since 2021/03/14
 */
public enum MethodCommandSet
	implements JDWPCommand
{
	/** Line number table. */
	LINE_TABLE(1)
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
			// Ignore class it is not needed
			__packet.readId();
			
			// Find the method
			JDWPMethod method = __controller.state.methods.get(
				__packet.readId());
			if (method == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_METHOD_ID);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Obtain the line table to record, ensure that it exists and is
			// valid.
			int[] lineTable = method.debuggerLineTable();
			if (lineTable == null || lineTable.length == 0 || lineTable[0] < 0)
			{
				// No information, so it is treated like if it were native
				rv.writeLong(-1);
				rv.writeLong(-1);
				rv.writeInt(0);
			}
			
			// Otherwise record the information
			else
			{
				// Size of the line table
				rv.writeLong(0);
				rv.writeLong(lineTable.length);
				
				// Write out the line table
				rv.writeInt(lineTable.length);
				for (int i = 0, n = lineTable.length; i < n; i++)
				{
					rv.writeLong(i);
					rv.writeInt(Math.max(0, lineTable[i]));
				}
			}
			
			return rv;
		}
	},
	
	/** Variable table with generics. */
	VARIABLE_TABLE_WITH_GENERIC(5)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/15
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Ignore class it is not needed
			__packet.readId();
			
			// Find the method
			JDWPMethod method = __controller.state.methods.get(
				__packet.readId());
			if (method == null)
				return __controller.__reply(
				__packet.id(), ErrorType.INVALID_METHOD_ID);
			
			// TODO: Implement
			Debugging.todoNote("Implement VariableTableWithGeneric.");
			return __controller.__reply(
				__packet.id(), ErrorType.ABSENT_INFORMATION);
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
	MethodCommandSet(int __id)
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
