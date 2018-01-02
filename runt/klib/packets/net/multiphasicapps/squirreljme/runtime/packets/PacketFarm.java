// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.packets;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a farm which produces {@link Packet}s using a shared byte array
 * which enables memory to be used more efficiently when there will be multiple
 * packets being created and destroyed.
 *
 * If there is not enough crop available in the farm, then new packets will
 * be allocated which are not managed by the farm.
 *
 * This class is thread safe.
 *
 * @since 2018/01/01
 */
public final class PacketFarm
{
	/** The default farm size */
	private static final int _FARM_SIZE =
		1024;
	
	/** The size of a sub-division in the farm. */
	private static final int _CROP_SIZE =
		128;
	
	/** Lock to protect access to the crops. */
	protected final Object lock =
		new Object();
	
	/** The number of crops in the farm. */
	protected final int numcrops;
	
	/** Crops which are currently being used. */
	private final boolean[] _allocation;
	
	/** The bytes which make up the farm. */
	private final byte[] _field;
	
	/**
	 * Initializes the packet farm with the default farm size.
	 *
	 * @since 2018/01/01
	 */
	public PacketFarm()
	{
		this(PacketFarm._FARM_SIZE, _CROP_SIZE);
	}
	
	/**
	 * Initializes the packet farm with the given farm size.
	 *
	 * @param __l The size of the farm.
	 * @param __cs The size of individual crops within the farm.
	 * @throws IllegalArgumentException If the farm size is zero or negative.
	 * @since 2018/01/01
	 */
	public PacketFarm(int __l, int __cs)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AT07 Invalid farm and/or crop size specified.}
		if (__l <= 0 || __cs <= 0 || (__l % __cs) != 0)
			throw new IllegalArgumentException("AT07");
		
		this._field = new byte[__l];
		
		// The field is split into crops which are then allocated
		int numcrops = __l / __cs;
		this.numcrops = numcrops;
		
		// Use simple flags to determine which crops are allocated
		this._allocation = new boolean[numcrops];
	}
	
	/**
	 * Creates a new variable length packet.
	 *
	 * @param __t The type of the packet, if the type is negative then no
	 * response will be used.
	 * @return The newly created packet of a variable length.
	 * @since 2018/01/01
	 */
	public final Packet create(int __t)
	{
		return __create(true, __t, PacketFarm,_CROP_SIZE, 0);
	}
	
	/**
	 * Creates a new fixed length packet with the specified length.
	 *
	 * @param __t The type of the packet, if the type is negative then no
	 * response will be used.
	 * @param __l The length of the packet to create.
	 * @return The newly created packet of the given length.
	 * @throws IllegalArgumentException If the length is negative.
	 * @since 2018/01/01
	 */
	public final Packet create(int __t, int __l)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AT01 Cannot have a packet with a negative
		// length.}
		if (__l < 0)
			throw new IllegalArgumentException("AT01");
		
		return __create(false, __t, __l, __l);
	}
	
	/**
	 * Creates a new packet.
	 *
	 * @param __var If {@code true} then the packet is variable.
	 * @param __t The type of packet to create.
	 * @param __a The number of bytes to allocate.
	 * @param __l The actual length of the initial packet.
	 * @return The packet.
	 * @since 2018/01/01
	 */
	private final Packet __create(boolean __var, int __t, int __a, int __l)
	{
		// Round up the allocation size to the crop size
		if (true)
			throw new todo.TODO();
		
		// Determine if this packet is just way to large to fit
		if (true)
			throw new todo.TODO();
		
		// If the packet is small enough to nicely fit then search for a free
		// allocation spot
		if (true)
			throw new todo.TODO();
		
		// If there are no free allocation spots, then just create the packet
		if (true)
			throw new todo.TODO();
		
		// Otherwise, allocate some space
		if (true)
			throw new todo.TODO();
		
		throw new todo.TODO();
	}
}

