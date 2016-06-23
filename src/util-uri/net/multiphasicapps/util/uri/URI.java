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
	 * @throws URISyntaxException If the URI is malformed.
	 * @since 2016/06/23
	 */
	public URI(String __uri)
		throws URISyntaxException
	{
		super();
		if (false)
			throw new URISyntaxException(null, null);
		throw new Error("TODO");
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
		throw new Error("TODO");
	}
	
	public String getFragment()
	{
		throw new Error("TODO");
	}
	
	public String getHost()
	{
		throw new Error("TODO");
	}
	
	public String getPath()
	{
		throw new Error("TODO");
	}
	
	public int getPort()
	{
		throw new Error("TODO");
	}
	
	public String getQuery()
	{
		throw new Error("TODO");
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
		throw new Error("TODO");
	}
	
	public String getSchemeSpecificPart()
	{
		throw new Error("TODO");
	}
	
	public String getUserInfo()
	{
		throw new Error("TODO");
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
					sb.append((char)('A' + ((c >>> 4) & 0xF)));
					sb.append((char)('A' + (c & 0xF)));
				}
				
				// Otherwise decompose into UTF-8
				else
					throw new Error("TODO");
			}
			
			// Lowercase before adding?
			if (__uc.doLowerCase() && c >= 'A' && c <= 'Z')
				sb.append('a' + (c - 'A'));
			
			// Keep as-is
			else
				sb.append(c);
		}
		
		// Finish
		return sb.toString();
	}
}

