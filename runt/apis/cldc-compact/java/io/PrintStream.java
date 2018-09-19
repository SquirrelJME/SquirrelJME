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
	
	private static final String _NEWLINE;
	
	/** The stream to write bytes to. */
	private final OutputStream _out;
	
	/** Is auto-flushing to be used? */
	private final boolean _autoflush;
	
	/** The encoder used to encode chars to bytes. */
	private final Encoder _encoder;
	
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
	
	@Override
	public void flush()
	{
		throw new todo.TODO();
	}
	
	public PrintStream format(String __a, Object... __b)
	{
		throw new todo.TODO();
	}
	
	public void print(boolean __a)
	{
		throw new todo.TODO();
	}
	
	public void print(char __a)
	{
		throw new todo.TODO();
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
	
	public void print(String __a)
	{
		throw new todo.TODO();
	}
	
	public void print(Object __a)
	{
		throw new todo.TODO();
	}
	
	public PrintStream printf(String __a, Object... __b)
	{
		throw new todo.TODO();
	}
	
	public void println()
	{
		// If the newline character has not yet been set, use a fallback
		String nl = PrintStream._NEWLINE;
		if (nl == null)
			nl = "\n";
		
		throw new todo.TODO();
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
	 * Prints the given string to the output stream followed by the system
	 * newline character.
	 *
	 * @param __v The string to write.
	 * @since 2018/09/18
	 */
	public void println(String __v)
	{
		throw new todo.TODO();
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
}

