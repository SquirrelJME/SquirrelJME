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

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import net.multiphasicapps.buffers.CircularBooleanBuffer;
import net.multiphasicapps.buffers.CircularByteBuffer;
import net.multiphasicapps.collections.HuffmanTree;

/**
 * This is a data processor which handles RFC 1951 deflate streams.
 *
 * @since 2016/03/11
 */
public class InflateDataProcessor
	extends DataProcessor
{
	/**
	 * Required non-finished bits in the queue, this is for optimal processing
	 * so that partial states are simpler.
	 */
	protected static final int REQUIRED_BITS =
		48;
	
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
	
	/** Input bits. */
	protected final CircularBooleanBuffer inputbits =
		new CircularBooleanBuffer();
	
	/** The sliding byte window. */
	protected final SlidingByteWindow window =
		new SlidingByteWindow(SLIDING_WINDOW_SIZE);
	
	/** The bit compactor for queing added bits. */
	protected final BitCompactor compactor =
		new BitCompactor(new BitCompactor.Callback()
			{
				/**
				 * {@inheritDoc}
				 * @since 2016/03/11
				 */
				@Override
				public void ready(byte __v)
				{
					// Give it to the output data
					output.offerLast(__v);
					
					// Also give it to the sliding window
					window.append(__v);
					
					// DEBUG
					System.err.printf("DEBUG -- W %02x %c%n", __v & 0xFF,
						(char)__v);
				}
			});
	
	/** Current decoding task. */
	private volatile __Task__ _task =
		__Task__.READ_HEADER;
	
	/** Was the final block hit? */
	private volatile boolean _finalhit;
	
	/** Nothing is left? */
	private volatile boolean _nothingleft;
	
	/** No compression length. */
	private volatile int _nocomplen;
	
	/** Dynamic number of literal length codes. */
	private volatile int _dhlit;
	
	/** Dynamic number of distance codes. */
	private volatile int _dhdist;
	
	/** Dynamic number of code length codes. */
	private volatile int _dhclen;
	
	/** Raw code lengths. */
	private volatile int[] _rawcodelens;
	
	/** The next code length to read. */
	private volatile int _readclnext;
	
	/** The code length huffman tree. */
	private volatile HuffmanTree<Integer> _clentree;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/11
	 */
	@Override
	protected void process()
		throws IOException, WaitingException
	{
		// Nothing left? Stop
		if (_nothingleft)
			return;
		
		// Take all bytes which are available to the input and add them to the
		// input bit buffer
		while (input.hasAvailable())
		{
			int v;
			inputbits.offerLastInt((v = ((int)input.removeFirst()) & 0xFF),
				0xFF);
			System.err.printf("DEBUG -- in %02x%n", v);
		}
		
		// Processing loop
		while (!_nothingleft)
		{
			// Require more available bytes if not finished
			if (!isFinished() && inputbits.available() < REQUIRED_BITS)
				throw new WaitingException("XI0e");
		
			// Perform work
			try
			{
				// Depends on the action
				switch (_task)
				{
						// Read the deflate header
					case READ_HEADER:
						if (!__readHeader())
							return;
						break;
						
						// No compression
					case NO_COMPRESSION:
						__readNoCompression();
						break;
						
						// Read fixed huffman table
					case FIXED_HUFFMAN:
						__readFixedHuffman();
						break;
						
						// Read dynamic huffman heder
					case DYNAMIC_HUFFMAN_HEAD:
						__readDynamicHuffmanHeader();
						break;
						
						// Read dynamic huffman alphabet (CLEN)
					case DYNAMIC_HUFFMAN_ALPHABET_CLEN:
						__readDynamicHuffmanAlphabetCLEN();
						break;
						
						// Read dynamic huffman alphabet (LIT)
					case DYNAMIC_HUFFMAN_ALPHABET_LIT:
						__readDynamicHuffmanAlphabetLIT();
						break;
						
						// Read dynamic huffman alphabet (DIST)
					case DYNAMIC_HUFFMAN_ALPHABET_DIST:
						__readDynamicHuffmanAlphabetDIST();
						break;
						
						// Read dynamic huffman compressed data
					case DYNAMIC_HUFFMAN_COMPRESSED:
						__readDynamicHuffmanCompressed();
						break;
						
						// Unknown
					default:
						throw new IOException(String.format("XI0f",
							_task.name()));
				}
			}
		
			// Short read
			catch (NoSuchElementException nsee)
			{
				throw new IOException("XI0g", nsee);
			}
		}
	}
	
	/**
	 * Calculates the value to shift into when using the dynamic huffman table
	 * where codes are written in this mixed format.
	 *
	 * @param __c The bit number currently being read, this is the read order
	 * as it appears in the data and not in the mixed order. Thus if the
	 * value is {@code 0} then shift 16 is used.
	 * @param __val Is the current bit set?
	 * @return The shift value to OR into.
	 * @throws IOException If the shift is out of range.
	 * @since 2016/03/13
	 */
	private int __alphaShift(int __c, boolean __val)
		throws IOException
	{
		// Check
		if (__c < 0 || __c >= 19)
			throw new InflaterException(String.format("XI0h %d", __c));
		
		// If not set, nothing is returned
		if (!__val)
			return 0;
		
		// Shift it
		return (1 << __alphaSwap(__c));
	}
	
	/**
	 * Code lengths are in swapped positions so that certain orders appear
	 * before other ones.
	 *
	 * @param __c The current index to write
	 * @return The actual position in the raw code length array to write to.
	 * @throws IOException If the read position is out of range.
	 * @since 2016/03/13
	 */
	private int __alphaSwap(int __c)
		throws IOException
	{
		// Check
		if (__c < 0 || __c >= 19)
			throw new InflaterException(String.format("XI0h %d", __c));
		
		// Depends on the input value
		switch (__c)
		{
				// The read order is shuffled a bit
			case 0: return (16);
			case 1: return (17);
			case 2: return (18);
			case 3: return (0);
			case 4: return (8);
			case 5: return (7); 
			case 6: return (9);
			case 7: return (6);
			case 8: return (10);
			case 9: return (5);
			case 10: return (11);
			case 11: return (4);
			case 12: return (12);
			case 13: return (3);
			case 14: return (13);
			case 15: return (2);
			case 16: return (14);
			case 17: return (1);
			case 18: return (15);
				
				// Unknown
			default:
				throw new InflaterException("WTFX");
		}
	}
	
	/**
	 * Handles a single huffman code.
	 *
	 * @param __c The input code.
	 * @throws IOException On read/write error.s
	 * @since 2016/03/12
	 */
	private void __handleCode(int __c)
		throws IOException
	{
		// Debug
		System.err.println("DEBUG -- c " + __c + " " + (char)__c);
		System.err.flush();
		
		// Literal byte value
		if (__c >= 0 && __c <= 255)
			compactor.add(__c & 0xFF, 0xFF);
		
		// Stop processing, use the waiting exception to break out of the state
		// and go back to the header handler
		else if (__c == 256)
		{
			System.err.println("DEBUG -- STOPPED");
			_task = __Task__.READ_HEADER;
			throw new WaitingException("XI0i");
		}
		
		// Window based result
		else if (__c >= 257 && __c <= 285)
		{
			// Read the distance
			int dist = __handleDistance(__c);
			System.err.printf("DEBUG -- Distance %d%n", dist);
			
			// Read the length
			int lent = __handleLength();
			System.err.printf("DEBUG -- Length %d%n", lent);
			
			// Create a byte array from the sliding window data
			byte[] winb = new byte[lent];
			try
			{
				window.get(dist, winb, 0, lent);
			}
			
			// Bad window read
			catch (IndexOutOfBoundsException ioobe)
			{
				throw new InflaterException(String.format(
					"XI0j %d %d", dist, lent), ioobe);
			}
			
			// Add those bytes to the output
			for (int i = 0; i < lent; i++)
				compactor.add(winb[i] & 0xFF, 0xFF);
		}
		
		// Error
		else
			throw new InflaterException(String.format("XI0k %d", __c));
	}
	
	/**
	 * Handles distance codes.
	 *
	 * @param __c The distance code.
	 * @return The distance to actually use
	 * @throws IOException On read/write errors.
	 * @since 2016/03/12
	 */
	private int __handleDistance(int __c)
		throws IOException
	{
		// If the code is 285 then the distance will be that
		if (__c == 285)
			return 285;
		
		// Get the base code
		int base = __c - 257;
		
		// Calculate the required distance to use
		int rv = 3;
		for (int i = 0; i < base; i++)
		{
			// Determine how many groups of 4 the code is away. Since zero
			// appears as items then subtract 1 to make it longer. However
			// after the first 8 it goes up in a standard pattern.
			rv += (1 << Math.max(0, (i / 4) - 1));
		}
		
		// Calculate the number of extra bits to read
		int extrabits = Math.max(0, (base / 4) - 1);
		
		// Read in those bits, if applicable
		if (extrabits > 0)
			rv += inputbits.removeFirstInt(extrabits, true);
		
		// Return the distance
		return rv;
	}
	
	/**
	 * Reads length codes from the input.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/03/12
	 */
	private int __handleLength()
		throws IOException
	{
		// Read 5 bits of length input
		int code = inputbits.removeFirstInt(5, true);
		
		// Error if above 29
		if (code > 29)
			throw new InflaterException(String.format("XI0l %d", code));
		
		// Calculate the required length to use
		int rv = 0;
		for (int i = 0; i < code; i++)
		{
			// This uses a similar pattern to the distance code, however the
			// division is half the size (so there are groups of 2 now).
			rv += (1 << Math.max(0, (i / 2) - 1));
		}
		
		// Determine the number of extra bits
		int extrabits = Math.max(0, (code / 2) - 1);
		
		// If there are bits to read then read them in
		if (extrabits > 0)
			rv += inputbits.removeFirstInt(extrabits, true);
		
		// Return it
		return rv;
	}
	
	/**
	 * Reads the code lengths for the code lengths alphabet.
	 *
	 * This is second (part A).
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/03/13
	 */
	private void __readDynamicHuffmanAlphabetCLEN()
		throws IOException
	{
		// Current code length
		int clen = _dhclen;
		
		// Need to allocate the array?
		int[] cll = _rawcodelens;
		if (cll == null)
			_rawcodelens = cll = new int[19];
		
		// Read code lengths
		for (;;)
		{
			// Next length to read
			int next = _readclnext;
			
			// Read them all?
			if (next == clen)
			{
				// Read the literal table
				_task = __Task__.DYNAMIC_HUFFMAN_ALPHABET_LIT;
				
				// DEBUG tree
				System.err.println("DEBUG -- Tree:");
				new __CodeLenTree__(3, 3, 3, 3, 3, 2, 4, 4);
				System.err.println("DEBUG -- END TREE");
				
				// Setup the huffman tree
				_clentree = new __CodeLenTree__(cll);
				
				// Not needed anymore
				_rawcodelens = null;
				
				// Done
				return;
			}
			
			// Not enough bits to read code lengths
			if (!isFinished() && inputbits.available() < 3)
				throw new WaitingException("XI0i");
			
			// Read three bits
			cll[__alphaSwap(next)] = inputbits.removeFirstInt(3);
			
			// Debug
			System.err.printf("DEBUG -- CLEN %d (%d) %d%n", next,
				__alphaSwap(next), cll[__alphaSwap(next)]);
			
			// Go to the next one
			_readclnext = (next + 1);
		}
	}
	
	/**
	 * Reads code lengths for the distance alphabet.
	 *
	 * This is second (part C).
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/03/13
	 */
	private void __readDynamicHuffmanAlphabetDIST()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Reads code lengths for the literal alphabet (0-255 values).
	 *
	 * This is second (part B).
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/03/13
	 */
	private void __readDynamicHuffmanAlphabetLIT()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Reads the dynamic huffman actually compressed data using the
	 * pre-existing dynamic data. This is done until the stop code is reached.
	 *
	 * This is third and last.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/03/13
	 */
	private void __readDynamicHuffmanCompressed()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Reads the dynamic huffman table header.
	 *
	 * This is the very start of the huffman data.
	 *
	 * This is first.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/03/13
	 */
	private void __readDynamicHuffmanHeader()
		throws IOException
	{
		// The header consists of 14 bits: HLIT (5), HDIST (5), HCLEN (4)
		if (!isFinished() && inputbits.available() < 14)
			throw new WaitingException("XI0i");
		
		// Read the bits
		int cll;
		_dhlit = inputbits.removeFirstInt(5) + 257;
		_dhdist = inputbits.removeFirstInt(5) + 1;
		_dhclen = cll = inputbits.removeFirstInt(4) + 4;
		
		// DEBUG
		System.err.printf("DEBUG -- DHH %d %d %d %n", _dhlit, _dhdist,
			_dhclen);
		
		// Code lengths cannot be higher than 19
		if (cll > 19)
			throw new InflaterException(String.format("XI0m %d", cll));
		
		// Initialize
		_rawcodelens = null;
		_clentree = null;
		_readclnext = 0;
		
		// Need to read the alphabet
		_task = __Task__.DYNAMIC_HUFFMAN_ALPHABET_CLEN;
	}					
	
	/**
	 * Reads the fixed huffman based input.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/03/11
	 */
	private void __readFixedHuffman()
		throws IOException
	{
		// If there are at least 9 available bits for input, read them.
		// Alternatively if this is the final block allow 7 because this
		// could be the final stop code.
		while (inputbits.available() >= 9 ||
			(_finalhit && inputbits.available() >= 7))
		{
			// Read single code
			int code = DeflateFixedHuffman.read(inputbits);
			
			// Handle the code
			__handleCode(code);
		}
	}
	
	/**
	 * Reads the deflate header.
	 *
	 * @return {@code true} if the calling loop should terminate.
	 * @throws IOException On read/write errors.
	 * @since 2016/03/11
	 */
	private boolean __readHeader()
		throws IOException
	{System.err.println("DEBUG -- ReadHeader");
		// If the final block was hit then just stop
		if (_finalhit)
		{
			_nothingleft = true;
			setWaiting(false);
			return false;
		}
		
		// Read final bit
		_finalhit |= inputbits.removeFirst();
		
		// Read type
		int type = inputbits.removeFirstInt(2);
		
		// Debug
		System.err.printf("DEBUG -- fn: %s%n", _finalhit);
		System.err.printf("DEBUG -- ty: %d%n", type);
		
		// Depends on the type to read
		switch (type)
		{
				// None
			case TYPE_NO_COMPRESSION:
				// Setup intial state
				_nocomplen = -1;
				
				// Enter task
				_task = __Task__.NO_COMPRESSION;
				break;
				
				// Fixed huffman
			case TYPE_FIXED_HUFFMAN:
				_task = __Task__.FIXED_HUFFMAN;
				break;
				
				// Dynamic huffman
			case TYPE_DYNAMIC_HUFFMAN:
				// Initialize state
				_dhlit = -1;
				_dhdist = -1;
				_dhclen = -1;
				
				// Start with the header
				_task = __Task__.DYNAMIC_HUFFMAN_HEAD;
				break;
			
				// Error or unknown
			case TYPE_ERROR:
			default:
				throw new InflaterException("XI0n");
		}
		
		// Continue
		return true;
	}
	
	/**
	 * Reads the no compression block.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/03/12
	 */
	private void __readNoCompression()
		throws IOException
	{
		// Get the current length
		int curlen = _nocomplen;
		
		// If not yet initialized, set it up
		if (curlen < 0)
		{
			// Need four bytes of input, along with potential alignment bits
			if (!isFinished() && inputbits.available() < 39)
				throw new WaitingException("XI0i");
			
			// Align to byte boundary
			while ((inputbits.headPosition() & 7) != 0)
			{
				System.err.printf("DEBUG -- hp %d%n",
					inputbits.headPosition());
				inputbits.removeFirst();
			}
			
			// Read length and the one's complement of it
			int len = inputbits.removeFirstInt(16);
			int com = inputbits.removeFirstInt(16);
			
			// The complemented length must be equal to the complement
			System.err.printf("DEBUG -- NL %04x %04x%n", len, com);
			if ((len ^ 0xFFFF) != com)
				throw new InflaterException(String.format("XI0o %x %x",
					len, com));
			
			// Set it
			_nocomplen = len;
		}
		
		// Otherwise read values
		else
			while (curlen > 0)
			{
				// Need at least a byte of input
				if (!isFinished() && inputbits.available() < 8)
					throw new WaitingException("XI0i");
				
				// Read byte
				int val = inputbits.removeFirstInt(8);
				
				// Add to output
				compactor.add(val, 0xFF);
				
				// Decrement
				curlen--;
				_nocomplen = curlen;
				
				// End of sequence
				if (curlen == 0)
				{
					_task = __Task__.READ_HEADER;
					return;
				}
			}
	}
	
	/**
	 * This is a very basic huffman tree for the input code length values
	 * which are needed when this is parsed.
	 *
	 * @since 2016/03/27
	 */
	private static class __CodeLenTree__
	{
		/**
		 * Initializes the code length tree.
		 *
		 * @param __lens Code lengths, this array is used directly and is not
		 * copied.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/27
		 */
		private __CodeLenTree__(int... __lens)
			throws NullPointerException
		{
			// Check
			if (__lens == null)
				throw new NullPointerException("NARG");
			
			// Input length
			int n = __lens.length;
			
			// Setup keys
			__Key__[] keys = new __Key__[n];
			for (int i = 0; i < n; i++)
				keys[i] = new __Key__(i, __lens[i]);
			
			// Sort the keys
			//Arrays.<__Key__>sort(keys, null);
			
			// The maximum bit count
			int maxbits = 0;
			for (int i = 0; i < n; i++)
				maxbits = Math.max(maxbits, keys[i].length);
			
			// The counts for all keys
			int[] bl_count = new int[n];
			for (int i = 0; i < n; i++)
				bl_count[keys[i].length]++;
			
			// Find the numerical value of the smallest code for each code
			// length:
			int code = 0;
			int[] next_code = new int[maxbits + 1];
			bl_count[0] = 0;
			for (int bits = 1; bits <= maxbits; bits++)
			{
				code = (code + bl_count[bits - 1]) << 1;
				next_code[bits] = code;
			}
			
			// Assign values to all codes
			int act_bits[] = new int[n];
			for (int q = 0; q < n; q++)
			{
				// Get length
				int len = keys[q].length;
				
				// Calculate
				if (len != 0)
					act_bits[q] = ((next_code[len])++);
			}
			
			for (int i = 0; i < n; i++)
			{
				System.err.printf("DEBUG -- B=%s H=%10s%n", keys[i],
					Integer.toBinaryString(act_bits[i]));
			}
			
			throw new Error("TODO");
		}
		
		/**
		 * This key contains a symbol and a value.
		 *
		 * @since 2016/03/28
		 */
		private static class __Key__
			implements Comparable<__Key__>
		{
			/** The symbol. */
			protected final int symbol;
			
			/** The length. */
			protected final int length;
			
			/**
			 * Initializes the key.
			 *
			 * @param __sym The symbol used.
			 * @param __len The length.
			 * @since 2016/03/28
			 */
			private __Key__(int __sym, int __len)
			{
				// Set
				symbol = __sym;
				length = __len;
			}
			
			/**
			 * {@inheritDoc}
			 * @since 2016/03/28
			 */
			@Override
			public int compareTo(__Key__ __o)
			{
				// Shorter lengths first
				int al = length;
				int bl = __o.length;
				if (al < bl)
					return -1;
				else if (al > bl)
					return 1;
				
				// Symbol scond
				int as = symbol;
				int bs = __o.symbol;
				if (as < bs)
					return -1;
				else if (as > bs)
					return 1;
				return 0;
			}
			
			/**
			 * {@inheritDoc}
			 * @since 2016/03/28
			 */
			@Override
			public String toString()
			{
				return "<" + symbol + "," + length + ">";
			}
		}
	}
	
	/**
	 * The current task to perform when decoding input code.
	 *
	 * @since 2016/03/11
	 */
	private static enum __Task__
	{
		/** Read the deflate header. */
		READ_HEADER,
		
		/** Read fixed huffman table. */
		FIXED_HUFFMAN,
		
		/** Read no compressed area. */
		NO_COMPRESSION,
		
		/** Read dynamic huffman header. */
		DYNAMIC_HUFFMAN_HEAD,
		
		/** Read dynamic huffman alphabet (code lengths). */
		DYNAMIC_HUFFMAN_ALPHABET_CLEN,
		
		/** Read dynamic huffman alphabet (literals). */
		DYNAMIC_HUFFMAN_ALPHABET_LIT,
		
		/** Read dynamic huffman alphabet (distances). */
		DYNAMIC_HUFFMAN_ALPHABET_DIST,
		
		/** Read dynamic huffman compressed data. */
		DYNAMIC_HUFFMAN_COMPRESSED,
		
		/** End. */
		;
	}
}

