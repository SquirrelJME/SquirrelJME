// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import java.lang.ref.Reference;

/**
 * This represents a chain of references used to store references to
 * object references.
 *
 * This class is used internally and should not be accessed or used directly
 *
 * @since 2020/03/10
 */
public final class ReferenceChain
{
	/** The head of the chain. */
	__Link__ _head;
	
	/**
	 * Initializes a new reference chain.
	 *
	 * @since 2020/03/10
	 */
	public ReferenceChain()
	{
		// Count this up twice to prevent this reference chain from being GCed
		// until it is force GCed
		Assembly.refCountUp(this);
		Assembly.refCountUp(this);
	}
	
	/**
	 * Pushes this reference to the garbage collector.
	 *
	 * @param __ref The reference to push.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/10
	 */
	public void push(Reference<?> __ref)
		throws NullPointerException
	{
		if (__ref == null)
			throw new NullPointerException("NARG");
		
		// Fresh link?
		if (this._head == null)
			this._head = new __Link__(__ref);
		
		// Needs to be linked in
		else
			throw todo.TODO.TODO();
	}
	
	/**
	 * Individual chain link.
	 *
	 * @since 2020/03/15
	 */
	static final class __Link__
	{
		/** The reference that is linked in. */
		final Reference<?> _ref;
		
		/** The previous link. */
		__Link__ _prev;
		
		/** The next link. */
		__Link__ _next;
		
		/**
		 * Initializes the link.
		 *
		 * @param __ref The reference to link.
		 * @since 2020/03/15
		 */
		__Link__(Reference<?> __ref)
		{
			this._ref = __ref;
		}
	}
}
