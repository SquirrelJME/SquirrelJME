// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.uri;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a representation of standard URIs and allows information that is
 * contained within a URI to be obtained and modified.
 *
 * This class is immutable and thread safe.
 *
 * Note that this class is not fully compatible with Java SE's URI class. This
 * implementation is more strict and more conforming generally.
 *
 * @since 2016/06/23
 */
public final class URI
	implements Comparable<URI>
{
	/** The encoded scheme. */
	protected final String scheme;
	
	/** The scheme specific part. */
	protected final String schemepart;
	
	/** The encoded authority. */
	protected final String authority;
	
	/** The encoded path. */
	protected final String path;
	
	/** The encoded query. */
	protected final String query;
	
	/** The encoded fragment. */
	protected final String fragment;
	
	/** The port. */
	protected final int port;
	
	/** The user information. */
	protected final String userinfo;
	
	/** The host. */
	protected final String host;
	
	/**
	 * Parses the given string as a URI.
	 *
	 * The input URI must be well formed where all input characters are
	 * already encoded.
	 *
	 * @param __uri The URI to parse.
	 * @throws NullPointerException On null arguments.
	 * @throws URISyntaxException If the URI is malformed.
	 * @since 2016/06/23
	 */
	public URI(String __uri)
		throws NullPointerException, URISyntaxException
	{
		// Check
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("DEBUG -- Check URI `%s`%n", __uri);
		
		// There are two primary URI forms:
		// foo://example.com:8042/over/there?name=ferret#nose
		// urn:example:animal:ferret:nose
		// /absolute/path
		// relative-path
		int n = __uri.length();
		
		// First colon position?
		int fcol = __uri.indexOf(':');
		
		// Starts with a slash?
		boolean sslash = __uri.startsWith("/");
		
		// Colon followed by //?
		boolean isheir = (fcol >= 0 && !sslash && fcol + 3 <= n &&
			__uri.substring(fcol, fcol + 3).equals("://"));
		
		System.err.printf("DEBUG -- n=%d fcol=%d sslash=%s isheir=%s%n",
			n, fcol, sslash, isheir);
		
		// Scheme specific URI
		if (fcol >= 0 && !sslash && !isheir)
		{
			// Decode scheme up to the colon
			this.scheme = __decode(__URIChars__.SCHEME,
				__uri.substring(0, fcol));
			
			// May have a fragment
			int frag = __uri.indexOf(fcol + 1, '#');
			if (frag >= 0)
			{
				this.schemepart = __decode(__URIChars__.PATH,
					__uri.substring(fcol + 1, frag));
				this.fragment = __decode(__URIChars__.FRAGMENT,
					__uri.substring(frag + 1));
			}
			
			// No fragment, the entire thing is the scheme specific part
			else
			{
				this.schemepart = __decode(__URIChars__.PATH,
					__uri.substring(fcol + 1));
				this.fragment = null;
			}
			
			// Not used
			this.authority = null;
			this.path = null;
			this.query = null;
			this.port = -1;
			this.userinfo = null;
			this.host = null;
		}
		
		// {@squirreljme.error DU02 The given URI is not valid.
		// (The input URI)}
		else
			throw new URISyntaxException(String.format("DU02 %s", __uri));
	}
	
	public URI(String __a, String __b, String __c, int __d, String __e,
		String __f, String __g)
		throws URISyntaxException
	{
		super();
		if (false)
			throw new URISyntaxException(null, null);
		throw new Error("TODO");
	}
	
	public URI(String __a, String __b, String __c, String __d, String __e)
		throws URISyntaxException
	{
		super();
		if (false)
			throw new URISyntaxException(null, null);
		throw new Error("TODO");
	}
	
	public URI(String __a, String __b, String __c, String __d)
		throws URISyntaxException
	{
		super();
		if (false)
			throw new URISyntaxException(null, null);
		throw new Error("TODO");
	}
	
	/**
	 * Initializes a URI using the given optional scheme, scheme specific part,
	 * and fragment. Each part of the URI is encoded even if there are already
	 * encoded characters in the URI.
	 *
	 * @param __scheme The scheme.
	 * @param __ssp The scheme specific part.
	 * @param __frag The fragment.
	 * @throws URISyntaxException If the resulting URI is not valid.
	 * @since 2016/06/23
	 */
	public URI(String __scheme, String __ssp, String __frag)
		throws URISyntaxException
	{
		this(__construct(__scheme, __ssp, __frag));
	}
	
	@Override
	public int compareTo(URI __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	public String getAuthority()
	{
		return this.authority;
	}
	
	public String getFragment()
	{
		return this.fragment;
	}
	
	public String getHost()
	{
		return this.host;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	public int getPort()
	{
		return this.port;
	}
	
	public String getQuery()
	{
		return this.query;
	}
	
	public String getRawAuthority()
	{
		throw new Error("TODO");
	}
	
	public String getRawFragment()
	{
		throw new Error("TODO");
	}
	
	public String getRawPath()
	{
		throw new Error("TODO");
	}
	
	public String getRawQuery()
	{
		throw new Error("TODO");
	}
	
	public String getRawSchemeSpecificPart()
	{
		throw new Error("TODO");
	}
	
	public String getRawUserInfo()
	{
		throw new Error("TODO");
	}
	
	public String getScheme()
	{
		return this.scheme;
	}
	
	public String getSchemeSpecificPart()
	{
		return this.schemepart;
	}
	
	public String getUserInfo()
	{
		return this.userinfo;
	}
	
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	public boolean isAbsolute()
	{
		throw new Error("TODO");
	}
	
	public boolean isOpaque()
	{
		throw new Error("TODO");
	}
	
	public URI normalize()
	{
		throw new Error("TODO");
	}
	
	public URI parseServerAuthority()
		throws URISyntaxException
	{
		if (false)
			throw new URISyntaxException(null, null);
		throw new Error("TODO");
	}
	
	public URI relativize(URI __a)
	{
		throw new Error("TODO");
	}
	
	public URI resolve(URI __a)
	{
		throw new Error("TODO");
	}
	
	public URI resolve(String __a)
	{
		throw new Error("TODO");
	}
	
	public String toASCIIString()
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	public static URI create(String __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Builds a string which is used during construction.
	 *
	 * @param __scheme The scheme.
	 * @param __ssp The scheme specific part.
	 * @param __frag The fragment.
	 * @throws URISyntaxException If the resulting URI is not valid.
	 * @since 2016/06/23
	 */
	private static String __construct(String __scheme, String __ssp,
		String __frag)
	{
		StringBuilder sb = new StringBuilder();
		
		// Add the scheme?
		if (__scheme != null)
		{
			sb.append(__encode(__URIChars__.SCHEME, __scheme));
			sb.append(':');
		}
		
		// Add the scheme specific part?
		if (__ssp != null)
			sb.append(__encode(__URIChars__.PATH, __ssp));
		
		// Add the fragment?
		if (__frag != null)
		{
			sb.append('#');
			sb.append(__encode(__URIChars__.FRAGMENT, __frag));
		}
		
		// Build
		return sb.toString();
	}
	
	/**
	 * Decodes the given string checking the input characters against the
	 * permitted set and performing decoding of hexadecimal sequences.
	 *
	 * @param __uc The valid character set.
	 * @param __s The input string to be decoded.
	 * @return The decoded form of the given input string.
	 * @throws NullPointerException On null arguments.
	 * @throws URISyntaxException If an illegal character or other sequence
	 * is specified.
	 * @since 2016/06/23
	 */
	private static String __decode(__URIChars__ __uc, String __s)
		throws NullPointerException, URISyntaxException
	{
		// Check
		if (__uc == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Output
		StringBuilder sb = new StringBuilder();
		
		// Go through each character
		int n = __s.length();
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// {@squirreljme.error DU03 Only ASCII characters are permitted
			// in URIs. (The URI piece; The non-ASCII character)}
			if (c >= 0x7F)
				throw new URISyntaxException(String.format("DU03 %s %d", __s,
					(int)c));
			
			// Hexadecimal sequence?
			if (c == '%')
				throw new Error("TODO");
			
			// Otherwise a non-"quoted" character
			else
			{
				// {@squirreljme.error DU04 The specified character is not a
				// valid part of the specified sequence in a URI. (The URI
				// piece; The character; The URI part being decoded)}
				if (!__uc.isValid(i, c))
					throw new URISyntaxException(String.format("DU04 %s %d %s",
						__s, (int)c, __uc));
				
				// Add to the output
				sb.append((char)c);
			}
		}
		
		// Finish
		return sb.toString();
	}
	
	/**
	 * Encodes the given string so that the scheme is valid.
	 *
	 * @param __uc The type of charactes to use.
	 * @param __s The string to encode.
	 * @return The result of encoding.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/23
	 */
	private static String __encode(__URIChars__ __uc, String __s)
		throws NullPointerException
	{
		// Check
		if (__uc == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Output
		StringBuilder sb = new StringBuilder();
		
		// Go through the input string
		int n = __s.length();
		for (int i = 0; i < n; i++)
		{
			// Get
			char c = __s.charAt(i);
			
			// If not valid? Encode as UTF-8
			if (!__uc.isValid(i, c))
			{
				// Add percent sign
				sb.append('%');
				
				// Lower ASCII? simple addition
				if (c <= 0x7F)
				{
					sb.append(__hexDigit((c >>> 4) & 0xF));
					sb.append(__hexDigit(c & 0xF));
				}
				
				// Otherwise decompose into UTF-8
				else
					throw new Error("TODO");
			}
			
			// Lowercase before adding?
			else if (__uc.doLowerCase() && c >= 'A' && c <= 'Z')
				sb.append((char)('a' + (c - 'A')));
			
			// Keep as-is
			else
				sb.append((char)c);
		}
		
		// Finish
		return sb.toString();
	}
	
	/**
	 * Returns the hexadecimal digit for the given value.
	 *
	 * @param __d The digit to convert.
	 * @return The representing character.
	 * @throws IllegalArgumentException If the digit is out of range.
	 * @since 2016/06/23
	 */
	private static char __hexDigit(int __d)
		throws IllegalArgumentException
	{
		// {@squirreljme.error DU01 The specified digit is out of range for
		// a hexadecimal value. (The digit)}
		if (__d < 0 || __d >= 16)
			throw new IllegalArgumentException(String.format("DU01 %d", __d));
		
		// Number?
		if (__d < 10)
			return (char)('0' + __d);
		
		// Letter
		else
			return (char)('A' + (__d - 10));
	}
}

