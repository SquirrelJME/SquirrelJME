// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.packets;

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
		2048;
	
	/** The size of a sub-division in the farm. */
	private static final int _CROP_SIZE =
		128;
	
	/** Lock to protect access to the crops. */
	protected final Object lock =
		new Object();
	
	/** The size of the farm. */
	protected final int farmsize;
	
	/** The crop size used. */
	protected final int cropsize;
	
	/** The mask for the crop size. */
	protected final int cropmask;
	
	/** The number of crops in the farm. */
	protected final int numcrops;
	
	/** The maximum size for a packet before it is not in the farm. */
	protected final int maxcropuse;
	
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
		this(PacketFarm._FARM_SIZE, PacketFarm._CROP_SIZE);
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
		if (__l <= 0 || __cs <= 0 || (__l % __cs) != 0 ||
			Integer.bitCount(__cs) != 1)
			throw new IllegalArgumentException("AT07");
		
		this.farmsize = __l;
		this._field = new byte[__l];
		
		// The field is split into crops which are then allocated
		int numcrops = __l / __cs;
		this.cropsize = __cs;
		this.cropmask = __cs - 1;
		this.numcrops = numcrops;
		this.maxcropuse = (__cs * (numcrops - (numcrops / 4)));
		
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
		return __create(true, __t, 0, 0);
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
	 * Closes the specified region and frees the crop space up.
	 *
	 * @param __o The starting offset.
	 * @param __a The allocation.
	 * @throws IllegalStateException If the offset and/or allocation size is
	 * not a multiple of the crop size.
	 * @since 2018/01/02
	 */
	final void __close(int __o, int __a)
		throws IllegalStateException
	{
		int cropsize = this.cropsize,
			cropmask = this.cropmask,
			numcrops = this.numcrops;
		
		// {@squirreljme.error AT08 The crop mask or allocation size is not
		// a multiple of the crop size.}
		if ((__o & cropmask) != 0 || (__a & cropmask) != 0)
			throw new IllegalStateException("AT08");
		
		// Determine allocation positions
		int pivot = __o / cropsize,
			endpivot = pivot + (__a / cropsize);
		
		// Lock to prevent contention among the field
		synchronized (this.lock)
		{
			boolean[] allocation = this._allocation;
			
			// Debug usage
			System.err.printf("DEBUG -- Free (%2d-%2d) ", pivot, endpivot);
			for (int i = 0; i < numcrops; i++)
			{
				boolean alloc = allocation[i];
				
				if (i >= pivot && i < endpivot)
					if (alloc)
						System.err.print('F');
					else
						System.err.print('f');
				else
					if (alloc)
						System.err.print('+');
					else
						System.err.print('-');
			}
			System.err.println();
			
			// Free the space
			for (int i = pivot; i < endpivot; i++)
				allocation[i] = false;
		}
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
		int cropsize = this.cropsize,
			cropmask = this.cropmask,
			numcrops = this.numcrops;
		if (__a < __l)
			__a = __l;
		if (__a == 0 || (__a & cropmask) != 0)
			__a = ((__a + cropsize) & (~cropmask));
		
		System.err.printf("DEBUG -- Allocate: %d%n", __a);
		
		// Determine if this packet is just way to large to fit
		boolean cropless = (__a > this.maxcropuse);
		if (cropless)
			return new Packet(this, __t, __var, new byte[__l], 0, __l, __l,
				false);
		
		// Lock to prevent contention among the field
		synchronized (this.lock)
		{
			boolean[] allocation = this._allocation;
			int usecrops = __a / cropsize;
			
			// Search for free crop space to consume
			int pivot = -1;
			for (int i = 0; i < numcrops; i++)
			{
				// This spot is taken
				if (allocation[i])
					continue;
				
				// Determine if there is room to fit this crop
				boolean taken = false;
				for (int j = i, je = i + usecrops; j < je; j++)
					if (allocation[j])
					{
						taken = true;
						break;
					}
				
				// No room
				if (taken)
					continue;
				
				// Otherwise use this spot
				pivot = i;
				break;
			}
		
			// If there are no free allocation spots, then just create the
			// packet outside of the farm
			if (pivot < 0)
				return new Packet(this, __t, __var, new byte[__l], 0, __l, __l,
					false);
			
			// The end of crop usage
			int endpivot = pivot + usecrops;
			
			// Debug usage
			System.err.printf("DEBUG -- Allocate (%2d-%2d) ", pivot, endpivot);
			for (int i = 0; i < numcrops; i++)
			{
				boolean alloc = allocation[i];
				
				if (i >= pivot && i < endpivot)
					if (alloc)
						System.err.print('A');
					else
						System.err.print('a');
				else
					if (alloc)
						System.err.print('+');
					else
						System.err.print('-');
			}
			System.err.println();
			
			// Allocate the space
			for (int i = pivot; i < endpivot; i++)
				allocation[i] = true;
			
			// Zero out the space
			byte[] field = this._field;
			int doff = pivot * cropsize;
			for (int woff = doff, end = doff + __l; woff < end; woff++)
				field[woff] = 0;
			
			// Use that area in the field
			return new Packet(this, __t, __var, field, doff, __a, __l, true);
		}
	}
}

