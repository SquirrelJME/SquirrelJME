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
import cc.squirreljme.runtime.cldc.debug.ErrorCode;

import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * Generic URI part.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public final class UriGenericPart
	extends UriPart
	implements UriPartAuthority, UriPartFragment, UriPartPath,
		UriPartPathParameter, UriPartQueryParameter
{
	/** The authority. */
	@SquirrelJMEVendorApi
	protected final UriAuthority authority;
	
	/** The decoded fragment. */
	@SquirrelJMEVendorApi
	protected final String fragment;
	
	/** The decoded path parameters. */
	@SquirrelJMEVendorApi
	protected final String rawPathParams;
	
	/** The query parameters. */
	@SquirrelJMEVendorApi
	protected final String rawQueryParams;
	
	/** The URI path. */
	@SquirrelJMEVendorApi
	protected final String path;
	
	/** The decoded path parameters. */
	private final String[] _pathParams;
	
	/** The decoded query parameters. */
	private final String[] _queryParams;
	
	/**
	 * Parses the given URI part as a generic part.
	 *
	 * @param __part The part to parse.
	 * @throws InvalidUriException If the part is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public UriGenericPart(String __part)
		throws InvalidUriException, NullPointerException
	{
		super(__part);
		
		// There are three specific splits for URIs: path, path parameters,
		// query parameters, and fragment
		int pp = __part.indexOf(';');
		int qp = __part.indexOf('?');
		int fr = __part.indexOf('#');
		
		// Path parameters are part of the path, however being a sub-delimiter
		// it is possible for it to be used in queries or the fragment and
		// as such, if it is used and is after those, ignore it
		if ((qp >= 0 && pp > qp) || (fr >= 0 && pp > fr))
			pp = -1;
		
		// Fragments are permitted to have ?, and if this is the case then
		// the query is dropped
		if (fr >= 0 && qp > fr)
			qp = -1;
		
		// The path ends at the earliest of these points
		int en = Math.min(Math.min(Math.min(__part.length(),
			(pp >= 0 ? pp : Integer.MAX_VALUE)),
			(qp >= 0 ? qp : Integer.MAX_VALUE)),
			(fr >= 0 ? fr : Integer.MAX_VALUE));
		
		// The query parameter either ends at the end of the part or where
		// the fragment starts
		int qe = Math.min(__part.length(),
			(fr >= 0 ? fr : Integer.MAX_VALUE));
		
		// The path parameters ends at the length, query, or fragment
		int ep = Math.min(Math.min(__part.length(),
			(qp >= 0 ? qp : Integer.MAX_VALUE)),
			(fr >= 0 ? fr : Integer.MAX_VALUE));
		
		// Get the three main parts of the URI, keep the authority and path
		// together for now
		String authPath = __part.substring(0, en);
		String pathParams = (pp < 0 ? null : __part.substring(pp + 1, ep));
		String queryParams = (qp < 0 ? null : __part.substring(qp + 1, qe));
		String fragment = (fr < 0 ? null : __part.substring(fr + 1));
		
		// Path must start with slash-slash
		/* {@squirreljme.error EC28 URI does not start with slash-slash.
		(The URI; The authority/path) */
		if (!authPath.startsWith("//"))
			throw new InvalidUriException(
				__error__("EC28 %s %s", __part, authPath));
		
		/* {@squirreljme.error EC29 URI too short. (The URI)} */
		if (authPath.length() < 3)
			throw new InvalidUriException(
				__error__("EC29 %s", __part));
		
		// There is no authority specified
		int fs = authPath.indexOf('/', 2);
		if (fs < 0 || fs == 2)
		{
			this.authority = null;
			this.path = authPath.substring(2);
		}
		
		// There is an authority
		else
		{
			this.authority = new UriAuthority(authPath.substring(2, fs));
			this.path = authPath.substring(fs);
		}
		
		// Path parameters
		if (pathParams == null)
		{
			this.rawPathParams = null;
			this._pathParams = null;
		}
		else
		{
			this.rawPathParams = UriPart.decode(pathParams);
			this._pathParams = UriPart.splitDecode(pathParams, ',');
		}
		
		// Query parameters
		if (queryParams == null)
		{
			this.rawQueryParams = null;
			this._queryParams = null;
		}
		else
		{
			this.rawQueryParams = UriPart.decode(queryParams);
			this._queryParams = UriPart.splitDecode(queryParams, '&');
		}
		
		// Fragment
		this.fragment = (fragment == null ? null :
			UriPart.decode(fragment));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public UriAuthority getAuthority()
	{
		return this.authority;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public String getFragment()
	{
		return this.fragment;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public String getPath()
	{
		return this.path;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public String pathParam(int __dx)
		throws IndexOutOfBoundsException
	{
		String[] params = this._pathParams;
		if (__dx < 0 || params == null || __dx >= params.length)
			throw new IndexOutOfBoundsException("IOOB");
		return params[__dx];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public int pathParamCount()
	{
		String[] params = this._pathParams;
		if (params == null)
			return 0;
		return params.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public String pathParams()
	{
		return this.rawPathParams;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public String queryParam(int __dx)
		throws IndexOutOfBoundsException
	{
		String[] params = this._queryParams;
		if (__dx < 0 || params == null || __dx >= params.length)
			throw new IndexOutOfBoundsException("IOOB");
		return params[__dx];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public int queryParamCount()
	{
		String[] params = this._queryParams;
		if (params == null)
			return 0;
		return params.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/29
	 */
	@Override
	public String queryParams()
	{
		return this.rawQueryParams;
	}
	
	/**
	 * Sets this URI with the same authority but a different absolute path.
	 *
	 * @param __path The path to use, this is treated as an absolute path.
	 * @return This URI with a different path.
	 * @throws InvalidUriException If the URI is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/01
	 */
	@SquirrelJMEVendorApi
	public UriGenericPart withPath(String __path)
		throws InvalidUriException, NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// Is an extra slash needed?
		String slash = (!__path.startsWith("/") ? "/" : "");
		
		// The authority is optional
		UriAuthority auth = this.authority;
		if (auth != null)
			return new UriGenericPart("//" + auth + slash + __path);
		return new UriGenericPart("//" + slash + __path);
	}
}
