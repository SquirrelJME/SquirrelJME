// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * This is used to manage pointers within SpringCoat.
 *
 * @since 2019/12/21
 */
@Deprecated
public final class SpringPointerManager
{
	/** Queue for cleared references. */
	@Deprecated
	private final ReferenceQueue<SpringObject> _queue =
		new ReferenceQueue<>();
	
	/** Reference to integer. */
	@Deprecated
	private final Map<Reference<SpringObject>, Integer> _rtoi =
		new HashMap<>();
	
	/** Integer to reference. */
	@Deprecated
	private final Map<Integer, Reference<SpringObject>> _itor =
		new HashMap<>();
	
	/** The next allocation address. */
	@Deprecated
	private int _next =
		4;
	
	/**
	 * Allocates and returns a new pointer area.
	 *
	 * @param __l The length to allocate.
	 * @return The resulting pointer.
	 * @throws IllegalArgumentException If the length is negative.
	 * @since 2019/12/21
	 */
	@Deprecated
	public final SpringPointerArea allocate(int __l)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BK3g Cannot allocate negative pointer space.}
		if (__l < 0)
			throw new IllegalArgumentException("BK3g");
		
		// Determine base pointer
		int base = this._next;
		
		// {@squirreljme.error BK3h Ran out of address space allocating
		// object. (The requested length)}
		if (base < 0)
			throw new SpringVirtualMachineException("BK3h " + __l);
		
		// Set next position
		this._next = base + ((__l + 3) & ~3);
		
		// Build pointer here
		return new SpringPointerArea(base, __l);
	}
	
	/**
	 * Allocates a pointer of the given length and binds the specified object
	 * to that pointer.
	 *
	 * @param __l The length to allocate.
	 * @param __o The object to bind.
	 * @return The resulting pointer.
	 * @throws IllegalArgumentException If the length is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/22
	 */
	@Deprecated
	public final SpringPointerArea allocateAndBind(int __l, SpringObject __o)
		throws IllegalArgumentException, NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Allocate pointer
		SpringPointerArea rv = this.allocate(__l);
		
		// Setup reference
		Reference<SpringObject> ref = new WeakReference(__o, this._queue);
		
		// Bind it
		synchronized (this)
		{
			Integer ibase = rv.base;
			
			// Store into both referencing maps
			this._rtoi.put(ref, ibase);
			this._itor.put(ibase, ref);
		}
		
		// Return the result
		return rv;
	}
	
	/**
	 * Locates the object at the given address.
	 *
	 * @param __p The object to find.
	 * @return The object which was found.
	 * @since 2019/12/22
	 */
	@Deprecated
	public final SpringObject findObject(int __p)
	{
		// Zero address always returns null pointers!
		if (__p == 0)
			return SpringNullObject.NULL;
		
		// Need to use both maps
		Map<Reference<SpringObject>, Integer> rtoi = this._rtoi;
		Map<Integer, Reference<SpringObject>> itor = this._itor;
		
		// Lock on self
		synchronized (this)
		{
			// First try to cleanup anything that was cleared reference
			// wise
			ReferenceQueue<SpringObject> queue = this._queue;
			for (;;)
			{
				// Any new ones appeared?
				Reference<? extends SpringObject> polled = queue.poll();
				if (polled == null)
					break;
				
				// Read integer base
				Integer ibase = rtoi.get(polled);
				
				// Clear from both maps
				rtoi.remove(polled);
				itor.remove(ibase);
			}
			
			// Try to find object reference
			Reference<SpringObject> ref = itor.get(__p);
			
			// {@squirreljme.error BK3i Could not find object referenced at
			// the given pointer. (The requested address)}
			SpringObject rv = (ref != null ? ref.get() : null);
			if (rv == null)
				throw new SpringVirtualMachineException(
					String.format("BK3i @%08x", __p));
			
			return rv;
		}
	}
}

