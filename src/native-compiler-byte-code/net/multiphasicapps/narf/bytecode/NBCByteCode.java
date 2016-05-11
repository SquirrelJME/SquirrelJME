// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.bytecode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.List;
import net.multiphasicapps.narf.classinterface.NCIByteBuffer;
import net.multiphasicapps.narf.classinterface.NCICodeAttribute;
import net.multiphasicapps.narf.classinterface.NCIMethod;

/**
 * This class contains the main representation of Java byte code.
 *
 * @since 2016/05/11
 */
public class NBCByteCode
	extends AbstractList<NBCOperation>
{
	/** The containing method. */
	protected final NCIMethod method;
	
	/** The code attribute. */
	protected final NCICodeAttribute attribute;
	
	/** The actual byte code. */
	protected final NCIByteBuffer code;
	
	/** The instruction count. */
	protected final int count;
	
	/** The positions for all logical operations. */
	private final int[] _logpos;
	
	/** Byte code operation cache. */
	private final Reference<NBCOperation>[] _bops;
	
	/**
	 * Initilizes the byte code representation.
	 *
	 * @param __m The containing method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/11
	 */
	public NBCByteCode(NCIMethod __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.method = __m;
		
		// Extract code data
		NCICodeAttribute attribute = __m.code();
		this.attribute = attribute;
		NCIByteBuffer code = attribute.code();
		this.code = code;
		
		// Setup logical positions
		int[] logpos = new __OpPositions__(code).get();
		_logpos = logpos;
		
		// Setup output byte operations
		int n = logpos.length;
		count = n;
		_bops = __makeBops(n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/11
	 */
	@Override
	public NBCOperation get(int __i)
	{
		// Check
		if (__i < 0 || __i >= count)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Lock on the bops
		Reference<NBCOperation>[] bops = _bops;
		synchronized (bops)
		{
			// Get
			Reference<NBCOperation> ref = bops[__i];
			NBCOperation rv;
			
			// Check
			if (ref == null || null == (rv = ref.get()))
				bops[__i] = new WeakReference<>(
					(rv = new NBCOperation(this, __i)));
			
			// Return it
			return rv;
		}
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
		int[] pp = _logpos;
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
		int[] pp = _logpos;
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
	 * {@inheritDoc}
	 * @since 2016/05/11
	 */
	@Override
	public int size()
	{
		return count;
	}
	
	/**
	 * Makes the byte operation reference array.
	 *
	 * @param __n The number of operations to store.
	 * @return The array of references.
	 * @since 2016/05/11
	 */
	@SuppressWarnings({"unchecked"})
	private static Reference<NBCOperation>[] __makeBops(int __n)
	{
		return (Reference<NBCOperation>[])((Object)new Reference[__n]);
	}
}

