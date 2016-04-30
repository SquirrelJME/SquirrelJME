// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.inflate;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import net.multiphasicapps.buffers.CircularBooleanBuffer;
import net.multiphasicapps.buffers.CircularByteBuffer;
import net.multiphasicapps.collections.HuffmanTree;
import net.multiphasicapps.io.DataPipe;

/**
 * This is a data processor which handles RFC 1951 deflate streams.
 *
 * @since 2016/03/11
 */
public class InflateDataPipe
	extends DataPipe
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
	
	/** The next literal to read. */
	private volatile int _nexthlitdist;
	
	/** Next literal lengths. */
	private volatile int[] _rawlitdistlens;
	
	/** The literal code tree. */
	private volatile HuffmanTree<Integer> _treehlit;
	
	/** The distance code tree. */
	private volatile HuffmanTree<Integer> _treedist;
	
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
		final int READQ = 32;
		for (byte[] qq = new byte[READQ];;)
		{
			// Read in all bytes
			int rc = pipeInput(qq, 0, READQ);
			
			// For every byte, offer it to the output
			for (int i = 0; i < rc; i++)
				inputbits.offerLastInt((v = ((int)qq[i]) & 0xFF, 0xFF);
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
					case DYNAMIC_HUFFMAN_ALPHABET_LITDIST:
						__readDynamicHuffmanAlphabetLITDIST();
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
	 * @param __len Length tree.
	 * @param __dist Distance tree.
	 * @throws IOException On read/write error.s
	 * @since 2016/03/12
	 */
	private void __handleCode(int __c, HuffmanTree<Integer> __len,
		HuffmanTree<Integer> __dist)
		throws IOException
	{
		// Literal byte value
		if (__c >= 0 && __c <= 255)
			compactor.add(__c & 0xFF, 0xFF);
		
		// Stop processing, use the waiting exception to break out of the state
		// and go back to the header handler
		else if (__c == 256)
		{
			// Do not need the litreral and distance trees again
			_treehlit = null;
			_treedist = null;
			
			// Go back to reading the header
			_task = __Task__.READ_HEADER;
			throw new WaitingException("XI0i");
		}
		
		// Window based result
		else if (__c >= 257 && __c <= 285)
		{
			// Read the length
			int lent = __handleLength(__c);
			
			// Read the distance
			int dist = __handleDistance(__dist);
			
			// Get the maximum valid length, so for example if the length is 5
			// and the distance is two, then only read two bytes.
			int maxlen;
			if (dist - lent < 0)
				maxlen = dist;
			else
				maxlen = lent;
			
			// Create a byte array from the sliding window data
			byte[] winb = new byte[maxlen];
			try
			{
				window.get(dist, winb, 0, maxlen);
			}
			
			// Bad window read
			catch (IndexOutOfBoundsException ioobe)
			{
				throw new InflaterException(String.format(
					"XI0j %d %d", dist, lent), ioobe);
			}
			
			// Add those bytes to the output, handle wrapping around if the
			// length is greater than the current position
			for (int i = 0; i < lent; i++)
				compactor.add(winb[i % maxlen] & 0xFF, 0xFF);
		}
		
		// Error
		else
			throw new InflaterException(String.format("XI0k %d", __c));
	}
	
	/**
	 * Handles distance codes.
	 *
	 * @param __dist The distance codes.
	 * @return The distance to actually use
	 * @throws IOException On read/write errors.
	 * @since 2016/03/12
	 */
	private int __handleDistance(HuffmanTree<Integer> __dist)
		throws IOException
	{
		// If using fixed huffman read 5 bits since they are all the same
		// Otherwise for dynamic use the huffman tree symbol set
		int code = (__dist == null ? inputbits.removeFirstInt(5, true) :
			__readTreeCode(__dist));
		
		// Error if above 29
		if (code > 29)
			throw new InflaterException(String.format("XI0l %d", code));
		
		// Calculate the required distance to use
		int rv = 1;
		for (int i = 0; i < code; i++)
		{
			// This uses a similar pattern to the length code, however the
			// division is half the size (so there are groups of 2 now).
			rv += (1 << Math.max(0, (i / 2) - 1));
		}
		
		// Determine the number of extra bits
		int extrabits = Math.max(0, (code / 2) - 1);
		
		// If there are bits to read then read them in
		if (extrabits > 0)
			rv += inputbits.removeFirstInt(extrabits, false);
		
		// Return it
		return rv;
	}
	
	/**
	 * Reads length codes from the input.
	 *
	 * @param __c Input code value.
	 * @throws InflaterException If the code is an invalid length.
	 * @throws IOException On read/write errors.
	 * @since 2016/03/12
	 */
	private int __handleLength(int __c)
		throws InflaterException, IOException
	{
		// If the code is 285 then the length will be that
		if (__c == 285)
			return 285;
		
		// Get the base code
		int base = __c - 257;
		
		// Invalid?
		if (base < 0)
			throw new InflaterException(String.format("XI0l %d", __c));
		
		// Calculate the required length to use
		int rv = 3;
		for (int i = 0; i < base; i++)
		{
			// Determine how many groups of 4 the code is long. Since zero
			// appears as items then subtract 1 to make it longer. However
			// after the first 8 it goes up in a standard pattern.
			rv += (1 << Math.max(0, (i / 4) - 1));
		}
		
		// Calculate the number of extra bits to read
		int extrabits = Math.max(0, (base / 4) - 1);
		
		// Read in those bits, if applicable
		if (extrabits > 0)
			rv += inputbits.removeFirstInt(extrabits, false);
		
		// Return the length
		return rv;
	}
	
	/**
	 * Reads code bits using the given huffman tree and into the specified
	 * array.
	 *
	 * @param __codes The huffman tree which contains the length codes which
	 * the values being read are encoded with.
	 * @param __out The output array.
	 * @param __next The next value to read.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/28
	 */
	private int __readCodeBits(HuffmanTree<Integer> __codes, int[] __out,
		int __next)
		throws IOException, NullPointerException
	{
		// Check
		if (__codes == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Read in code based on an input huffman tree
		int basenext = __next;
		int code = __readTreeCode(__codes);
		
		// Literal length, the input is used
		if (code >= 0 && code < 16)
			__out[__next++] = code;
		
		// Repeating
		else
		{
			// Repeat this value and for this many lengths
			int repval;
			int repfor;
			
			// Repeat the previous length 3-6 times
			if (code == 16)
			{
				// Make sure there is a last length
				int lastlendx = __next - 1;	
				if (lastlendx < 0)
					throw new InflaterException(String.format("XI0x %d",
						lastlendx));
				
				// Read the last
				repval = __out[lastlendx];
				
				// Read the repeat count
				repfor = 3 + inputbits.removeFirstInt(2);
			}
			
			// Repeat zero for 3-10 times
			else if (code == 17)
			{
				// Use zero
				repval = 0;
				
				// Read 3 bits
				repfor = 3 + inputbits.removeFirstInt(3);
			}
			
			// Repeat zero for 11-138 times
			else if (code == 18)
			{
				// Use zero
				repval = 0;
				
				// Read 7 bits
				repfor = 11 + inputbits.removeFirstInt(7);
			}
			
			// Illegal code
			else
				throw new InflaterException(String.format("XI0w %d", code));
			
			// Could fail
			try
			{
				// Place in repeated values
				for (int i = 0; i < repfor; i++)
					__out[__next++] = repval;
			}
			
			// Out of bounds entry
			catch (IndexOutOfBoundsException ioobe)
			{
				throw new InflaterException("XI0y", ioobe);
			}
		}
		
		// Skip count
		return __next - basenext;
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
			if (next >= clen)
			{
				// Read the literal table
				_task = __Task__.DYNAMIC_HUFFMAN_ALPHABET_LITDIST;
				
				// Load into tree
				_clentree = __thunkCodeLengthTree(cll, 0, cll.length);
				
				// Not needed anymore
				_rawcodelens = null;
				
				// Setup for read
				_nexthlitdist = 0;
				_rawlitdistlens = null;
				_treehlit = null;
				_treedist = null;
				
				// Done
				return;
			}
			
			// Not enough bits to read code lengths
			if (!isFinished() && inputbits.available() < 3)
				throw new WaitingException("XI0i");
			
			// Read three bits
			cll[__alphaSwap(next)] = inputbits.removeFirstInt(3);
			
			// Go to the next one
			_readclnext = (next + 1);
		}
	}
	
	/**
	 * Reads code lengths for the literal alphabet (0-255 values) and the
	 * distance codes.
	 *
	 * This is second (part B).
	 *
	 * It should be noted that once the values are decoded, the read values
	 * are turned into a huffman tree the same as the code lengths. However
	 * the encoding of the values here are a bit more complex because they
	 * are compressed with the codelengths themselves.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2016/03/13
	 */
	private void __readDynamicHuffmanAlphabetLITDIST()
		throws IOException
	{
		// Get the tree and the max bit count used
		HuffmanTree<Integer> htree = _clentree;
		int maxbits = htree.maximumBits();
		int hlit = _dhlit;
		int hdist = _dhdist;
		int total = hlit + hdist;
		
		// Get raw literal lengths
		int[] rawints = _rawlitdistlens;
		if (rawints == null)
			_rawlitdistlens = rawints = new int[total];
		
		// Read loop
		try
		{
			for (;;)
			{
				// Get the next literal to read
				int next = _nexthlitdist;
				
				// Read them all?
				if (next >= total)
				{
					// No longer needed
					_clentree = null;
					
					// Generate trees
					_treehlit = __thunkCodeLengthTree(rawints, 0, hlit);
					_treedist = __thunkCodeLengthTree(rawints, hlit, hdist);
					
					// Read the compressed data now
					_task = __Task__.DYNAMIC_HUFFMAN_COMPRESSED;
					
					// Use a waiting exception to break from the loop
					throw new WaitingException("XI0i");
				}
				
				// Not enough bits to read code lengths?
				// Add 7 due to the repeat zero many times symbol
				if (!isFinished() && inputbits.available() < maxbits + 7)
					throw new WaitingException("XI0i");
				
				// Read in code
				int cbskip = __readCodeBits(htree, rawints, next);
				
				// Increase read
				_nexthlitdist = next + cbskip;
			}
		}
		
		// Out of bits or a bad tree
		catch (NoSuchElementException nsee)
		{
			throw new IOException("XI0v", nsee);
		}
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
		// Get both trees
		HuffmanTree<Integer> thlit = _treehlit;
		HuffmanTree<Integer> tdist = _treedist;
		
		// Maximum number of btis to read
		int maxbits = Math.max(thlit.maximumBits(), tdist.maximumBits());
		
		// Need to be able to read a value from the tree along with any
		// extra distance and length codes it may have
		while (isFinished() || inputbits.available() >= maxbits + 32)
		{
			// Decode literal code
			int code = __readTreeCode(thlit);
			
			// Handle it
			__handleCode(code, thlit, tdist);
		}
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
		
		// Code lengths cannot be higher than 19
		if (cll > 19)
			throw new InflaterException(String.format("XI0m %d", cll));
		
		// Initialize
		_rawcodelens = null;
		_clentree = null;
		_readclnext = 0;
		_nexthlitdist = 0;
		_rawlitdistlens = null;
		_treehlit = null;
		_treedist = null;
		
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
		// Require up to 32 bits because of the input along with extra distance
		// codes, length codes, and more
		while (isFinished() || inputbits.available() >= 32)
		{
			// Read single code
			int code = DeflateFixedHuffman.read(inputbits);
			
			// Handle the code
			__handleCode(code, null, null);
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
	{
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
				inputbits.removeFirst();
			}
			
			// Read length and the one's complement of it
			int len = inputbits.removeFirstInt(16);
			int com = inputbits.removeFirstInt(16);
			
			// The complemented length must be equal to the complement
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
	 * Reads in a code using the given huffman code.
	 *
	 * @param __codes The input tree.
	 * @return The read symbol.
	 * @throws IOException On read errors.
	 * @throws NoSuchElementException If the input ran out of available bits
	 * or the tree contains an unfinished structure.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/28
	 */
	private int __readTreeCode(HuffmanTree<Integer> __codes)
		throws NullPointerException, NoSuchElementException,
			NullPointerException
	{
		// Check
		if (__codes == null)
			throw new NullPointerException("NARG");
		
		// Start traversal in the tree
		HuffmanTree.Traverser<Integer> trav = __codes.traverser();
		for (;;)
		{
			// Is a value reached?
			if (trav.hasValue())
				return trav.getValue();
			
			// Read in a bit which designates the side to move down on the
			// tree
			int side = inputbits.removeFirstInt(1);
			
			// Traverse that side
			trav.traverse(side);
		}
	}
	
	/**
	 * Creates a huffman tree from the given code lengths. These generate
	 * symbols which are used to determine how the dynamic huffman data is to
	 * be decoded.
	 *
	 * @param __lens The input code lengths.
	 * @param __o The starting offset.
	 * @param __len The number of lengths to decode.
	 * @return A huffman tree from the code length input.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/28
	 */
	private HuffmanTree<Integer> __thunkCodeLengthTree(int[] __lens, int __o,
		int __l)
		throws NullPointerException
	{
		// Check
		if (__lens == null)
			throw new NullPointerException("NARG");
		
		// Setup target tree
		HuffmanTree<Integer> rv = new HuffmanTree<>();
		
		// The number of lengths
		int n = __l;
		
		// Obtain the number of bits that are available in all of the input
		// lengths
		int maxbits = 0;
		for (int i = 0; i < n; i++)
			maxbits = Math.max(maxbits, __lens[__o + i]);
		
		// Determine the bitlength count for all of the inputs
		int[] bl_count = new int[n];
		for (int i = 0; i < n; i++)
			bl_count[__lens[__o + i]]++;
		
		// Find the numerical value of the smallest code for each code
		// length.
		int code = 0;
		int[] next_code = new int[maxbits + 1];
		bl_count[0] = 0;
		for (int bits = 1; bits <= maxbits; bits++)
		{
			code = (code + bl_count[bits - 1]) << 1;
			next_code[bits] = code;
		}
	
		// Assign values to all codes
		for (int q = 0; q < n; q++)
		{
			// Get length
			int len = __lens[__o + q];
		
			// Calculate
			if (len != 0)
			{
				// The bits to use
				int use = ((next_code[len])++);
				
				// Add to the output table
				rv.add(q, use, (1 << len) - 1);
			}
		}
		
		// Return it
		return rv;
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
		
		/** Read dynamic huffman alphabet (literals and distances). */
		DYNAMIC_HUFFMAN_ALPHABET_LITDIST,
		
		/** Read dynamic huffman compressed data. */
		DYNAMIC_HUFFMAN_COMPRESSED,
		
		/** End. */
		;
	}
}

