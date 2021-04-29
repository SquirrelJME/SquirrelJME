// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.jdwp.views.JDWPViewType;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Method command set.
 *
 * @since 2021/03/14
 */
public enum CommandSetMethod
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
			// Read class and the method it is in
			Object classy = __packet.readType(__controller, false);
			int methodId = __packet.readId();
			
			// Not a valid method?
			JDWPViewType viewType = __controller.viewType();
			if (!viewType.isValidMethod(classy, methodId))
				throw ErrorType.INVALID_METHOD_ID.toss(classy, methodId);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
				
			// Put down all the valid indexes in the method, even if there
			// are no possible lines we may still want to break at specific
			// byte code addresses even without the lines?
			long addrCount = viewType.methodLocationCount(classy, methodId);
			rv.writeLong(0);
			rv.writeLong(Math.max(-1, addrCount));
			
			// Obtain the line table to record, ensure that it exists and is
			// valid. If this method has no byte code (abstract/native?),
			// then do nothing.
			int[] lineTable = viewType.methodLineTable(classy, methodId);
			if (addrCount <= 0 || lineTable == null ||
				lineTable.length == 0 || lineTable[0] < 0)
			{
				// No information, so there are no lines
				rv.writeInt(0);
			}
			
			// Otherwise record the information
			else
			{
				// Shrink the table so less information is given
				int countedLines = 0;
				int[] paddedPCs = new int[lineTable.length];
				int[] paddedLines = new int[lineTable.length];
				for (int i = 0, n = lineTable.length, lastLine = -1;
					i < n; i++)
				{
					// If the line has changed, record it
					int at = lineTable[i];
					if (i == 0 || at != lastLine)
					{
						paddedPCs[countedLines] = i;
						paddedLines[countedLines++] = at;
						
						// Do not repeat this line
						lastLine = at;
					}
				}
				
				// Write out the condensed line table
				rv.writeInt(countedLines);
				for (int i = 0, n = countedLines; i < n; i++)
				{
					rv.writeLong(paddedPCs[i]);
					rv.writeInt(Math.max(0, paddedLines[i]));
				}
			}
			
			return rv;
		}
	},
	
	/** Method byte code. */
	BYTE_CODES(3)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/21
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Read class and the method it is in
			Object classy = __packet.readType(__controller, false);
			int methodId = __packet.readId();
			
			// Not a valid method?
			JDWPViewType viewType = __controller.viewType();
			if (!viewType.isValidMethod(classy, methodId))
				throw ErrorType.INVALID_METHOD_ID.toss(classy, methodId);
			
			// Absent information is not returned normally, but in this case
			// return it
			byte[] byteCode = viewType.methodByteCode(classy, methodId);
			if (byteCode == null)
				throw ErrorType.ABSENT_INFORMATION.toss(classy, methodId);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Dump the byte code
			rv.writeInt(byteCode.length);
			rv.write(byteCode, 0, byteCode.length);
			
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
			// Read class and the method it is in
			Object classy = __packet.readType(__controller, false);
			int methodId = __packet.readId();
			
			// Not a valid method?
			JDWPViewType viewType = __controller.viewType();
			if (!viewType.isValidMethod(classy, methodId))
				throw ErrorType.INVALID_METHOD_ID.toss(classy, methodId);
			
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
	CommandSetMethod(int __id)
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
