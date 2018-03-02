// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.datagrampackets;

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
	/**
	 * {@squirreljme.property cc.squirreljme.kernel.packets.defaultfarmcount=n
	 * This represents the number of global farms which exist by default and
	 * are available to use as needed. The higher this value the more memory
	 * will be used to have these farms initialized. This value must be
	 * positive.}
	 */
	public static final String FARM_COUNT_PROPERTY =
		"cc.squirreljme.kernel.packets.defaultfarmcount";
	
	/**
	 * {@squirreljme.property cc.squirreljme.kernel.packets.defaultfarmsize=n
	 * This represents the default size that farms initialized without a size
	 * will be. The higher this value the more memory will be used when
	 * default farms are initialized. This value must be a power of two and
	 * be positive.}
	 */
	public static final String DEFAULT_FARM_SIZE_PROPERTY =
		"cc.squirreljme.kernel.packets.defaultfarmsize";
	
	/**
	 * {@squirreljme.property cc.squirreljme.kernel.packets.defaultcroptsize=n
	 * This represents the default size of each crop within the farm. Fields
	 * are split into crops. Each crop can be used to contain the space of a
	 * packet. This value must be a power of two and be positive, it must not
	 * be greater than the farm size.}
	 */
	public static final String DEFAULT_CROP_SIZE_PROPERTY =
		"cc.squirreljme.kernel.packets.defaultcropsize";
	
	/** The default number of global farms. */
	public static final int FARM_COUNT =
		Math.max(1, Integer.getInteger(FARM_COUNT_PROPERTY, 8));
	
	/** The default size for individual farms. */
	public static final int DEFAULT_FARM_SIZE =
		Integer.highestOneBit(Math.max(8,
			Integer.getInteger(DEFAULT_FARM_SIZE_PROPERTY, 2048)));
	
	/** The default crop size. */
	public static final int DEFAULT_CROP_SIZE =
		Math.min(DEFAULT_FARM_SIZE / 2, Integer.highestOneBit(Math.max(32,
			Integer.getInteger(DEFAULT_CROP_SIZE_PROPERTY, 128))));
	
	/** Globally initialized farms. */
	private static final PacketFarm[] _GLOBAL_FARMS;
	
	/** The next global farm to create a packet from. */
	private static volatile int _nextglobalfarm;
	
	/** Lock to protect access to the crops. */
	protected final Object lock =
		new Object();
		
	/** Is this a global farm? */
	protected final boolean isglobalfarm;
	
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
	 * Initializes the global farms.
	 *
	 * @since 2018/01/17
	 */
	static
	{
		int n = FARM_COUNT;
		PacketFarm[] gf = new PacketFarm[n];
		for (int i = 0; i < n; i++)
			gf[i] = new PacketFarm(true, DEFAULT_FARM_SIZE, DEFAULT_CROP_SIZE);
		_GLOBAL_FARMS = gf;
	}
	
	/**
	 * Initializes the packet farm with the default farm size.
	 *
	 * @since 2018/01/01
	 */
	public PacketFarm()
	{
		this(false, DEFAULT_FARM_SIZE, DEFAULT_CROP_SIZE);
	}
	
	/**
	 * Initializes the packet farm with the given farm size.
	 *
	 * @param __l The size of the farm.
	 * @param __cs The size of individual crops within the farm.
	 * @throws IllegalArgumentException If the farm and or crop size is
	 * zero, negative, or is not a power of two.
	 * @since 2018/01/01
	 */
	public PacketFarm(int __l, int __cs)
		throws IllegalArgumentException
	{
		this(false, __l, __cs);
	}
	
	/**
	 * Initializes the packet farm with the given farm size.
	 *
	 * @param __global Is this a global packet farm? This is used as a key for
	 * packets so they know where to source new packets from.
	 * @param __l The size of the farm.
	 * @param __cs The size of individual crops within the farm.
	 * @throws IllegalArgumentException If the farm and or crop size is
	 * zero, negative, or is not a power of two.
	 * @since 2018/01/17
	 */
	private PacketFarm(boolean __global, int __l, int __cs)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AT0b Invalid farm and/or crop size specified.
		// (The farm size; The crop size)}
		if (__l <= 0 || __cs <= 0 || (__l % __cs) != 0 ||
			Integer.bitCount(__l) != 1 || Integer.bitCount(__cs) != 1)
			throw new IllegalArgumentException(
				String.format("AT0b %d %d", __l, __cs));
		
		this.isglobalfarm = __global;
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
		// {@squirreljme.error AT0c Cannot have a packet with a negative
		// length.}
		if (__l < 0)
			throw new IllegalArgumentException("AT0c");
		
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
		
		// {@squirreljme.error AT0d The crop mask or allocation size is not
		// a multiple of the crop size.}
		if ((__o & cropmask) != 0 || (__a & cropmask) != 0)
			throw new IllegalStateException("AT0d");
		
		// Determine allocation positions
		int pivot = __o / cropsize,
			endpivot = pivot + (__a / cropsize);
		
		// Lock to prevent contention among the field
		synchronized (this.lock)
		{
			boolean[] allocation = this._allocation;
			
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
		
		// Is this a global farm?
		boolean isglobalfarm = this.isglobalfarm;
		
		// Determine if this packet is just way to large to fit
		boolean cropless = (__a > this.maxcropuse);
		if (cropless)
			return new Packet(this, __t, __var, new byte[__l], 0, __l, __l,
				false, isglobalfarm);
		
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
					false, isglobalfarm);
			
			// The end of crop usage
			int endpivot = pivot + usecrops;
			
			// Allocate the space
			for (int i = pivot; i < endpivot; i++)
				allocation[i] = true;
			
			// Zero out the space
			byte[] field = this._field;
			int doff = pivot * cropsize;
			for (int woff = doff, end = doff + __l; woff < end; woff++)
				field[woff] = 0;
			
			// Use that area in the field
			return new Packet(this, __t, __var, field, doff, __a, __l, true,
				isglobalfarm);
		}
	}
	
	/**
	 * Creates a new variable length packet.
	 *
	 * @param __t The type of the packet, if the type is negative then no
	 * response will be used.
	 * @return The newly created packet of a variable length.
	 * @since 2018/01/17
	 */
	public static final Packet createPacket(int __t)
	{
		return PacketFarm.__nextFarm().create(__t);
	}
	
	/**
	 * Creates a new fixed length packet with the specified length.
	 *
	 * @param __t The type of the packet, if the type is negative then no
	 * response will be used.
	 * @param __l The length of the packet to create.
	 * @return The newly created packet of the given length.
	 * @throws IllegalArgumentException If the length is negative.
	 * @since 2018/01/17
	 */
	public static final Packet createPacket(int __t, int __l)
		throws IllegalArgumentException
	{
		return PacketFarm.__nextFarm().create(__t, __l);
	}
	
	/**
	 * Returns the next packet farm to use.
	 *
	 * @return The next packet farm.
	 * @since 2018/01/17
	 */
	private static final PacketFarm __nextFarm()
	{
		// Cycle to the next farm
		int next = PacketFarm._nextglobalfarm + 1;
		if (next >= FARM_COUNT)
			next = 0;
		PacketFarm._nextglobalfarm = next;
		
		// Use that farm
		return PacketFarm._GLOBAL_FARMS[next];
	}
}

