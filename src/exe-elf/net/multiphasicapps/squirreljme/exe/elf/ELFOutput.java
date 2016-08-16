// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.elf;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.exe.ExecutableOutput;

/**
 * This is used to output ELF binaries.
 *
 * @since 2016/08/07
 */
public class ELFOutput
	implements ExecutableOutput
{
	/** The size of the entry table. */
	private static final int _ENTRY_SIZE =
		12;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** System properties to put for initial execution. */
	protected final Map<String, String> properties =
		new LinkedHashMap<>();
	
	/** Class visible lock. */
	final Object _lock =
		this.lock;
	
	/** The "padding" bytes in the elf identification. */
	final byte[] _padding =
		new byte[8];
	
	/** Programs within the ELF. */
	final List<ELFProgram> _programs =
		new ArrayList<>();
	
	/** Sections within the ELF. */
	final List<ELFSection> _sections =
		new ArrayList<>();
	
	/** The endianess used. */
	volatile JITCPUEndian _endianess;
	
	/** The word size of the CPU. */
	volatile int _wordsize =
		-1;
	
	/** The OS ABI. */
	volatile int _osabi =
		-1;
	
	/** The type of ELF used. */
	volatile ELFType _type;
	
	/** The used machine. */
	volatile int _machine =
		-1;
	
	/** Flags to use. */
	volatile int _flags;
	
	/** The ELF base address. */
	volatile long _baseaddr;
	
	/**
	 * Adds a system property to be included in the target binary.
	 *
	 * @param __k The the key.
	 * @param __v The value.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/24
	 */
	public void addSystemProperty(String __k, String __v)
		throws IOException, NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			Map<String, String> properties = this.properties;
			properties.put(__k, __v);
		}
	}
	
	/**
	 * Generates the actual binary.
	 *
	 * @param __os The stream to write to.
	 * @throws IllegalStateException If the output stream does not match an
	 * optionally previously specified primed stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	public void generate(OutputStream __os)
		throws IllegalStateException, IOException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Check some things
			__check();
			
			// Setup sequences
			__Sequences__ seq = new __Sequences__(this);
			
			// Setup output
			ExtendedDataOutputStream dos = new ExtendedDataOutputStream(__os);
			switch (this._endianess)
			{
					// Big endian
				case BIG:
					dos.setEndianess(DataEndianess.BIG);
					break;
				
					// Little endian
				case LITTLE:
					dos.setEndianess(DataEndianess.LITTLE);
					break;
					
					// Unknown
				default:
					throw new RuntimeException("OOPS");
			}
			
			// Write sequences
			for (__Sequences__.__Sequence__ s : seq)
			{
				// {@squirreljme.error AX0a Never specified the sequence start
				// position or its position has been exceeded.}
				int at = s._at;
				if (at < 0 || at > dos.size())
					throw new JITException("AX0a");
				
				// Pad to it
				while (dos.size() < at)
					dos.writeByte(0);
				
				// End position
				int end = at + s._size;
				
				// Write the sequence
				s.__write(dos);
				
				// {@squirreljme.error AX0b Not enough data was written in the
				// sequence. (The expected end point; The actual end point;
				// The class type of the last written thing)}
				long really;
				if (end != (really = dos.size()))
					throw new JITException(String.format("AX0b %d %d %s",
						end, really, s.getClass()));
			}
		}
	}
	
	/**
	 * Inserts the namespace into the specified output.
	 *
	 * @param __name The name of the namespace.
	 * @param __data The input namespace data.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	public ELFProgram insertNamespace(String __name, InputStream __data)
		throws IOException, NullPointerException
	{
		// Check
		if (__name == null || __data == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			List<ELFProgram> programs = this._programs;
			ELFProgram rv;
			programs.add((rv = new ELFProgram(this, __name, __data)));
			return rv;
		}
	}
	
	/**
	 * This primes the given stream for output for the given binary, generally
	 * this should always be called first with the specified output stream in
	 * the event the writer is capable of streamed output. If the writer does
	 * not need to write to the stream as soon as possible then this method
	 * can do nothing.
	 *
	 * @param __os The stream to prime.
	 * @throws IllegalStateException If the output has already been primed.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	public void primeOutput(OutputStream __os)
		throws IllegalStateException, IOException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
		}
	}
	
	/**
	 * Sets the endianess of the ELF.
	 *
	 * @param __e The ELF endianess.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	public void setEndianess(JITCPUEndian __e)
		throws NullPointerException
	{
		// Check
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._endianess = __e;
		}
	}
	
	/**
	 * Sets the flags to use in the ELF header.
	 *
	 * @param __f The flags to use.
	 * @since 2016/08/15
	 */
	public void setFlags(int __f)
	{
		// Lock
		synchronized (this.lock)
		{
			this._flags = __f;
		}
	}
	
	/**
	 * Sets the machine which is used.
	 *
	 * @param __m The machine identifier.
	 * @throws IllegalArgumentException If the input value is not within range.
	 * @since 2016/08/15
	 */
	public void setMachine(int __m)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AX04 The machine value is not within range.}
		if (__m < 0 || __m > 65535)
			throw new IllegalArgumentException("AX04");
		
		// Lock
		synchronized (this.lock)
		{
			this._machine = __m;
		}
	}
	
	/**
	 * Sets the OS ABI used in the ELF.
	 *
	 * @param __v The OS ABI to use.
	 * @throws IllegalArgumentException On null arguments.
	 * @since 2016/08/15
	 */
	public void setOSABI(int __v)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AX02 OS ABI is out of range.}
		if (__v < 0 || __v > 255)
			throw new IllegalArgumentException("AX02");
		
		// Lock
		synchronized (this.lock)
		{
			this._osabi = __v;
		}
	}
	
	/**
	 * Sets the padding to be used in the ELF identification header.
	 *
	 * @param __b The bytes to use for the padding.
	 * @throws IndexOutOfBoundsException If the input array exceeds 8 bytes.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	public void setPadding(byte... __b)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AX03 Padding is limited to 8 bytes.}
		int n = __b.length;
		if (n > 8)
			throw new IndexOutOfBoundsException("AX03");
		
		// Lock
		synchronized (this.lock)
		{
			// Copy and zero the rest
			byte[] dest = this._padding;
			for (int i = 0; i < 8; i++)
				if (i < n)
					dest[i] = __b[i];
				else
					dest[i] = 0;
		}
	}
	
	/**
	 * Sets the type of ELF that this is.
	 *
	 * @param __t The type of ELF to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	public void setType(ELFType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._type = __t;
		}
	}
	
	/**
	 * Sets the word size of the CPU.
	 *
	 * @param __w The word size to use.
	 * @throws IllegalArgumentException On null arguments.
	 * @since 2016/08/15
	 */
	public void setWordSize(int __w)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AX01 The CPU word size is not a power of two or
		// is not 32-bit or 64-bit.}
		if (__w <= 0 || Integer.bitCount(__w) != 1 || (__w != 32 && __w != 64))
			throw new IllegalArgumentException("AX01");
		
		// Lock
		synchronized (this.lock)
		{
			this._wordsize = __w;
		}
	}
	
	/**
	 * Checks that settings are properly set.
	 *
	 * @since 2016/08/16
	 */
	private void __check()
	{
		// {@squirreljme.error AX05 ELF endianess not set.}
		if (this._endianess == null)
			throw new JITException("AX05");
		
		// {@squirreljme.error AX06 ELF word size not set.}
		if (this._wordsize < 0)
			throw new JITException("AX06");
		
		// {@squirreljme.error AX07 ELF OS ABI is not set.}
		if (this._osabi < 0)
			throw new JITException("AX07");
		
		// {@squirreljme.error AX08 ELF type is not set.}
		if (this._type == null)
			throw new JITException("AX08");
		
		// {@squirreljme.error AX09 ELF machine is not set.}
		if (this._machine < 0)
			throw new JITException("AX09");
	}
}

