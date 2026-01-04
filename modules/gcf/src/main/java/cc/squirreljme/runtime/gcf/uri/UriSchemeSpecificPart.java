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
 * Scheme specific URI part, this does not have any restrictions to the URI
 * formatting similar to {@link UriGenericPart}.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public final class UriSchemeSpecificPart
	extends UriPart
	implements UriPartFragment, UriPartSchemeSpecific
{
	/** The decoded scheme specific part. */
	protected final String schemeSpecific;
	
	/** The decoded fragment. */
	protected final String fragment;
	
	/**
	 * Parses the given URI part as a scheme specific part.
	 *
	 * @param __part The part to parse.
	 * @throws InvalidUriException If the part is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public UriSchemeSpecificPart(String __part)
		throws InvalidUriException, NullPointerException
	{
		super(__part);
		
		// Is there a fragment?
		int hc = __part.indexOf('#');
		
		// Extract scheme specific part and the fragment, if any
		String specific = (hc < 0 ? __part : __part.substring(0, hc));
		String fragment = (hc < 0 ? null : __part.substring(hc + 1));
		
		// Note that only the fragment is checked, as the scheme specific part
		// is really up to the scheme
		/* {@squirreljme.error Fragment contains an invalid
		character. (The URI part)} */
		if (fragment != null)
			for (int n = fragment.length(), i = 0; i < n; i++)
				if (!UriPart.isFragment(fragment.charAt(i)))
					throw new InvalidUriException(
						__error__("EC27 %s", __part));
		
		// Store decoded versions
		this.schemeSpecific = UriPart.decode(specific);
		this.fragment = (fragment == null ? null :
			UriPart.decode(fragment));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public String getFragment()
	{
		return this.fragment;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public String getSchemeSpecific()
	{
		return this.schemeSpecific;
	}
}
