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

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import net.multiphasicapps.narf.classinterface.NCIByteBuffer;
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCICodeAttribute;
import net.multiphasicapps.narf.classinterface.NCICodeException;
import net.multiphasicapps.narf.classinterface.NCICodeExceptions;
import net.multiphasicapps.narf.classinterface.NCILookup;
import net.multiphasicapps.narf.classinterface.NCIMethod;
import net.multiphasicapps.narf.classinterface.NCIPool;
import net.multiphasicapps.narf.program.NRBasicBlock;
import net.multiphasicapps.narf.program.NRJumpTarget;
import net.multiphasicapps.narf.program.NROp;
import net.multiphasicapps.narf.program.NRProgram;

/**
 * This performs parsing operations.
 *
 * @since 2016/05/08
 */
public final class NCPCodeParser
{
	/** The library for class lookup (optimization). */
	protected final NCILookup lookup;
	
	/** The containing class. */
	protected final NCIClass outerclass;
	
	/** The constant pool. */
	protected final NCIPool constantpool;
	
	/** The method to parse. */
	protected final NCIMethod method;
	
	/** The code attribute. */
	protected final NCICodeAttribute code;
	
	/** The actual code. */
	protected final NCIByteBuffer actual;
	
	/** Basic blocks which are waiting to be returned. */
	protected final List<NRBasicBlock> wait =
		new ArrayList<>();
	
	/** The current queue of operations in the curent basic block. */
	protected final List<NROp> intobasic =
		new ArrayList<>();
	
	/** The current queue of operations in the next basic block. */
	protected final List<NROp> intonextbasic =
		new ArrayList<>();
	
	/** No exceptions to handle at all? */
	protected final boolean nohandlers;
	
	/** The basic block which is associated with the exception handler. */
	protected final NRBasicBlock exceptionhandlerbb;
	
	/** Operation positions. */
	private final int[] _opos;
	
	/**
	 * Parses all operations.
	 *
	 * @param __lu The lookup for other classes.
	 * @param __m The source method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/08
	 */
	public NCPCodeParser(NCILookup __lu, NCIMethod __m)
		throws NullPointerException
	{
		// Check
		if (__lu == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Set
		lookup = __lu;
		method = __m;
		outerclass = __m.outerClass();
		constantpool = outerclass.constantPool();
		code = __m.code();
		actual = code.code();
		
		// Calculation all the operation positions
		int[] opos = new __OpPositions__(actual).get();
		_opos = opos;
		
		// Determine if exceptions are handled
		NCICodeExceptions handlers = code.exceptionHandlers();
		boolean nohandlers = handlers.isEmpty();
		this.nohandlers = nohandlers;
		
		// If there are no exception handlers, then just return from the
		// method without clearing the exception handler register if there is
		// an exception.
		if (nohandlers)
		{
			if (true)
				throw new Error("TODO");
		}
		
		// Otherwise the exception handler table needs to be created and
		// properly initialized.
		else
		{
			if (true)
				throw new Error("TODO");
		}
		
		// Setup block
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the parsed program.
	 *
	 * @return The parsed program.
	 * @since 2016/05/08
	 */
	public NRProgram get()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Converts a logical address to a physical one.
	 *
	 * @param __l The input logical address.
	 * @return The physical address or {@code -1} if it is not matched to an
	 * instruction.
	 * @since 2016/05/08
	 */
	public int logicalToPhysical(int __l)
	{
		// Would never match
		int[] pp = _opos;
		if (__l < 0 || __l >= pp.length)
			return -1;
		
		// Directly represented
		return pp[__l];
	}
	
	/**
	 * Converts a physical address to a logical one.
	 *
	 * @param __p The input physical address.
	 * @return The logical address or {@code -1} if it is not matched to an
	 * instruction.
	 * @since 2016/05/08
	 */
	public int physicalToLogical(int __p)
	{
		// Would never match
		int[] pp = _opos;
		int n = pp.length;
		if (__p < 0 || __p > pp[n - 1])
			return -1;
		
		// Perform a binary search
		for (int lo = 0, hi = n - 1, piv = (n >>> 1); !(piv < 0 || piv >= n);)
		{
			// Get the address at the pivot
			int pva = pp[piv];
			
			// If matched, return it
			if (pva == __p)
				return piv;
			
			// Nothing left?
			if (lo == hi)
				return -1;
			
			// Go higher
			if (__p > pva)
				lo = piv + 1;
			
			// Go lower
			else
				hi = piv - 1;
			
			// Set the new pivot
			piv = lo + ((hi - lo) >>> 1);
		}
		
		// Not found
		return -1;
	}
	
	/**
	 * Pops the current operations which are waiting to be placed into the
	 * current basic block
	 */
	NRBasicBlock __popBlock()
	{
		// Create new block
		NRBasicBlock rv = new NRBasicBlock(intobasic);
		wait.add(rv);
		
		// Clear old instructions
		intobasic.clear();
		
		// If there are any instruction waiting to be placed into the next
		// block then place them in the current one.
		intobasic.addAll(intonextbasic);
		intonextbasic.clear();
		
		// Return the block
		return rv;
	} 
}

