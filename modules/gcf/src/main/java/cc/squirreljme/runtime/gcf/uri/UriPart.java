// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.uri;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StringUtils;
import java.io.UnsupportedEncodingException;

import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * This is the part specific part of a URI.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public abstract class UriPart
	implements Comparable<UriPart>
{
	/** The original full part. */
	@SquirrelJMEVendorApi
	protected final String original;
	
	/**
	 * Initializes the base part.
	 *
	 * @param __part The full part.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	@KeepWhenCompacting
	UriPart(String __part)
		throws NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		// Remember the original full part
		this.original = __part;
		
		// Perform a check for invalid characters
		for (int n = __part.length(), i = 0; i < n; i++)
		{
			char c = __part.charAt(i);
			
			/* {@squirreljme.error EC23 Invalid URI character. (The URI part;
			the character)} */
			if (!UriPart.isAny(c))
				throw new InvalidUriException(
					__error__("EC23 %s %c", __part, c));
		}
	}
	
	/**
	 * Returns this URI as a generic URI.
	 *
	 * @return The generic URI.
	 * @throws InvalidUriException If this is not a generic URI.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public final UriGenericPart asGeneric()
		throws InvalidUriException
	{
		/* {@squirreljme.error EC22 This is a not a generic URI part. */
		if (!(this instanceof UriGenericPart))
			throw new InvalidUriException(
				__error__("EC22"));
		
		return (UriGenericPart)this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public final int compareTo(UriPart __b)
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// The original URIs are very different
		int rv = this.original.compareTo(__b.original);
		if (rv != 0)
			return rv;
		
		// Are these actually the same class?
		if (this.equals(__b))
			return 0;
		
		// Otherwise order based on class type
		return this.getClass().getName().compareTo(
			__b.getClass().getName());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public final int hashCode()
	{
		return this.original.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		else if (__o == null || this.getClass() != __o.getClass())
			return false;
		
		// Must be the same original path and the same type
		return this.original.equals(((UriPart)__o).original);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public final String toString()
	{
		return this.original;
	}
	
	/**
	 * Decodes the given string.
	 *
	 * @param __in The input string.
	 * @return The resultant decoded characters.
	 * @throws InvalidUriException If the input contains an invalid encoding.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public static String decode(String __in)
		throws InvalidUriException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// If there are no percents, then nothing actually needs to be
		// decoded
		int anyPercent = __in.indexOf('%');
		if (anyPercent < 0)
			return __in;
		
		// First get the bytes to process
		byte[] inBytes;
		try
		{
			inBytes = __in.getBytes("utf-8");
		}
		
		// Should not occur
		catch (UnsupportedEncodingException __e)
		{
			throw new InvalidUriException(__e.getMessage(), __e);
		}
		
		// Go through and scan through to find percent indicators
		int len = inBytes.length;
		for (int i = 0; i < len; i++)
		{
			// Skip non-percent
			if (inBytes[i] != '%')
				continue;
			
			/* {@squirreljme.error EB25 URI section contains a truncated
			escape sequence. (The input section)} */
			if (i + 3 > len)
				throw new InvalidUriException(
					__error__("EB25 %s", __in));
			
			/* {@squirreljme.error EB25 URI section contains an invalid
			escape sequence. (The input section)} */
			int hi = Character.digit((char)inBytes[i + 1], 16);
			int lo = Character.digit((char)inBytes[i + 2], 16);
			if (hi < -1 || lo < -1)
				throw new InvalidUriException(
					__error__("EB26 %s", __in));
			
			// Replace the percent
			inBytes[i] = (byte)((hi << 4) | lo);
			
			// Move the entire right chunk down
			if (i + 3 < len)
				ObjectShelf.arrayCopy(inBytes, i + 3,
					inBytes, i + 1, (len - (i + 1)) - 2);
			ObjectShelf.arrayFill(inBytes, len - 2, 2, (byte)0);
			len -= 2;
		}
		
		// Convert back to string
		try
		{
			return new String(inBytes, 0, len, "utf-8");
		}
		
		// Should not occur
		catch (UnsupportedEncodingException __e)
		{
			throw new InvalidUriException(__e.getMessage(), __e);
		}
	}
	
	/**
	 * Encodes the given string so it can be stored in the URI.
	 *
	 * @param __in The input string.
	 * @return The resultant string.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/29
	 */
	@SquirrelJMEVendorApi
	public static String encode(String __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// First determine if it actually needs to be encoded
		boolean needsEncode = false;
		int n = __in.length();
		for (int i = 0; i < n; i++)
			if (needsEncode |= (!UriPart.isUnreserved(__in.charAt(i))))
				break;
		
		// Does not need encoding?
		if (!needsEncode)
			return __in;
		
		// Encode anything that is not reserved
		try
		{
			StringBuilder sb = new StringBuilder(__in.length());
			
			// Encode to UTF-8
			byte[] in = __in.getBytes("utf-8");
			for (int bn = in.length, i = 0; i < bn; i++)
			{
				char c = (char)(in[i] & 0xFF);
				
				// Add directly if unreserved
				if (UriPart.isUnreserved(__in.charAt(i)))
				{
					sb.append(c);
					continue;
				}
				
				// Otherwise, use percent encoding
				sb.append('%');
				sb.append(Character.forDigit((c >> 4) & 0xF, 16));
				sb.append(Character.forDigit(c & 0xF, 16));
			}
			
			// Return the encoded string
			return sb.toString();
		}
		catch (UnsupportedEncodingException __e)
		{
			throw Debugging.oops();
		}
	}
	
	/**
	 * Is this any valid URI character?
	 *
	 * @param __c The character to check.
	 * @return If this is valid or not.
	 * @since 2025/12/29
	 */
	@SquirrelJMEVendorApi
	public static boolean isAny(char __c)
	{
		return __c == ':' || __c == '#' || __c == '[' || __c == ']' || 
			__c == '@' || __c == '/' || __c == '?' || __c == '!' || 
			__c == '$' || __c == '&' || __c == '\'' || __c == '(' || 
			__c == ')' || __c == '*' || __c == '+' || __c == ',' || 
			__c == ';' || __c == '=' || (__c >= 'a' && __c <= 'z') || 
			(__c >= 'A' && __c <= 'Z') || (__c >= '0' && __c <= '9') || 
			__c == '-' || __c == '.' || __c == '_' || __c == '~' ||
			__c == '%';
	}
	
	/**
	 * Is this a valid gen-delimiter?
	 *
	 * @param __c The character to check.
	 * @return If this is valid or not.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public static boolean isGenDelim(char __c)
	{
		return __c == ':' || __c == '/' || __c == '?' || __c == '#' ||
			__c == '[' || __c == ']' || __c == '@';
	}
	
	/**
	 * Is this a valid hexidecimal digit?
	 *
	 * @param __c The character to check.
	 * @return If this is valid or not.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public static boolean isHexDigit(char __c)
	{
		return (__c >= 'a' && __c <= 'f') || (__c >= 'A' && __c <= 'F') ||
			(__c >= '0' && __c <= '9');
	}
	
	@SquirrelJMEVendorApi
	public static boolean isQuery(char __c)
	{
		return __c == '/' || __c == '?' || UriPart.isPChar(__c);
	}
	
	/**
	 * Is this a valid p-character?
	 *
	 * @param __c The character to check.
	 * @return If this is valid or not.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public static boolean isPChar(char __c)
	{
		return UriPart.isUnreserved(__c) || UriPart.isHexDigit(__c) ||
			__c == '%' || __c == ':' || __c == '@';
	}
	
	/**
	 * Is this a valid reserved character?
	 *
	 * @param __c The character to check.
	 * @return If this is valid or not.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public static boolean isReserved(char __c)
	{
		return UriPart.isGenDelim(__c) || UriPart.isSubDelim(__c);
	}
	
	/**
	 * Is this a valid character for a fragment?
	 *
	 * @param __c The character to check.
	 * @return If this is valid or not.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public static boolean isFragment(char __c)
	{
		return __c == '/' || __c == '?' || UriPart.isPChar(__c);
	}
	
	
	/**
	 * Is this a valid sub-delimiter?
	 *
	 * @param __c The character to check.
	 * @return If this is valid or not.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public static boolean isSubDelim(char __c)
	{
		return __c == '!' || __c == '$' || __c == '&' || __c == '\'' ||
			__c == '(' || __c == ')' || __c == '*' || __c == '+' ||
			__c == ',' || __c == ';' || __c == '=';
	}
	
	/**
	 * Is this a valid unreserved character?
	 *
	 * @param __c The character to check.
	 * @return If this is valid or not.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public static boolean isUnreserved(char __c)
	{
		return (__c >= 'a' && __c <= 'z') || (__c >= 'A' && __c <= 'Z') ||
			(__c >= '0' && __c <= '9') || __c == '-' || __c == '.' ||
			__c == '_' || __c == '~';
	}
	
	/**
	 * Splits with the delimiter and then decodes the parameters.
	 *
	 * @param __in The input string.
	 * @param __delim The delimiter to use.
	 * @return The split string with delimiters
	 * @throws InvalidUriException If the decoded path is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/29
	 */
	@SquirrelJMEVendorApi
	public static String[] splitDecode(String __in, char __delim)
		throws InvalidUriException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Splice and decode
		String[] splice = StringUtils.basicSplit(__delim, __in);
		for (int i = 0; i < splice.length; i++)
			splice[i] = UriPart.decode(splice[i]);
		
		return splice;
	}
}
