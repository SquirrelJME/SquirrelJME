// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

/**
 * This class contains serializers of data.
 *
 * @since 2019/01/20
 */
public final class DataSerialization
{
	/**
	 * Encodes key value.
	 *
	 * @param __key The key to encode.
	 * @return The resulting key.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
	public static String encodeKey(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
			
		// Encode the key to allow for valid characters
		int n;
		StringBuilder sb = new StringBuilder((n = __key.length()));
		for (int i = 0; i < n; i++)
		{
			char c = __key.charAt(i);
			
			// Possibly re-encoded?
			boolean enc = false;
			if (c >= 'A' && c <= 'Z')
				c = Character.toLowerCase(c);
			
			// Re-encode?
			char redo = 0;
			switch (c)
			{
				case '+':	redo = 'p'; break;
				case '#':	redo = 'h'; break;
				case '.':	redo = 'd'; break;
				case '-':	redo = '-'; break;
			}
			
			// Was this value being re-encoded?
			if (redo != 0)
			{
				sb.append('-');
				sb.append(redo);
			}
			else
				sb.append(c);
		}
		
		// Use the encoded key
		return sb.toString();
	}
	
	/**
	 * Encodes the given string to a manifest safe format.
	 *
	 * @param __s The string to encode.
	 * @return The encoded string, {@code null} has a special value.
	 * @since 2018/10/06
	 */
	public static String encodeString(String __s)
	{
		// Special value for null strings
		if (__s == null)
			return "\\NULL";
		
		// Encode characters to normalize them
		StringBuilder sb = new StringBuilder(__s.length());
		for (int i = 0, n = __s.length(); i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Is the character to be translated?
			boolean escape = false;
			switch (c)
			{
					// Just escape these
				case '\\':
				case '"':
					escape = true;
					break;
					
					// Make spaces just in the form of an underline to make
					// them more spacey but easier to see
				case ' ':
					escape = true;
					c = '_';
					break;
					
					// Newline
				case '\n':
					escape = true;
					c = 'n';
					break;
					
					// Carriage return
				case '\r':
					escape = true;
					c = 'r';
					break;
					
					// Tab
				case '\t':
					escape = true;
					c = 't';
					break;
					
					// Opening brace
				case '{':
					escape = true;
					c = '(';
					break;
					
					// Closing brace
				case '}':
					escape = true;
					c = ')';
					break;
					
					// Delete
				case 0x7F:
					escape = true;
					c = 'd';
					break;
				
				case 0x00:
				case 0x01:
				case 0x02:
				case 0x03:
				case 0x04:
				case 0x05:
				case 0x06:
				case 0x07:
				case 0x08:
				case 0x0b:
				case 0x0c:
				case 0x0e:
				case 0x1f:
				case 0x10:
				case 0x11:
				case 0x12:
				case 0x13:
				case 0x14:
				case 0x15:
				case 0x16:
				case 0x17:
				case 0x18:
				case 0x19:
				case 0x1a:
				case 0x1b:
				case 0x1c:
				case 0x1d:
				case 0x1e:
					escape = true;
					c = (char)((c < 10 ? '0' + c : 'A' + (c - 10)));
					break;
				
					// Not changed
				default:
					break;
			}
			
			// Character is out of range?
			if (c >= 0x7F)
				sb.append(String.format("\\@%04x", (int)c));
			
			// Append printable
			else
			{
				if (escape)
					sb.append('\\');
				sb.append(c);
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Serialize the given object to a string.
	 *
	 * @param __o The value to serialize.
	 * @return The resulting serialization.
	 * @since 2019/01/20
	 */
	public static String serialize(Object __o)
	{
		// Null
		if (__o == null)
			return "null";
		
		// No result generated
		else if (__o instanceof __NoResult__)
			return "NoResult";
		
		// Undefined
		else if (__o instanceof __UndefinedResult__)
			return "UndefinedResult";
		
		// Exception was thrown
		else if (__o instanceof __ExceptionThrown__)
			return "ExceptionThrown";
		
		// No exception was thrown
		else if (__o instanceof __NoExceptionThrown__)
			return "NoExceptionThrown";
		
		// Boolean values
		else if (__o instanceof Boolean)
			return __o.toString();
		
		// String
		else if (__o instanceof String)
			return "string:" + DataSerialization.encodeString((String)__o);
		
		// Byte
		else if (__o instanceof Byte)
			return "byte:" + __o;
		
		// Short
		else if (__o instanceof Short)
			return "short:" + __o;
		
		// Character
		else if (__o instanceof Character)
			return "char:" + (int)((Character)__o).charValue();
		
		// Integer
		else if (__o instanceof Integer)
			return "int:" + __o;
		
		// Long
		else if (__o instanceof Long)
			return "long:" + __o;
		
		// Byte array
		else if ((__o instanceof byte[]) || __o instanceof Byte[])
		{
			// Convert first
			if (__o instanceof Byte[])
			{
				Byte[] a = (Byte[])__o;
				int n = a.length;
				byte[] b = new byte[n];
				for (int i = 0; i < n; i++)
				{
					Byte v = a[i];
					b[i] = (v == null ? 0 : v.byteValue());
				}
				__o = b;
			}
			
			// Print values
			byte[] a = (byte[])__o;
			int n = a.length;
			StringBuilder sb = new StringBuilder(
				String.format("byte[%d]:", n));
			for (int i = 0; i < n; i++)
			{
				if (i > 0)
					sb.append(",");
				sb.append(a[i]);
			}
			return sb.toString();
		}
		
		// Short array
		else if ((__o instanceof short[]) || __o instanceof Short[])
		{
			// Convert first
			if (__o instanceof Short[])
			{
				Short[] a = (Short[])__o;
				int n = a.length;
				short[] b = new short[n];
				for (int i = 0; i < n; i++)
				{
					Short v = a[i];
					b[i] = (v == null ? 0 : v.shortValue());
				}
				__o = b;
			}
			
			// Print values
			short[] a = (short[])__o;
			int n = a.length;
			StringBuilder sb = new StringBuilder(
				String.format("short[%d]:", n));
			for (int i = 0; i < n; i++)
			{
				if (i > 0)
					sb.append(",");
				sb.append(a[i]);
			}
			return sb.toString();
		}
		
		// Character array
		else if ((__o instanceof char[]) || __o instanceof Character[])
		{
			// Convert first
			if (__o instanceof Character[])
			{
				Character[] a = (Character[])__o;
				int n = a.length;
				char[] b = new char[n];
				for (int i = 0; i < n; i++)
				{
					Character v = a[i];
					b[i] = (v == null ? 0 : v.charValue());
				}
				__o = b;
			}
			
			// Print values
			char[] a = (char[])__o;
			int n = a.length;
			StringBuilder sb = new StringBuilder(
				String.format("char[%d]:", n));
			for (int i = 0; i < n; i++)
			{
				if (i > 0)
					sb.append(",");
				sb.append((int)a[i]);
			}
			return sb.toString();
		}
		
		// Integer array
		else if ((__o instanceof int[]) || __o instanceof Integer[])
		{
			// Convert first
			if (__o instanceof Integer[])
			{
				Integer[] a = (Integer[])__o;
				int n = a.length;
				int[] b = new int[n];
				for (int i = 0; i < n; i++)
				{
					Integer v = a[i];
					b[i] = (v == null ? 0 : v.intValue());
				}
				__o = b;
			}
			
			// Print values
			int[] a = (int[])__o;
			int n = a.length;
			StringBuilder sb = new StringBuilder(
				String.format("int[%d]:", n));
			for (int i = 0; i < n; i++)
			{
				if (i > 0)
					sb.append(",");
				sb.append(a[i]);
			}
			return sb.toString();
		}
		
		// String array
		else if (__o instanceof String[])
		{
			// Print values
			String[] a = (String[])__o;
			int n = a.length;
			StringBuilder sb = new StringBuilder(
				String.format("string[%d]:", n));
			for (int i = 0; i < n; i++)
			{
				if (i > 0)
					sb.append(",");
				sb.append(DataSerialization.encodeString(a[i]));
			}
			return sb.toString();
		}
		
		// Throwable, meta data is used
		else if (__o instanceof Throwable)
		{
			Throwable t = (Throwable)__o;
			
			StringBuilder sb = new StringBuilder("throwable:");
			
			// The last package to shorten the classes
			String lastpkg = "java.lang.";
			
			// Throwables may be of multiple class types and it usually is
			// expected that they are some base class. For example a class
			// can thrown some FooIndexOutOfBoundsException which is based
			// off IndexOutOfBoundsException, if the result expects the
			// base class then it must still pass. So store the entire class
			// tree.
			boolean comma = false;
			for (Class<?> x = t.getClass(); x != null && x != Object.class;
				x = x.getSuperclass())
			{
				// Clip off the package if it matches
				String clname = x.getName();
				if (clname.startsWith(lastpkg))
					clname = clname.substring(lastpkg.length());
				
				// Otherwise remember the package used
				else
				{
					int ld = clname.lastIndexOf('.');
					if (ld >= 0)
						lastpkg = clname.substring(0, ld + 1);
					
					// Maybe default package?
					else
						lastpkg = "";
				}
				
				// Split to encode multiple classes
				if (comma)
					sb.append(',');
				comma = true;
				
				// Append class name here
				sb.append(clname);
			}
			
			// For debug purposes, encode the message information
			String msg = t.getMessage();
			if (msg != null)
			{
				sb.append(':');
				sb.append(DataSerialization.encodeString(msg));
			}
			
			// Now metadata is included for this
			return sb.toString();
		}
		
		// Unrepresented object, just use its string representation in an
		// encoded form
		else
			return "other:" + __o.getClass().getName() + ":" +
				DataSerialization.encodeString(__o.toString());
	}
}

