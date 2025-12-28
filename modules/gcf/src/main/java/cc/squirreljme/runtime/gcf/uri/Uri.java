// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.uri;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * This is a URI which consists of a scheme and a {@link UriPart}.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public final class Uri
	implements Comparable<Uri>
{
	/** The scheme. */
	protected final String scheme;
	
	/** The part. */
	protected final UriPart part;
	
	/**
	 * Parses the given URI.
	 *
	 * @param __uri The URI to parse.
	 * @throws InvalidUriException If the URI is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public Uri(String __uri)
		throws InvalidUriException, NullPointerException
	{
		this(Uri.__scheme(__uri), Uri.__partFull(__uri));
	}
	
	/**
	 * Parses the given URI.
	 *
	 * @param __scheme The scheme.
	 * @param __part The part to parse.
	 * @throws InvalidUriException If the URI is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public Uri(String __scheme, String __part)
		throws InvalidUriException, NullPointerException
	{
		this(__scheme, Uri.__partParse(__part));
	}
	
	/**
	 * Initializes a full URI.
	 *
	 * @param __scheme The scheme.
	 * @param __part The parsed part.
	 * @throws InvalidUriException If the URI is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public Uri(String __scheme, UriPart __part)
		throws InvalidUriException, NullPointerException
	{
		if (__scheme == null || __part == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public int compareTo(@NotNull Uri __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the URI part.
	 *
	 * @return The URI part.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public UriPart getPart()
	{
		return this.part;
	}
	
	/**
	 * Returns the URI scheme.
	 *
	 * @return The URI scheme.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public String getScheme()
	{
		return this.scheme;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Parses the given URI.
	 *
	 * @param __uri The URI to parse.
	 * @return The resultant URI part.
	 * @throws InvalidUriException If the URI is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	private static UriPart __partFull(String __uri)
		throws InvalidUriException, NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EC21 Expected a scheme specifier in the
		URI. (The URI)} */
		int fc = __uri.indexOf(':');
		if (fc < 0)
			throw new InvalidUriException(
				__error__("EC21 %s", __uri));
		
		// Parse everything after it
		return Uri.__partParse(__uri.substring(fc + 1));
	}
	
	/**
	 * Parses the given URI.
	 *
	 * @param __part The URI part to parse.
	 * @return The resultant URI part.
	 * @throws InvalidUriException If the URI is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	private static UriPart __partParse(String __part)
		throws InvalidUriException, NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		// Generic part?
		if (__part.startsWith("//"))
			return new UriGenericPart(__part);
		return new UriSchemeSpecificPart(__part);
	}
	
	/**
	 * Extracts the URI scheme.
	 *
	 * @param __uri The URI to extract the scheme from.
	 * @return The resultant scheme.
	 * @throws InvalidUriException If the URI is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	private static String __scheme(String __uri)
		throws InvalidUriException, NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EC21 Expected a scheme specifier in the
		URI. (The URI)} */
		int fc = __uri.indexOf(':');
		if (fc < 0)
			throw new InvalidUriException(
				__error__("EC21 %s", __uri));
		
		// Return everything before it
		return __uri.substring(0, fc);
	}
}
