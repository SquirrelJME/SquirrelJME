// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This is an abstract set which provides only a few more methods than the
 * base collection class.
 *
 * @param <E> The storage type.
 * @since 2018/12/07
 */
public abstract class AbstractSet<E>
	extends AbstractCollection<E>
	implements Set<E>
{
	/**
	 * Requires that the class be extended.
	 *
	 * @since 2018/10/10
	 */
	protected AbstractSet()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof Set))
			return false;
		
		// Compare size first
		Set<?> o = (Set<?>)__o;
		if (this.size() != o.size())
			return false;
		
		// Just check if this set contains everything in the other set
		return this.containsAll(o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public int hashCode()
	{
		int rv = 0;
		for (E e : this)
			rv += (e == null ? 0 : e.hashCode());
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	public boolean removeAll(Collection<?> __c)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Iterate over our set and remove
		boolean did = false;
		if (this.size() <= __c.size())
		{
			for (Iterator<E> it = this.iterator(); it.hasNext();)
			{
				E e = it.next();
				
				if (__c.contains(e))
				{
					it.remove();
					did = true;
				}
			}
		}
		
		// Iterate over the other set and remove from ours
		else
		{
			for (Iterator<?> it = __c.iterator(); it.hasNext();)
			{
				Object e = it.next();
				
				if (this.contains(e))
				{
					this.remove(e);
					did = true;
				}
			}
		}
		
		return did;
	}
}

