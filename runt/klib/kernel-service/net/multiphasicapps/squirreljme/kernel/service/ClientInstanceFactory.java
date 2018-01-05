// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.service;

/**
 * This is used to create instances of {@link ClientInstance} which is used
 * to communicate with a remote service.
 *
 * @since 2018/01/05
 */
public interface ClientInstanceFactory
{
	/**
	 * Creates a new client which uses the given packet stream to
	 * communicate with the service instance in the kernel.
	 *
	 * @param __sps The stream to send packets into.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public abstract ClientInstance createClient(ServicePacketStream __sps)
		throws NullPointerException;
}

