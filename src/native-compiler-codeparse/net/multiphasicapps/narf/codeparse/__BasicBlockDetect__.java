// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.codeparse;

import java.util.Map;
import net.multiphasicapps.narf.classinterface.NCIByteBuffer;
import net.multiphasicapps.narf.classinterface.NCICodeException;
import net.multiphasicapps.narf.classinterface.NCICodeExceptions;

/**
 * This class detects the start location of basic blocks.
 *
 * @since 2016/05/09
 */
class __BasicBlockDetect__
{
	/** The output block data. */
	protected final Map<Integer, __Block__> out;
	
	/** The logical program length. */
	protected final int length;
	
	/** The used parser. */
	protected final NCPCodeParser parser;
	
	/**
	 * Initializes the basic block detector.
	 *
	 * @param __bb The input buffer containing operations.
	 * @param __lops The logical operation positions.
	 * @param __out The output block information.
	 * @param __ex Exception handlers for jump targets.
	 * @param __ncp the code parser used for operation detection.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/09
	 */
	__BasicBlockDetect__(NCIByteBuffer __bb, int[] __lops,
		Map<Integer, __Block__> __out, NCICodeExceptions __ex,
		NCPCodeParser __ncp)
		throws NullPointerException
	{
		// Check
		if (__bb == null || __lops == null || __out == null || __ex == null ||
			__ncp == null)
			throw new NullPointerException("NARG");
		
		// Set
		parser = __ncp;
		out = __out;
		int n;
		length = (n = __lops.length);
		
		// Go through all exceptions and check that they have valid start and
		// end positions along with valid handler locations
		int apl = __bb.length();
		for (NCICodeException x : __ex)
		{
			// Get all logical addresses
			int ls = __ncp.physicalToLogical(x.startAddress());
			
			// The end could be at the program bounds
			int le = x.endAddress();
			if (le != apl)
				le = __ncp.physicalToLogical(le);
			
			// {@squirreljme.error AR06 An exception handler does not have a
			// valid range within the program. (The exception)}
			if (ls < 0 || le < 0)
				throw new NCPException(NCPException.Issue.EXCEPTION_RANGE,
					String.format("AR06 %s", x));
			
			// Emit block at the handler location
			emit(x.handlerAddress()).setExceptionHandler();
		}
		
		throw new Error("TODO");
	}
	
	/**
	 * Potentially initializes a basic block at the given operation.
	 *
	 * @param __pos The position to emit a basic block at.
	 * @return The block that was emitted or was at the current position.
	 * @throws NCPException If the position is not within bounds of the
	 * program.
	 * @since 2016/05/09
	 */
	public __Block__ emit(int __pos)
		throws NCPException
	{
		// Map the position, which is in byte code to the program
		int log = parser.physicalToLogical(__pos);
		
		// {@squirreljme.error AR05 Jump target of an instruction is outside
		// the bounds of the program or does not map to an instruction in the
		// program. (The position; The number of available instructions)}
		if (log < 0 || log >= length)
			throw new NCPException(NCPException.Issue.ILLEGAL_BLOCK_START,
				String.format("AR05 %d %d", __pos, length));
		
		// Add block if it does not exist
		Integer k = Integer.valueOf(log);
		Map<Integer, __Block__> map = out;
		__Block__ bl = map.get(k);
		
		// Return it
		if (bl != null)
			return bl;
		
		// Otherwise create it
		bl = new __Block__();
		map.put(k, bl);
		return bl;
	}
}

