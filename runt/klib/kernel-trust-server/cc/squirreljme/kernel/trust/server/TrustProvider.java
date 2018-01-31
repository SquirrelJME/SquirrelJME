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
		synchronized (trusts)
		{
			// See if a trust already exists
			int nextid = 1;
			for (SystemTrustGroup rv : trusts.values())
			{
				// Use a unique trust ID always
				int dx;
				if ((dx = rv.index()) >= nextid)
					nextid = dx + 1;
				
				// Ignore trusted ones
				if (rv.isTrusted())
					continue;
				
				// Matches this trust
				if (__name.equals(rv.name()) &&
					__vendor.equals(rv.vendor()))
					return rv;
			}
			
			// No trust exists, so the provider must create it and store it
			throw new todo.TODO();
		}
	}
}

