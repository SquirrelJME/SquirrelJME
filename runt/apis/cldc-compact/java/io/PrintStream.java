// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.io.CodecFactory;
import cc.squirreljme.runtime.cldc.io.Encoder;
import java.util.Formatter;

/**
 * This class is used to print translated and formatted text.
 *
 * No {@link IOException} is ever thrown by any of these methods, they are
 * handled and provided as an error flag which can be obtained.
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
	
	/** Threshold before a forced flush. */
	private static final int _THRESHOLD =
		90;
	
	/** The newline sequence. */
	private static final String _NEWLINE;
	
	/** The stream to write bytes to. */
	private final OutputStream _out;
	
	/** Is auto-flushing to be used? */
	private final boolean _autoflush;
	
	/** The encoder used to encode chars to bytes. */
	private final Encoder _encoder;
	
	/** The internal buffer. */
	private final byte[] _buf =
		new byte[_BUFFER_SIZE];
	
	/** The position the buffer is at. */
	private int _bat;
	
	/** Error state? */
	private boolean _inerror;
	
	/**
	 * Cache the line separator which is derived from the system properties.
	 *
	 * @since 2018/09/18
	 */
	static
	{
		String nl;
		try
		{
			nl = System.getProperty("line.separator");
		}
		catch (SecurityException e)
		{
			nl = "\n";
		}
		
		_NEWLINE = nl;
	}
	
	/**
	 * Writes to the given stream using the default encoding and with no
	 * auto flushing.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/17
	 */
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
	
	public PrintStream append(CharSequence __a)
	{
		throw new todo.TODO();
	}
	
	public PrintStream append(CharSequence __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public PrintStream append(char __a)
	{
		throw new todo.TODO();
	}
	
	public boolean checkError()
	{
		throw new todo.TODO();
	}
	
	protected void clearError()
	{
		throw new todo.TODO();
	}
	
	@Override
	public void close()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/21
	 */
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
	public PrintStream format(String __fmt, Object... __args)
		throws IllegalArgumentException, NullPointerException
	{
		return this.__printf(__fmt, __args);
	}
	
	public void print(boolean __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Prints the specified character to the stream.
	 *
	 * @param __c The character to print.
	 * @since 2018/09/23
	 */
	public void print(char __c)
	{
		synchronized (this)
		{
			this.__writeChar(__c);
		}
	}
	
	public void print(int __a)
	{
		throw new todo.TODO();
	}
	
	public void print(long __a)
	{
		throw new todo.TODO();
	}
	
	public void print(float __a)
	{
		throw new todo.TODO();
	}
	
	public void print(double __a)
	{
		throw new todo.TODO();
	}
	
	public void print(char[] __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Prints the specified string.
	 *
	 * @param __s The string to print, if {@code null} then {@code "null"} is
	 * printed.
	 * @since 2018/09/20
	 */
	public void print(String __s)
	{
		synchronized (this)
		{
			this.__print(__s);
		}
	}
	
	public void print(Object __a)
	{
		throw new todo.TODO();
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
	public PrintStream printf(String __fmt, Object... __args)
	{
		return this.__printf(__fmt, __args);
	}
	
	/**
	 * Prints the end of line sequence that is used for the current platform.
	 *
	 * @return The end of line sequence.
	 * @since 2018/09/21
	 */
	public void println()
	{
		synchronized (this)
		{
			this.__println();
		}
	}
	
	public void println(boolean __a)
	{
		throw new todo.TODO();
	}
	
	public void println(char __a)
	{
		throw new todo.TODO();
	}
	
	public void println(int __a)
	{
		throw new todo.TODO();
	}
	
	public void println(long __a)
	{
		throw new todo.TODO();
	}
	
	public void println(float __a)
	{
		throw new todo.TODO();
	}
	
	public void println(double __a)
	{
		throw new todo.TODO();
	}
	
	public void println(char[] __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Prints the given string to the output stream followed by a new line.
	 *
	 * @param __v The string to write.
	 * @since 2018/09/18
	 */
	public void println(String __v)
	{
		synchronized (this)
		{
			this.__print(__v);
			this.__println();
		}
	}
	
	public void println(Object __a)
	{
		throw new todo.TODO();
	}
	
	protected void setError()
	{
		throw new todo.TODO();
	}
	
	@Override
	@ImplementationNote("If newline is written, this will flush.")
	public void write(int __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	@ImplementationNote("If auto-flushing, this calls flush after writing.")
	public void write(byte[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Flushes the stream to the output.
	 *
	 * @since 2018/09/21
	 */
	private final void __flush()
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
		for (bop = 0; bop < bat; bop++)
			try
			{
				out.write(buf[bop]);
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
	private final void __print(String __s)
	{
		synchronized (this)
		{
			// Print null explicitely
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
	private final PrintStream __printf(String __fmt, Object... __args)
		throws IllegalArgumentException, NullPointerException
	{
		if (__fmt == null)
			throw new NullPointerException("NARG");
		
		// Generate text to write into a string builder
		StringBuilder sb = new StringBuilder();
		try (Formatter f = new Formatter(sb))
		{
			f.format(__fmt, __args);
		}
		
		// Dump characters that were written into the string but without
		// turning it into an actual string, for memory purposes
		synchronized (this)
		{
			for (int i = 0, n = sb.length(); i < n; i++)
				this.__writeChar(sb.charAt(i));
		}
		
		return this;
	}
	
	/**
	 * Prints the end of line sequence that is used for the current platform.
	 *
	 * @return The end of line sequence.
	 * @since 2018/09/21
	 */
	private final void __println()
	{
		synchronized (this)
		{
			// If the newline character has not yet been set, use a fallback
			String nl = PrintStream._NEWLINE;
			if (nl == null)
				nl = "\n";
			
			// Write the ending
			for (int i = 0, n = nl.length(); i < n; i++)
				this.__writeChar(nl.charAt(i));
			
			// Flush the stream after every line printed, in the event the
			// system does not use a UNIX newline
			if (this._autoflush)
				this.flush();
		}
	}
	
	/**
	 * Writes a single character to the output, encoding it as required.
	 *
	 * @param __c The character to write.
	 * @since 2018/09/19
	 */
	private final void __writeChar(char __c)
	{
		byte[] buf = this._buf;
		int bat = this._bat;
		
		// Encode the character into empty space
		int wc = this._encoder.encode(__c, buf, bat, _BUFFER_SIZE - bat);
		
		// {@squirreljme.error ZZ18 Did not expect the buffer to be out of
		// room.}
		if (wc < 0)
			throw new Error("ZZ18");
		
		// Should we flush bytes?
		boolean flush = false;
		if (this._autoflush)
		{
			for (int skim = bat + wc; bat < skim; bat++)
				if (buf[skim] == '\n')
					flush = true;
		}
		else
			bat += wc;
		
		// Store changes
		this._bat = bat;
		
		// Flush?
		if (flush || bat >= _THRESHOLD)
			this.__flush();
	}
}

