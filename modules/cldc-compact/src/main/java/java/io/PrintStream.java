// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.io.CodecFactory;
import cc.squirreljme.runtime.cldc.io.Encoder;
import cc.squirreljme.runtime.cldc.lang.LineEndingUtils;
import java.util.Formatter;

/**
 * This class is used to print translated and formatted text.
 *
 * No {@link IOException} is ever thrown by any of these methods, they are
 * handled and provided as an error flag which can be obtained. If
 * {@link InterruptedIOException} is thrown then the error state is not set,
 * any operations which cause this to occur will instead call
 * {@code Thread.currentThread().interrupt()}.
 *
 * Print streams may optionally have automatic flushing which will call
 * {@link #flush()} whenever a byte array is written or when it is detected
 * that {@code '\n'} is written. Internally the output is bufferred to optimize
 * for writing multiple characters at once rather than one at a time.
 *
 * If not specified, the current system encoding is used.
 *
 * Any characters which are written to the output will be encoded accordingly.
 *
 * @since 2018/09/16
 */
@Api
public class PrintStream
	extends OutputStream
	implements Appendable, Closeable
{
	/**
	 * This buffer size for this class has been chosen to be small, since in
	 * most cases this class will either not be used or will be outputting just
	 * text to the console. So since most consoles and most text will likely
	 * be 80 columns or less, this buffer size is enough to fit such a terminal
	 * but also give some extra room in the event of overflow.
	 */
	private static final int _BUFFER_SIZE =
		96;
	
	/** If the buffer gets too big we have to drop bytes. */
	private static final int _EMERGENCY_HALT =
		95;
	
	/** The maximum number of byte that might be encoded at once. */
	private static final int _MAX_ENCODE_BYTES =
		8;
	
	/** Threshold before a forced flush. */
	private static final int _THRESHOLD =
		88;
	
	/** The stream to write bytes to. */
	private final OutputStream _out;
	
	/** Is auto-flushing to be used? */
	private final boolean _autoflush;
	
	/** The encoder used to encode chars to bytes. */
	private final Encoder _encoder;
	
	/** Mini-byte buffer for encoded characters. */
	private final byte[] _minienc =
		new byte[PrintStream._MAX_ENCODE_BYTES];
	
	/** The internal buffer. */
	private final byte[] _buf =
		new byte[PrintStream._BUFFER_SIZE];
	
	/** The position the buffer is at. */
	private int _bat;
	
	/** Error state? */
	private boolean _inerror;
	
	/**
	 * Writes to the given stream using the default encoding and with no
	 * auto flushing.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/17
	 */
	@Api
	public PrintStream(OutputStream __out)
		throws NullPointerException
	{
		this(__out, false, CodecFactory.defaultEncoder());
	}
	
	/**
	 * Writes to the given stream using the default encoding and with the
	 * specified auto flushing.
	 *
	 * @param __out The stream to write to.
	 * @param __autoflush If auto flushing is to be enabled.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/17
	 */
	@Api
	public PrintStream(OutputStream __out, boolean __autoflush)
		throws NullPointerException
	{
		this(__out, __autoflush, CodecFactory.defaultEncoder());
	}
	
	/**
	 * Writes to the given stream using the given encoding and with the
	 * specified auto flushing.
	 *
	 * @param __out The stream to write to.
	 * @param __autoflush If auto flushing is to be enabled.
	 * @param __enc The encoding to use.
	 * @throws NullPointerException On null arguments.
	 * @throws UnsupportedEncodingException If the encoding is not supported.
	 * @since 2018/09/17
	 */
	@Api
	public PrintStream(OutputStream __out, boolean __autoflush, String __enc)
		throws NullPointerException, UnsupportedEncodingException
	{
		this(__out, __autoflush, CodecFactory.encoder(__enc));
	}
	
	/**
	 * Writes to the given stream using the given encoder and with the
	 * specified auto flushing.
	 *
	 * @param __out The stream to write to.
	 * @param __autoflush If auto flushing is to be enabled.
	 * @param __enc The encoder to use to encode characters to bytes.
	 * @throws NullPointerException On null arguments.
	 * @throws UnsupportedEncodingException If the encoding is not supported.
	 * @since 2018/09/17
	 */
	private PrintStream(OutputStream __out, boolean __autoflush, Encoder __enc)
		throws NullPointerException
	{
		if (__out == null || __enc == null)
			throw new NullPointerException("NARG");
		
		this._out = __out;
		this._autoflush = __autoflush;
		this._encoder = __enc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/21
	 */
	@Override
	public PrintStream append(CharSequence __c)
	{
		this.print((__c == null ? "null" : __c.toString()));
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/21
	 */
	@Override
	public PrintStream append(CharSequence __c, int __s, int __e)
	{
		this.print((__c == null ? "null" :
			__c.subSequence(__s, __e).toString()));
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/21
	 */
	@Override
	public PrintStream append(char __c)
	{
		this.print(__c);
		return this;
	}
	
	/**
	 * Flushes the stream and checks the error state.
	 *
	 * @return The current error state.
	 * @since 2019/06/21
	 */
	@Api
	public boolean checkError()
	{
		synchronized (this)
		{
			this.__flush();
			return this._inerror;
		}
	}
	
	/**
	 * Clears the error state.
	 *
	 * @since 2019/06/21
	 */
	@Api
	protected void clearError()
	{
		synchronized (this)
		{
			this._inerror = false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/21
	 */
	@Api
	@Override
	public void close()
	{
		synchronized (this)
		{
			// Flush output
			this.__flush();
			
			// Close the stream
			try
			{
				this._out.close();
			}
			
			// Set error state?
			catch (IOException e)
			{
				this._inerror = true;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/21
	 */
	@Api
	@Override
	public void flush()
	{
		synchronized (this)
		{
			this.__flush();
		}
	}
	
	/**
	 * Writes formatted text to the print stream, using the default locale.
	 *
	 * @param __fmt The format specifiers.
	 * @param __args The arguments.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the string contains illegal
	 * format specifiers.
	 * @throws NullPointerException If no format was specified.
	 * @since 2018/09/23
	 */
	@Api
	public PrintStream format(String __fmt, Object... __args)
		throws IllegalArgumentException, NullPointerException
	{
		return this.__printf(__fmt, __args);
	}
	
	/**
	 * Prints the given value.
	 *
	 * @param __v The value to print.
	 * @since 2019/06/21
	 */
	@Api
	public void print(boolean __v)
	{
		synchronized (this)
		{
			this.__print((__v ? "true" : "false"));
		}
	}
	
	/**
	 * Prints the specified character to the stream.
	 *
	 * @param __v The character to print.
	 * @since 2018/09/23
	 */
	@Api
	public void print(char __v)
	{
		synchronized (this)
		{
			this.__writeChar(__v);
		}
	}
	
	/**
	 * Prints the specified integer to the stream.
	 *
	 * @param __v The value to print.
	 * @since 2018/11/04
	 */
	@Api
	public void print(int __v)
	{
		synchronized (this)
		{
			this.__print(Integer.toString(__v));
		}
	}
	
	/**
	 * Prints the given value.
	 *
	 * @param __v The value to print.
	 * @since 2019/06/21
	 */
	@Api
	public void print(long __v)
	{
		synchronized (this)
		{
			this.__print(Long.toString(__v));
		}
	}
	
	/**
	 * Prints the given value.
	 *
	 * @param __v The value to print.
	 * @since 2019/06/21
	 */
	@Api
	public void print(float __v)
	{
		synchronized (this)
		{
			this.__print(Float.toString(__v));
		}
	}
	
	/**
	 * Prints the given value.
	 *
	 * @param __v The value to print.
	 * @since 2019/06/21
	 */
	@Api
	public void print(double __v)
	{
		synchronized (this)
		{
			this.__print(Double.toString(__v));
		}
	}
	
	/**
	 * Prints the given value.
	 *
	 * @param __v The value to print.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/21
	 */
	@Api
	public void print(char[] __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			for (int i = 0, n = __v.length; i < n; i++)
				this.__writeChar(__v[i]);
		}
	}
	
	/**
	 * Prints the specified string.
	 *
	 * @param __v The string to print, if {@code null} then {@code "null"} is
	 * printed.
	 * @since 2018/09/20
	 */
	@Api
	public void print(String __v)
	{
		synchronized (this)
		{
			this.__print(__v);
		}
	}
	
	/**
	 * Prints the given value.
	 *
	 * @param __v The value to print.
	 * @since 2019/06/21
	 */
	@Api
	public void print(Object __v)
	{
		synchronized (this)
		{
			this.__print((__v == null ? "null" : __v.toString()));
		}
	}
	
	/**
	 * Writes formatted text to the print stream, using the default locale.
	 *
	 * @param __fmt The format specifiers.
	 * @param __args The arguments.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the string contains illegal
	 * format specifiers.
	 * @throws NullPointerException If no format was specified.
	 * @since 2018/09/23
	 */
	@Api
	public PrintStream printf(String __fmt, Object... __args)
	{
		return this.__printf(__fmt, __args);
	}
	
	/**
	 * Prints the end of line sequence that is used for the current platform.
	 *
	 * @since 2018/09/21
	 */
	@Api
	public void println()
	{
		synchronized (this)
		{
			this.__println();
		}
	}
	
	/**
	 * Prints the given value and ends the line.
	 *
	 * @param __v The value to print.
	 * @since 2019/06/21
	 */
	@Api
	public void println(boolean __v)
	{
		synchronized (this)
		{
			this.__print((__v ? "true" : "false"));
			this.__println();
		}
	}
	
	/**
	 * Prints the given value and ends the line.
	 *
	 * @param __v The value to print.
	 * @since 2019/06/21
	 */
	@Api
	public void println(char __v)
	{
		synchronized (this)
		{
			this.__writeChar(__v);
			this.__println();
		}
	}
	
	/**
	 * Prints the given value and ends the line.
	 *
	 * @param __v The value to print.
	 * @since 2019/06/21
	 */
	@Api
	public void println(int __v)
	{
		synchronized (this)
		{
			this.__print(Integer.toString(__v));
			this.__println();
		}
	}
	
	/**
	 * Prints the given value and ends the line.
	 *
	 * @param __v The value to print.
	 * @since 2019/06/21
	 */
	@Api
	public void println(long __v)
	{
		synchronized (this)
		{
			this.__print(Long.toString(__v));
			this.__println();
		}
	}
	
	/**
	 * Prints the given value and ends the line.
	 *
	 * @param __v The value to print.
	 * @since 2019/06/21
	 */
	@Api
	public void println(float __v)
	{
		synchronized (this)
		{
			this.__print(Float.toString(__v));
			this.__println();
		}
	}
	
	/**
	 * Prints the given value and ends the line.
	 *
	 * @param __v The value to print.
	 * @since 2019/06/21
	 */
	@Api
	public void println(double __v)
	{
		synchronized (this)
		{
			this.__print(Double.toString(__v));
			this.__println();
		}
	}
	
	/**
	 * Prints the given value and ends the line.
	 *
	 * @param __v The value to print.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/21
	 */
	@Api
	public void println(char[] __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			for (int i = 0, n = __v.length; i < n; i++)
				this.__writeChar(__v[i]);
			this.__println();
		}
	}
	
	/**
	 * Prints the given string to the output stream followed by a new line.
	 *
	 * @param __v The string to write.
	 * @since 2018/09/18
	 */
	@Api
	public void println(String __v)
	{
		synchronized (this)
		{
			this.__print(__v);
			this.__println();
		}
	}
	
	/**
	 * Prints the given object to the output stream followed by a new line.
	 *
	 * @param __v The string to write.
	 * @since 2018/11/20
	 */
	@Api
	public void println(Object __v)
	{
		synchronized (this)
		{
			this.__print((__v == null ? "null" : __v.toString()));
			this.__println();
		}
	}
	
	/**
	 * Sets the error state to on.
	 *
	 * @since 2019/06/21
	 */
	@Api
	protected void setError()
	{
		this._inerror = true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/21
	 */
	@Api
	@Override
	@ImplementationNote("If newline is written, this will flush.")
	public void write(int __c)
	{
		// Is always in array form
		byte[] b = new byte[]{(byte)__c};
		
		// Forward
		synchronized (this)
		{
			this.__writeBytes(b, 0, 1);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/21
	 */
	@Api
	@Override
	@ImplementationNote("If auto-flushing, this calls flush after writing.")
	public void write(byte[] __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			this.__writeBytes(__b, 0, __b.length);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/21
	 */
	@Api
	@Override
	@ImplementationNote("If auto-flushing, this calls flush after writing.")
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		synchronized (this)
		{
			this.__writeBytes(__b, __o, __l);
		}
	}
	
	/**
	 * Flushes the stream to the output.
	 *
	 * @since 2018/09/21
	 */
	private void __flush()
	{
		// Nothing to be written at all?
		int bat = this._bat;
		if (bat <= 0)
			return;
		
		// Write individual bytes and detect any exceptions
		OutputStream out = this._out;
		boolean oopsie = false;
		byte[] buf = this._buf;
		int bop = 0;
		for (; bop < bat; bop++)
			try
			{
				out.write(buf[bop]);
			}
			catch (InterruptedIOException e)
			{
				// Just stop handling here and interrupt the thread
				Thread.currentThread().interrupt();
				break;
			}
			catch (IOException e)
			{
				oopsie = true;
				break;
			}
		
		// Could not flush to the output
		if (oopsie)
		{
			// Bring down the characters in the buffer so that they are not
			// just lost
			for (int i = bop, o = 0; i < bat; i++, o++)
				buf[o] = buf[i];
			this._bat = bat - bop;
			
			// Set error state
			this._inerror = true;
		}
		
		// Is perfectly fine, so "clear" the buffer
		else
			this._bat = 0;
	}
	
	/**
	 * Prints the specified string.
	 *
	 * @param __s The string to print, if {@code null} then {@code "null"} is
	 * printed.
	 * @since 2018/09/20
	 */
	private void __print(String __s)
	{
		synchronized (this)
		{
			// Print null explicitly
			if (__s == null)
				__s = "null";
			
			for (int i = 0, n = __s.length(); i < n; i++)
				this.__writeChar(__s.charAt(i));
		}
	}
	
	/**
	 * Writes formatted text to the print stream, using the default locale.
	 *
	 * @param __fmt The format specifiers.
	 * @param __args The arguments.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the string contains illegal
	 * format specifiers.
	 * @throws NullPointerException If no format was specified.
	 * @since 2018/09/23
	 */
	private PrintStream __printf(String __fmt, Object... __args)
		throws IllegalArgumentException, NullPointerException
	{
		if (__fmt == null)
			throw new NullPointerException("NARG");
		
		// Generate formatter and write to ourselves
		new Formatter(this).format(__fmt, __args);
		
		return this;
	}
	
	/**
	 * Prints the end of line sequence that is used for the current platform.
	 *
	 * @since 2018/09/21
	 */
	private void __println()
	{
		synchronized (this)
		{
			// Write end of line sequence
			int lineType = RuntimeShelf.lineEnding();
			for (int i = 0;; i++)
			{
				char c = LineEndingUtils.toChar(lineType, i);
				if (c == 0)
					break;
				
				this.__writeChar(c);
			}
			
			// Flush the stream after every line printed, in the event the
			// system does not use a UNIX newline
			if (this._autoflush)
				this.flush();
		}
	}
	
	/**
	 * Writes multiple bytes to the output.
	 *
	 * @param __b The bytes to write.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/21
	 */
	private void __writeBytes(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Our current buffer state
		byte[] buf = this._buf;
		int bat = this._bat;
		
		// Auto-flush on any newlines?
		boolean autoflush = this._autoflush;
		
		// Copy bytes into the buffer
		boolean flush = false;
		for (int i = 0; i < __l; i++)
		{
			byte b = __b[__o + i];
			
			// Fill into buffer as long as we can actually fit bytes here,
			// if this case ever happens we just for the most part drop the
			// bytes since there is no room to store them anymore
			if (bat < PrintStream._EMERGENCY_HALT)
				buf[bat++] = b;
			
			// Auto-flushing on newline?
			if (autoflush && b == '\n')
				flush = true;
			
			// Force a flush?
			if (bat >= PrintStream._THRESHOLD)
			{
				// Store at location, flush then reload it
				this._bat = bat;
				this.__flush();
				bat = this._bat;
				
				// Clear the flush flag as we already flushed once
				flush = false;
			}
		}
		
		// Store changes
		this._bat = bat;
		
		// Perform a flush?
		if (flush || bat >= PrintStream._THRESHOLD)
			this.__flush();
	}
	
	/**
	 * Writes a single character to the output, encoding it as required.
	 *
	 * @param __c The character to write.
	 * @since 2018/09/19
	 */
	private void __writeChar(char __c)
	{
		// Encode bytes into the array
		byte[] encBytes = this._minienc;
		int wc = this._encoder.encode(__c, encBytes, 0, encBytes.length);
		
		/* {@squirreljme.error ZZ0q Did not expect the buffer to be out of
		room or be too small.} */
		if (wc < 0 || wc > encBytes.length)
			throw new Error("ZZ0q");
		
		// Write them into the buffer
		this.__writeBytes(encBytes, 0, wc);
	}
}

