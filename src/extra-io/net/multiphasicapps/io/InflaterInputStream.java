// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.collections.HuffmanTree;

/**
 * This input stream reads deflated input (using the deflate algorithm) and
 * decompresses it to provide the original data.
 *
 * This follows RFC 1951.
 *
 * @since 2016/03/09
 */
public class InflaterInputStream
	extends InputStream
{
	/** The initial ring buffer size. */
	protected static final int INITIAL_RING_BUFFER_SIZE =
		8;
	
	/** The size of the sliding window. */
	protected static final int SLIDING_WINDOW_SIZE =
		32768;
	
	/** No compression. */
	protected static final int TYPE_NO_COMPRESSION =
		0b00;
	
	/** Fixed huffman table compression. */
	protected static final int TYPE_FIXED_HUFFMAN =
		0b01;
	
	/** Dynamic huffman table compression. */
	protected static final int TYPE_DYNAMIC_HUFFMAN =
		0b10;
	
	/** An error. */
	protected static final int TYPE_ERROR =
		0b11;
	
	/** Global static huffman tree cache. */
	private static volatile Reference<HuffmanTree<Integer>> _GLOBAL_TREE;
	
	/** Lock on the global tree. */
	private static final Object _GLOBAL_TREE_LOCK =
		new Object();
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The wrapped bit stream. */
	protected final BitInputStream in;
	
	/** The sliding window where historical bytes may be referenced. */
	protected final SlidingByteWindow slidingwindow =
		new SlidingByteWindow(SLIDING_WINDOW_SIZE);
	
	/** The bit compactor for queing added bits. */
	protected final BitCompactor compactor;
	
	/** Finished reading? */
	private volatile boolean _finished;
	
	/** Ring buffer read/write queue (must be power of 2). */
	private volatile byte[] _ring;
	
	/** The read index in the queue. */
	private volatile int _read =
		-1;
	
	/** The write index in the queue. */
	private volatile int _write =
		-1;
	
	/**
	 * This initializes the input stream which is used to inflate deflated
	 * data.
	 *
	 * @param __w The input deflated stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/09
	 */
	public InflaterInputStream(InputStream __w)
		throws NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException();
		
		// Set
		in = new BitInputStream(__w, false);
		
		// Setup compactor
		compactor = new BitCompactor(new BitCompactor.Callback()
			{
				/**
				 * {@inheritDoc}
				 * @since 2016/03/10
				 */
				@Override
				public void ready(byte __v)
				{
					System.err.println("Ready: " + Integer.toHexString(__v));
					// Lock
					synchronized (lock)
					{
						// Get the ring buffer
						byte[] ring = _ring;
						
						// Needs creation?
						if (ring == null)
						{
							_ring = ring = new byte[INITIAL_RING_BUFFER_SIZE];
							_read = _write = 0;
						}
						
						// Ring buffer length
						int len = ring.length;
						
						// Get the read and write position
						int r = _read;
						int w = _write;
						
						// The next write position
						int nw = (w + 1) & (len - 1);
						
						// Would overflow? Need to increase the buffer size
						// since the code using the inflater class is not
						// pulling enough bytes out
						if (nw == r)
						{System.err.println("Collided " + nw + " " + r);
							// The queue has just collected a large number
							// of bytes which were never collected.
							if (len >= 0x4000_0000)
								throw new IllegalStateException();
							
							// Create new buffer
							byte[] creat = new byte[len << 1];
							int clen = creat.length;
							
							// The new read/write position
							int xr = 0;
							int xw = 0;
							
							// Copy the old ring to the new one
							int or = r;
							while (or != w)
							{
								// Write here
								creat[xw] = ring[or];
								
								// Move position up
								or = (or + 1) & (len - 1);
								xw = (xw + 1) & (clen - 1);
							}
							
							for (int i = 0; i < ring.length; i++)
								System.err.printf("%02x", ring[i]);
							System.err.println();
							for (int i = 0; i < creat.length; i++)
								System.err.printf("%02x", creat[i]);
							System.err.println();
							
							// Set new ring buffer data
							r = xr;
							w = xw;
							len = creat.length;
							nw = (w + 1) & (clen - 1);
							_ring = ring = creat;
						}
						
						// Write into it (at the current write position)
						ring[w] = __v;
						
						// Set new positions
						_read = r;
						_write = nw;
					}
				}
			});
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/09
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close the wrapped stream
		in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/09
	 */
	@Override
	public int read()
		throws IOException
	{
		// Lock
		synchronized (lock)
		{
			// There might not be enough data for an output read to occur.
			for (;;)
			{
				// Are there bytes waiting to be given out?
				if (_ring != null)
				{
					// Get positions
					int r = _read;
					int w = _write;
					
					// Not at the end?
					if (r != w)
					{
						// Get byte here
						int rv = _ring[r++];
						
						// Cap to buffer size
						_read = r & (_ring.length - 1);
						
						// Return it
						return rv;
					}
				}
				
				// If finished, then stop
				if (_finished)
					return -1;
			
				// Is this the final block?
				boolean isfinal = in.read();
			
				// Mark finished?
				_finished |= isfinal;
			
				// Read the compression type
				int type = (int)in.readBits(2);
				
				// DEBUG
				System.err.printf("DEBUG -- Finl: %s%n", isfinal);
				System.err.printf("DEBUG -- Type: %d%n", type);
				System.err.flush();
			
				// No compression
				if (type == TYPE_NO_COMPRESSION)
					throw new Error("TODO");
				
				// Huffman compressed (static tree)
				else if (type == TYPE_FIXED_HUFFMAN)
				{
					// Loop until the end is reached
					for (;;)
					{
						// Read fixed code
						int val = __readFixed();
						
						System.err.println("read fixed " + val);
						
						// Literal value?
						if (val < 256)
							compactor.add(val, 0xFF);
						
						// Stop?
						else if (val == 256)
							break;
						
						// Use window value
						else if (val >= 257 && val <= 285)
							throw new Error("TODO");
						
						// Illegal value
						else
							throw new InflaterException.IllegalSequence();
					}
				}
				
				// Huffman compressed (dynamic)
				else if (type == TYPE_DYNAMIC_HUFFMAN)
				{
					// The tree to use for the data
					HuffmanTree<Integer> ht;
					
					// Load in dynamic huffman table?
					if (type == TYPE_DYNAMIC_HUFFMAN)
						throw new Error("TODO");
				
					// Use the fixed one
					else
						ht = __fixedTree();
					
					// Start at the root of the tree
					HuffmanTree<Integer>.Traverse rover = ht.root();
					
					// Loop until the end of the block is reached
					for (;;)
					{
						// Read bit value here
						int bit = (int)in.readBits(1);
						System.err.println("Read bit " + bit);
						
						// Get node for this side
						HuffmanTree<Integer>.Node node = rover.get(bit);
						
						// If a value, stop
						if (node.isLeaf())
						{
							// Read value
							Integer value = node.asLeaf().get();
							
							// Illegal?
							if (value == null)
								throw new InflaterException.NoValueForBits();
							
							// Normal value
							if (value < 256)
								compactor.add(value, 0xFF);
							
							// Stop parsing?
							else if (value == 256)
								break;
							
							// Access the window
							else if (value >= 257 && value <= 285)
								throw new Error("TODO");
							
							// Illegal value
							else
								throw new InflaterException.IllegalSequence();
							
							// Go back to the root node to read the next value
							rover = ht.root();
						}
						
						// Go into it if a traverse
						else if (node.isTraverse())
							rover = node.asTraverse();
						
						// Otherwise not part of the tree
						else
							throw new InflaterException.NoValueForBits();
					}
				}
			
				// Unknown or error
				else
					throw new InflaterException.HeaderErrorTypeException();
			}
		}
	}
	
	/**
	 * Reads a fixed huffman code for use by the {@code TYPE_FIXED_HUFFMAN}
	 * state. This method does not use a built huffman tree and instead
	 * determines the desired value to place depending on the used initial
	 * bits. The decoding is similar to UTF-8 in a way as the table of input
	 * values is mostly linear of a fixed size. Note that this will potentially
	 * return windows and stop values.
	 *
	 * @return The read value.
	 * @throws IOException On read errors.
	 * @since 2016/03/10
	 */
	private final int __readFixed()
		throws IOException
	{
		// The minimum set of fixed sized bits is 7, thus read the 7 bits first
		// It should be noted that the returned bits are on the RIGHT side as
		// those specified in the RFC 1951 (3.2.6).
		//   frm to  low       hi       
		//   --- --- --+++++++ --+++++++
		// A   0 143  00110000  10111111
		// B 144 255 110010000 111111111
		// C 256 279   0000000   0101111
		// D 280 287  11000000  11000111
		//   --- --- --+++++++ --+++++++
		//  L 84      10000100  10000100
		int bits = (int)in.readBits(7);
		boolean lka = true, lkb = true, lkc = true, lkd = true;
		
		// Never A?
		if (false)
			lka = false;
		
		// Never B?
		// It will never be B if the mask is < 0b10000
		if (bits < 0b0010000)
			lkb = false;
		
		// Never C?
		// If bit 7 is set
		// The mask is greater than the high C value
		// Bit 
		if ((0 != (bits & 0b1000000)) ||
			(bits > 0b0101111))
			lkc = false;
		
		// Never D?
		// If bits 3-6 are set then it will never be D
		// If bit 7 is not set
		if ((0 != (bits & 0b0111000)) ||
			(0 == (bits & 0b1000000)))
			lkd = false;
		
		System.err.printf("%s %s %s %s : %s%n", lka, lkb, lkc, lkd,
			Integer.toString(bits, 2));
		
		
		// It is possible that extra bits need to be read depending on the
		// value prefix. There also needs to be the primary return value number
		// base along with the bit base.
		int extrabits;
		int nbase;
		int bbase;
		
		if (true)
			throw new Error("TODO");
		// 7-bit
		if (bits >= 0b0000000 && bits <= 0b0010111)
		{
			extrabits = 0;
			nbase = 256;
			bbase = 0b0000000;
		}
		
		// The lower 8-bit range (output data)
		else if (bits >= 0b0011000 && bits <= 0b1011111)
		{
			extrabits = 1;
		}
		
		throw new Error("TODO");
	}
	
	/**
	 * This returns the potentially cached fixed huffman tree which is used
	 * as input for dynamic huffman trees and the fixed huffman tree for
	 * potentially small data sources.
	 *
	 * @since 2016/03/10
	 */
	private static final HuffmanTree<Integer> __fixedTree()
	{
		// Lock on the global tree
		synchronized (_GLOBAL_TREE_LOCK)
		{
			// Get reference
			Reference<HuffmanTree<Integer>> ref = _GLOBAL_TREE;
			HuffmanTree<Integer> rv = null;
			
			// Cached already?
			if (ref != null)
				rv = ref.get();
			
			// Needs creation?
			if (rv == null)
			{
				// Create a new one
				rv = new HuffmanTree<Integer>();
				
				// 0 - 143, 8 bits
				for (int i = 0; i <= 143; i++)
					rv.setLiteralRepresentation(0b00110000 + i, 0b11111111, i);
				
				// 144 - 255, 9 bits
				for (int i = 144, j = 0; i <= 255; i++, j++)
					rv.setLiteralRepresentation(0b110010000 + j, 0b111111111,
						i);
				
				// 256 - 279, 7 bits
				for (int i = 256, j = 0; i <= 279; i++, j++)
					rv.setLiteralRepresentation(0b0000000 + j, 0b1111111, i);
				
				// 280 - 287, 8 bits
				for (int i = 280, j = 0; i <= 287; i++, j++)
					rv.setLiteralRepresentation(0b11000000 + j, 0b11111111, i);
				
				// Cache it
				_GLOBAL_TREE = new WeakReference<>(rv);
			}
			
			// Return it
			return rv;
		}
	}
}

