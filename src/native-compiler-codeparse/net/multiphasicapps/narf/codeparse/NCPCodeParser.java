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

import net.multiphasicapps.narf.classinterface.NCIByteBuffer;
import net.multiphasicapps.narf.classinterface.NCIClass;
import net.multiphasicapps.narf.classinterface.NCICodeAttribute;
import net.multiphasicapps.narf.classinterface.NCILookup;
import net.multiphasicapps.narf.classinterface.NCIMethod;
import net.multiphasicapps.narf.classinterface.NCIPool;
import net.multiphasicapps.narf.program.NRBasicBlock;
import net.multiphasicapps.narf.program.NRProgram;
import net.multiphasicapps.util.boxed.BoxedIntegerList;

/**
 * This class is given a method which is then parsed.
 *
 * @since 2016/04/20
 */
public class NCPCodeParser
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
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
	
	/** Has this work been done already? */
	private volatile boolean _did;
	
	/** Operation positions. */
	private volatile int[] _opos;
	
	/**
	 * Initializes the code parser.
	 *
	 * @param __lib The lookup to find class definitions (used for final
	 * optimizations and such).
	 * @param __m The method to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public NCPCodeParser(NCILookup __lib, NCIMethod __m)
		throws NullPointerException
	{
		// Check
		if (__lib == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Set
		lookup = __lib;
		method = __m;
		outerclass = __m.outerClass();
		constantpool = outerclass.constantPool();
		code = __m.code();
		actual = code.code();
	}
	
	/**
	 * Parses and returns the resultant program.
	 *
	 * @return The decoded program.
	 * @since 2016/04/27
	 */
	public NRProgram get()
	{
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error ND01 Program parsing is being or has already
			// been performed.}
			if (_did)
				throw new IllegalStateException("ND01");
			_did = true;
		}
		
		// Local cache
		NCICodeAttribute code = this.code;
		NCIByteBuffer actual = this.actual;
		
		// Calculation all the operation positions
		int[] opos = new __OpPositions__(actual).get();
		_opos = opos;
		
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
}

