// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

/**
 * This is a data sink which supports the CRC 32 algorithm.
 *
 * This class is not thread safe.
 *
 * @since 2016/07/16
 */
public class CRC32Calculator
	implements Checksum
{
	/** The polynomial to use. */
	protected final int polynomial;
	
	/** The final XOR value. */
	protected final int finalXor;
	
	/** Reflect the data? */
	protected final boolean reflectData;
	
	/** Reflect the remainder? */
	protected final boolean reflectRemainder;
	
	/** The initial remainder. */
	protected final int initRemainder;
	
	/** The CRC Table. */
	final CRC32Table _table;
	
	/** Solo buffer. */
	private final byte[] _solo =
		new byte[1];
	
	/** The current CRC value (remainder). */
	private volatile int _remainder;
	
	/**
	 * Initializes the CRC-32 data sink.
	 *
	 * @param __rdata Reflect the data?
	 * @param __rrem Reflect the remainder?
	 * @param __poly The polynomial.
	 * @param __initrem The initial remainder.
	 * @param __fxor The value to XOR the remainder with on return.
	 * @since 2016/07/16
	 */
	public CRC32Calculator(boolean __rdata, boolean __rrem, int __poly,
		int __initrem, int __fxor)
	{
		// Set
		this.reflectData = __rdata;
		this.reflectRemainder = __rrem;
		this.polynomial = __poly;
		this.finalXor = __fxor;
		this.initRemainder = __initrem;
		this._remainder = __initrem;
		
		// Setup table
		this._table = CRC32Table.calculateTable(__poly);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/16
	 */
	@Override
	public final int checksum()
	{
		// Return the current CRC
		int rem = this._remainder;
		return (this.reflectRemainder ? Integer.reverse(rem) : rem) ^
			this.finalXor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
	public final void offer(byte __b)
	{
		byte[] solo = this._solo;
		solo[0] = __b;
		this.offer(solo, 0, 1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
	public final void offer(byte[] __b)
		throws NullPointerException
	{
		this.offer(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
	public final void offer(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("BAOB");
		
		// Read data into the work buffer
		boolean reflectData = this.reflectData;
		int remainder = this._remainder;
		int[] table = this._table._table;
		for (int i = __o, end = __o + __l; i < end; i++)
		{
			// Read in data value
			int val = __b[i] & 0xFF;
		
			// Reflect the data?
			if (reflectData)
				val = Integer.reverse(val) >>> 24;
		
			int d = (val ^ (remainder >>> 24));
			remainder = table[d] ^ (remainder << 8);
		}
		
		// Set new remainder
		this._remainder = remainder;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/05
	 */
	@Override
	public final void reset()
	{
		// Only the remainder has to be updated
		this._remainder = this.initRemainder;
	}
	
	/**
	 * Calculates the checksum using the given parameters.
	 *
	 * @param __rdata Reflect the data?
	 * @param __rrem Reflect the remainder?
	 * @param __poly The polynomial.
	 * @param __initrem The initial remainder.
	 * @param __fxor The value to XOR the remainder with on return.
	 * @param __b The buffer to calculate.
	 * @return The checksum.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public static final int calculate(boolean __rdata, boolean __rrem,
		int __poly, int __initrem, int __fxor, byte[] __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		return CRC32Calculator.calculate(__rdata, __rrem, __poly, __initrem,
			__fxor, __b, 0, __b.length);
	}
	
	/**
	 * Calculates the checksum using the given parameters.
	 *
	 * @param __rdata Reflect the data?
	 * @param __rrem Reflect the remainder?
	 * @param __poly The polynomial.
	 * @param __initrem The initial remainder.
	 * @param __fxor The value to XOR the remainder with on return.
	 * @param __b The buffer to calculate.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the buffer.
	 * @return The checksum.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public static final int calculate(boolean __rdata, boolean __rrem,
		int __poly, int __initrem, int __fxor, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Initialize calculator
		CRC32Calculator calc = new CRC32Calculator(__rdata, __rrem,
			__poly, __initrem, __fxor);
		
		// Calculate and return checksum
		calc.offer(__b, __o, __l);
		return calc.checksum();
	}
	
	/**
	 * Calculates the checksum in accordance to Zip files.
	 *
	 * @param __b The buffer to calculate.
	 * @return The checksum.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public static final int calculateZip(byte[] __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		return CRC32Calculator.calculateZip(__b, 0, __b.length);
	}
	
	/**
	 * Calculates the checksum in accordance to Zip files.
	 *
	 * @param __b The buffer to calculate.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the buffer.
	 * @return The checksum.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	public static final int calculateZip(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		return CRC32Calculator.calculate(true, true,
			0x04C11DB7, 0xFFFFFFFF, 0xFFFFFFFF,
			__b, __o, __l);
	}
}

