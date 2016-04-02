// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This contains a mutable set of interfaces which a class implements.
 *
 * Classes cannot implement arrays.
 *
 * @since 2016/03/19
 */
public class JVMClassInterfaces
	extends AbstractSet<ClassNameSymbol>
{
	/** Internal lock. */
	Object lock;
	
	/** The class which owns this interface set. */
	protected final JVMClass owner;
	
	/** Internal interface storage list. */
	private final Set<ClassNameSymbol> _store =
		new LinkedHashSet<>();
	
	/**
	 * Initializes the interface set.
	 *
	 * @param __owner The owner of this class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/19
	 */
	JVMClassInterfaces(JVMClass __owner)
		throws NullPointerException
	{
		// Check
		if (__owner == null)
			throw new NullPointerException("NARG");
		
		// Set
		owner = __owner;
		lock = owner.lock;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws JVMClassFormatError If the added symbol is an array.
	 * @NullPointerException On null arguments.
	 * @since 2016/03/19
	 */
	@Override
	public boolean add(ClassNameSymbol __e)
		throws JVMClassFormatError, NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Cannot implement an array
		if (__e.isArray())
			throw new JVMClassFormatError(String.format("IN10 %s", __e));
		
		// Cannot be Object
		if (__e.equals("java/lang/Object"))
			throw new JVMClassFormatError("IN13");
		
		// Lock
		synchronized (lock)
		{
			// Add it
			return _store.add(__e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/19
	 */
	@Override
	public Iterator<ClassNameSymbol> iterator()
	{
		return new Iterator<ClassNameSymbol>()
			{
				/** The base iterator. */
				protected final Iterator<ClassNameSymbol> base =
					_store.iterator();
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/19
				 */
				@Override
				public boolean hasNext()
				{
					// Lock
					synchronized (lock)
					{
						return base.hasNext();
					}
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/19
				 */
				@Override
				public ClassNameSymbol next()
				{
					// Lock
					synchronized (lock)
					{
						return base.next();
					}
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/03/19
				 */
				@Override
				public void remove()
				{
					// Lock
					synchronized (lock)
					{
						base.remove();
					}
				}
			};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/19
	 */
	@Override
	public int size()
	{
		// Lock
		synchronized (lock)
		{
			return _store.size();
		}
	}
}

