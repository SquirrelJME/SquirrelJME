// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.bytecode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIAccessibleFlags;
import net.multiphasicapps.squirreljme.ci.CIAccessibleObject;
import net.multiphasicapps.squirreljme.ci.CIByteBuffer;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIClassFlags;
import net.multiphasicapps.squirreljme.ci.CICodeAttribute;
import net.multiphasicapps.squirreljme.ci.CIException;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIPool;
import net.multiphasicapps.squirreljme.cldc.IndexedBinaryComparator;
import net.multiphasicapps.squirreljme.cldc.IndexedBinarySearch;
import net.multiphasicapps.util.empty.EmptyMap;
import net.multiphasicapps.util.singleton.SingletonMap;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;

/**
 * This class contains the main representation of Java byte code.
 *
 * This class is immutable.
 *
 * @since 2016/05/11
 */
public final class BCByteCode
	extends AbstractList<BCOperation>
{
	/** The comparator for finding verification states. */
	private static final IndexedBinaryComparator<BCStateVerification[],
		Integer> _VERIFY_SEARCH =
		new IndexedBinaryComparator<BCStateVerification[], Integer>()
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/06/22
			 */
			@Override
			public int binaryCompare(BCStateVerification[] __q, Integer __a,
				int __b)
			{
				int a = __a.intValue();
				int b = __q[__b].logicalAddress();
				
				if (a < b)
					return -1;
				else if (a > b)
					return 1;
				return 0;
			}
		};
	
	/** The containing method. */
	protected final CIMethod method;
	
	/** The code attribute. */
	protected final CICodeAttribute attribute;
	
	/** The actual byte code. */
	protected final CIByteBuffer code;
	
	/** The instruction count. */
	protected final int count;
	
	/** Explicit verification states. */
	private final BCStateVerification[] _xverif;
	
	/** The positions for all logical operations. */
	private final int[] _logpos;
	
	/** Byte code operation cache. */
	private final Reference<BCOperation>[] _bops;
	
	/** Raw byte code operation cache. */
	private final Reference<BCRawOperation>[] _rops;
	
	/**
	 * Initilizes the byte code representation.
	 *
	 * @param __m The containing method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/11
	 */
	public BCByteCode(CIMethod __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.method = __m;
		
		// Extract code data
		CICodeAttribute attribute = __m.code();
		this.attribute = attribute;
		CIByteBuffer code = attribute.code();
		this.code = code;
		
		// Setup logical positions
		int[] logpos = new __OpPositions__(code).get();
		_logpos = logpos;
		
		// Setup output byte operations
		int n = logpos.length;
		count = n;
		_bops = BCByteCode.<BCOperation>__makeBops(n);
		_rops = BCByteCode.<BCRawOperation>__makeBops(n);
		
		// Decode the stack map table
		CIByteBuffer os = attribute.stackMapOld();
		CIByteBuffer ns = attribute.stackMapNew();
		
		// Old states
		if (os != null)
			this._xverif = new __StackMapParser__(false, os, this).result();
		
		// New States
		else if (ns != null)
			this._xverif = new __StackMapParser__(true, ns, this).result();
		
		// None used
		else
			this._xverif = new BCStateVerification[]
				{
					new BCStateVerification(__m)
				};
	}
	
	/**
	 * Returns the code attribute that this byte code representation is based
	 * on.
	 *
	 * @return The code attribute this represents byte code for.
	 * @since 2016/05/13
	 */
	public final CICodeAttribute attribute()
	{
		return this.attribute;
	}
	
	/**
	 * Returns the raw code buffer.
	 *
	 * @return The code buffer.
	 * @since 2016/05/13
	 */
	public final CIByteBuffer codeBuffer()
	{
		return this.code;
	}
	
	/**
	 * Returns the constant pool associated with this class.
	 *
	 * @return The associated constant pool.
	 * @since 2016/05/12
	 */
	public final CIPool constantPool()
	{
		return this.method.outerClass().constantPool();
	}
	
	/**
	 * Returns the explicit verification state which is defined for an input
	 * logical address.
	 *
	 * @return The explicit verification for the given logical address or
	 * {@code null} if it does not use one.
	 * @since 2016/05/12
	 */
	public final BCStateVerification explicitVerification(int __l)
	{
		// Search for it
		BCStateVerification[] xverif = this._xverif;
		int dx = IndexedBinarySearch.<BCStateVerification[], Integer>
			search(xverif, __l, 0, xverif.length, _VERIFY_SEARCH);
		
		// Was it found?
		if (dx >= 0)
			return xverif[dx];
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/11
	 */
	@Override
	public final BCOperation get(int __i)
		throws IndexOutOfBoundsException
	{
		// Check
		if (__i < 0 || __i >= count)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Lock on the bops
		Reference<BCOperation>[] bops = _bops;
		synchronized (bops)
		{
			// Get
			Reference<BCOperation> ref = bops[__i];
			BCOperation rv;
			
			// Check
			if (ref == null || null == (rv = ref.get()))
				bops[__i] = new WeakReference<>(
					(rv = new BCOperation(this, getRaw(__i))));
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Obtains the raw operation which just contains the operation code and
	 * any arguments that are passed to the given operation.
	 *
	 * @param __i The logical index of the instruction to get.
	 * @return The raw operation for the given instruction.
	 * @throws IndexOutOfBoundsException If the instruction is not within
	 * bounds.
	 * @since 2016/06/22
	 */
	public final BCRawOperation getRaw(int __i)
		throws IndexOutOfBoundsException
	{
		// Check
		if (__i < 0 || __i >= count)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Lock
		Reference<BCRawOperation>[] rops = this._rops;
		synchronized (rops)
		{
			// Get
			Reference<BCRawOperation> ref = rops[__i];
			BCRawOperation rv;
			
			// Cache?
			if (ref == null || null == (rv = ref.get()))
				rops[__i] = new WeakReference<>(
					(rv = new BCRawOperation(this, this.code, __i)));
			
			// Return
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
	public final int logicalToPhysical(int __l)
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
	public final int physicalToLogical(int __p)
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
	public final int size()
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
	private static <V> Reference<V>[] __makeBops(int __n)
	{
		return (Reference<V>[])((Object)new Reference[__n]);
	}
}

