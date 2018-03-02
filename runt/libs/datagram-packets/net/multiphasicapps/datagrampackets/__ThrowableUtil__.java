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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * Utilties for exception throwing and such.
 *
 * @since 2018/01/07
 */
final class __ThrowableUtil__
{
	/**
	 * Not used.
	 *
	 * @since 2018/01/07
	 */
	private __ThrowableUtil__()
	{
	}
	
	/**
	 * Returns the base type of the exception.
	 *
	 * @param __c The class type of the input exception.
	 * @return The exception base type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/07
	 */
	static final Class<? extends RemoteThrowable> __baseType(
		Class<? extends Throwable> __c)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		if (Error.class.isAssignableFrom(__c))
			return RemoteError.class;
		else if (SecurityException.class.isAssignableFrom(__c))
			return RemoteSecurityException.class;
		
		// Unknown, use RuntimeException
		return RemoteException.class;
	}
	
	/**
	 * This decodes a throwable which was stored in a packet.
	 *
	 * @param __r The reader to decode from.
	 * @param __ln The local name.
	 * @param __rn The remote name.
	 * @return The decoded throwable from the remote end.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	static final RemoteThrowable __decode(PacketReader __r, String __ln,
		String __rn)
		throws NullPointerException
	{
		if (__r == null || __ln == null || __rn == null)
			throw new NullPointerException("NARG");
		
		// Read suppressed exceptions
		int n = __r.readUnsignedByte();
		Throwable[] suppressed = new Throwable[n];
		for (int i = 0; i < n; i++)
			suppressed[i] = (Throwable)__ThrowableUtil__.__decode(__r,
				__ln, __rn);
		
		// Is there a cause?
		Throwable cause = null;
		if (__r.readBoolean())
			cause = (Throwable)__ThrowableUtil__.__decode(__r, __ln, __rn);
		
		// Read class type
		String type = __r.readString();
		
		// Handle the base type
		Class<?> basetype;
		try
		{
			basetype = Class.forName(__r.readString());
		}
		catch (ClassNotFoundException e)
		{
			basetype = RemoteException.class;
		}
		
		// Read message
		String message = __r.readString();
		
		// Read stack trace
		String trace = __r.readString();
		
		// Build detail which is given to the exception
		RemoteThrowableDetail detail = new RemoteThrowableDetail(message,
			cause, type, basetype.getName(), trace, __ln, __rn);
		
		// Build exception
		RemoteThrowable rv;
		if (basetype == RemoteError.class)
			rv = new RemoteError(detail);
		else if (basetype == RemoteSecurityException.class)
			rv = new RemoteSecurityException(detail);
		else
			rv = new RemoteException(detail);
		
		// Make sure suppressed exceptions are passed
		for (Throwable t : suppressed)
			((Throwable)rv).addSuppressed(t);
		
		return rv;
	}
	
	/**
	 * Encodes the given throwable to the packet.
	 *
	 * @param __t The throwable to encode.
	 * @param __w The target writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	static final void __encode(Throwable __t, PacketWriter __w)
		throws NullPointerException
	{
		if (__t == null || __w == null)
			throw new NullPointerException("NARG");
		
		// Record suppressed exceptions
		Throwable[] suppressed = __t.getSuppressed();
		int n = Math.min(suppressed.length, Byte.MAX_VALUE);
		__w.writeByte(n);
		for (int i = 0; i < n; i++)
			__ThrowableUtil__.__encode(suppressed[i], __w);
		
		// Is there a cause?
		Throwable cause = __t.getCause();
		boolean hascause;
		__w.writeBoolean((hascause = (cause != null)));
		if (hascause)
			__ThrowableUtil__.__encode(cause, __w);
		
		// Class type
		Class<? extends Throwable> tcl = __t.getClass();
		__w.writeString(tcl.getName());
		__w.writeString(__ThrowableUtil__.__baseType(tcl).getName());
		
		// Message, if there is any
		// {@squirreljme.error AT0h Throwable has no message.}
		__w.writeString(Objects.toString(__t.getLocalizedMessage(), "AT0h"));
		
		// Write the stack trace, this is the one which is printed in the
		// printStackTrace call
		String trace = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos, true, "utf-8"))
		{
			// Write and flush so it actually exists
			__t.printStackTrace(ps);
			ps.flush();
			
			// Encode string from written bytes
			trace = new String(baos.toByteArray(), "utf-8");
		}
		
		// Ignore these
		catch (IOException|OutOfMemoryError e)
		{
			trace = "";
		}
		
		// Write it out
		__w.writeString(trace);
	}
	
	/**
	 * Prints the stack trace.
	 *
	 * @param __ps The stream to print to.
	 * @param __t The thrown exception.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/07
	 */
	static final void __printStackTrace(PrintStream __ps, RemoteThrowable __t)
		throws NullPointerException
	{
		if (__ps == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Need the detail
		RemoteThrowableDetail detail = __t.detail();
		
		// Print a message saying this is from another process
		__ps.println("------------------------------");
		String trace = detail.trace();
		if (trace != null)
		{
			__ps.print("REMOTE STACK TRACE: ");
			__ps.println(detail.remoteName());
			__ps.println(trace);
		}
		
		// Also print the local side because that can be important!
		__ps.print("LOCAL STACK TRACE: ");
		__ps.println(detail.localName());
		__t.printLocalStackTrace(__ps);
		__ps.println("------------------------------");
	}
}

