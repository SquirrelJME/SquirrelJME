// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.trust.server;

import cc.squirreljme.kernel.trust.client.TrustClient;
import cc.squirreljme.kernel.trust.client.TrustClientFactory;
import cc.squirreljme.kernel.service.ServicePacketStream;
import cc.squirreljme.kernel.service.ServiceProvider;
import cc.squirreljme.runtime.cldc.SystemTask;
import cc.squirreljme.runtime.cldc.SystemTrustGroup;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * This manages the trusts which are available to be used.
 *
 * @since 2018/01/17
 */
public abstract class TrustProvider
	extends ServiceProvider
{
	/** Lock to prevent multiple threads from accessing trusts. */
	protected final Object lock =
		new Object();
	
	/** The trusts which are available. */
	private final Map<Integer, SystemTrustGroup> _trusts =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the base trust provider.
	 *
	 * @since 2018/01/17
	 */
	public TrustProvider()
	{
		super(TrustClient.class, TrustClientFactory.class);
	}
	
	/**
	 * Creates the specified trust group.
	 *
	 * @param __trusted If {@code true} then this group is trusted.
	 * @param __dx The index that the trust will be created under.
	 * @param __name The name this trust is for.
	 * @param __vendor The vendor this trust is for.
	 * @return The newly created trust.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/31
	 */
	protected abstract SystemTrustGroup createTrustGroup(boolean __trusted,
		int __dx, String __name, String __vendor)
		throws NullPointerException;
	
	/**
	 * Returns the trust by the given index.
	 *
	 * @param __dx The index of the trust to get.
	 * @return The given trust or {@code null} if it does not exist.
	 * @since 2018/02/12
	 */
	public final SystemTrustGroup byIndex(int __dx)
	{
		// Lock
		Map<Integer, SystemTrustGroup> trusts = this._trusts;
		synchronized (this.lock)
		{
			return trusts.get(__dx);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/17
	 */
	@Override
	public final TrustServer createInstance(SystemTask __st,
		ServicePacketStream __ps)
		throws NullPointerException
	{
		if (__st == null || __ps == null)
			throw new NullPointerException("NARG");
		
		return new TrustServer(this, __st, __ps);
	}
	
	/**
	 * Registers a new trust group.
	 *
	 * @param __t The trust group to register.
	 * @throws IllegalStateException If a trust already exists with the given
	 * index or has a
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/31
	 */
	protected final void register(SystemTrustGroup __t)
		throws IllegalStateException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		Integer idx = __t.index();
		
		// Lock
		Map<Integer, SystemTrustGroup> trusts = this._trusts;
		synchronized (this.lock)
		{
			// {@squirreljme.error BK04 Attempt to register a trust with a
			// duplicate index. (The index)}
			if (trusts.containsKey(idx))
				throw new IllegalStateException(String.format("BK04 %d", idx));
			
			// {@squirreljme.error BK05 Cannot add a trust which refers to the
			// same group. (The first group; The second group}}
			for (SystemTrustGroup v : trusts.values())
				if (TrustProvider.isSameReference(v, __t))
					throw new IllegalStateException(String.format("BK05 %s %s",
						__t, v));
			
			// Is okay, so add it
			trusts.put(idx, __t);
		}
	}
	
	/**
	 * Returns a trust group for an untrusted program which goes by the
	 * specified name.
	 *
	 * @param __name The midlet name.
	 * @param __vendor The midlet vendor.
	 * @return The trust group for the midlet.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/31
	 */
	public final SystemTrustGroup untrustedTrust(String __name,
		String __vendor)
		throws NullPointerException
	{
		if (__name == null || __vendor == null)
			throw new NullPointerException("NARG");
		
		// Need to go through every trust to find a match
		Map<Integer, SystemTrustGroup> trusts = this._trusts;
		synchronized (this.lock)
		{
			// See if a trust already exists
			int nextid = 1;
			for (SystemTrustGroup rv : trusts.values())
			{
				// Use a unique trust ID always
				int dx;
				if ((dx = rv.index()) >= nextid)
					nextid = dx + 1;
				
				// Matches this trust
				if (TrustProvider.isSameReference(rv, false, __name, __vendor))
					return rv;
			}
			
			// No trust exists, so the provider must create it and store it
			SystemTrustGroup rv = this.createTrustGroup(false, nextid,
				__name, __vendor);
			
			// {@squirreljme.error BK03 No trust created or it had a different
			// index, name, or vendor.}
			if (rv == null || rv.index() != nextid ||
				!__name.equals(rv.name()) || !__vendor.equals(rv.vendor()))
				throw new RuntimeException("BK03");
			
			// Register it
			this.register(rv);
			return rv;
		}
	}
	
	/**
	 * Checks if the two trust groups refer to the same trust.
	 *
	 * @param __a The first trust group.
	 * @param __b The second trust group.
	 * @return {@code true} if they are equal.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/31
	 */
	public static final boolean isSameReference(SystemTrustGroup __a,
		SystemTrustGroup __b)
		throws NullPointerException
	{
		if (__a == null || __b == null)
			throw new NullPointerException("NARG");
		
		return TrustProvider.isSameReference(__a, __b.isTrusted(), __b.name(),
			__b.vendor());
	}
	
	/**
	 * Checks if the given trust group refers to the given parameters.
	 *
	 * @param __a The first trust group.
	 * @param __btrusted Is the second
	 * @return {@code true} if they are equal.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/31
	 */
	public static final boolean isSameReference(SystemTrustGroup __a,
		boolean __btrusted, String __bname, String __bvendor)
		throws NullPointerException
	{
		if (__a == null || __bname == null || __bvendor == null)
			throw new NullPointerException("NARG");
		
		return __a.isTrusted() == __btrusted &&
			__bname.equals(__a.name()) &&
			__bvendor.equals(__a.vendor());
	}
}

