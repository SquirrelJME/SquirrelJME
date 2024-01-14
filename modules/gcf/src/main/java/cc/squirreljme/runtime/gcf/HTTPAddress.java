// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.multiphasicapps.collections.EmptyList;
import net.multiphasicapps.collections.UnmodifiableList;
import org.intellij.lang.annotations.Language;

/**
 * This represents an IP address.
 *
 * @since 2019/05/06
 */
public final class HTTPAddress
	implements SocketAddress
{
	/** The IP Address. */
	public final IPAddress ipaddr;
	
	/** The file. */
	public final FileAddress file;
	
	/** The query. */
	public final String query;
	
	/** The fragment. */
	public final String fragment;
	
	/** Parameters which exist within the address. */
	public final List<String> parameters;
	
	/**
	 * Initializes the HTTP Address.
	 *
	 * @param __ip The IP address.
	 * @param __file The file.
	 * @param __parameters The parameters, there may be multiple ones.
	 * @param __query The query, may be {@code null}.
	 * @param __frag The fragment, may be {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public HTTPAddress(IPAddress __ip, FileAddress __file,
		List<String> __parameters, String __query,
		String __frag)
		throws NullPointerException
	{
		if (__ip == null || __file == null)
			throw new NullPointerException("NARG");
		
		// Defensive copy
		if (__parameters != null)
		{
			__parameters = new ArrayList<>(__parameters);
			for (String param : __parameters)
				if (param == null)
					throw new NullPointerException("NARG");
		}
		
		this.ipaddr = __ip;
		this.file = __file;
		this.query = __query;
		this.fragment = __frag;
		this.parameters = (__parameters == null ? EmptyList.<String>empty() :
			UnmodifiableList.of(__parameters));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String toString()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Decodes an address from the URI part.
	 *
	 * @param __p The part to decode from.
	 * @return The HTTP address.
	 * @throws IllegalArgumentException If the address is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static HTTPAddress fromUriPart(
		@Language("http-url-reference") String __p)
		throws IllegalArgumentException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Debug
		Debugging.debugNote("Decode %s", __p);
		
		/* {@squirreljme.error EC02 HTTP address must start with double
		slash. (The URI part)} */
		if (!__p.startsWith("//"))
			throw new IllegalArgumentException("EC02 " + __p);
		__p = __p.substring(2);
		
		// Only contains the host part
		int sl = __p.indexOf('/');
		if (sl < 0)
			return new HTTPAddress(IPAddress.of(__p), FileAddress.of("/"),
				null, null, null);
		
		// Parse host portion
		IPAddress ipaddr = IPAddress.of(__p.substring(0, sl));
		
		// Parse remaining part, but keep the slash
		__p = __p.substring(sl);
		
		// The file address can end at any of these points
		int nextParam = __p.indexOf(';');
		int nextQuery = __p.indexOf('?');
		int nextFrag = __p.indexOf('#');
	
		// Extract the file parameter part
		int endPos = (nextParam >= 0 ? nextParam :
			(nextQuery >= 0 ? nextQuery :
			(nextFrag >= 0 ? nextFrag : __p.length())));
		FileAddress fileAddress = FileAddress.of(__p.substring(0, endPos));
		
		// Move to the end position
		__p = __p.substring(endPos);
		
		// Parse parameter
		List<String> parameters;
		int el = __p.indexOf(';');
		if (el >= 0)
			throw Debugging.todo();
		
		// No parameter used
		else
			parameters = null;
		
		// Parse query
		String query;
		int ql = __p.indexOf('?');
		if (ql >= 0)
		{
			// Queries can end before a fragment
			int hashAt = __p.indexOf('#', ql + 1);
			
			query = HTTPUtils.stringDecode(
				HTTPUrlCharacterSet.QUERY_OR_FRAGMENT,
				(hashAt < 0 ? __p.substring(ql + 1) :
					__p.substring(ql + 1, hashAt)));
		}
		
		// No query used
		else
			query = null;
		
		// Parse fragment, fragments are always at the end
		String fragment;
		int fl = __p.indexOf('#');
		if (fl >= 0)
			fragment = HTTPUtils.stringDecode(
				HTTPUrlCharacterSet.QUERY_OR_FRAGMENT, __p.substring(fl + 1));
			
			// No fragment used
		else
			fragment = null;
		
		// Build remaining address
		return new HTTPAddress(ipaddr, fileAddress, parameters, query,
			fragment);
	}
	
	/**
	 * Decodes an address from the URI part or returns {@code null} if not
	 * valid.
	 *
	 * @param __part The part to decode from.
	 * @return The HTTP address or {@code null} if the address is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/10/11
	 */
	public static HTTPAddress fromUriPartUnchecked(String __part)
		throws NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return HTTPAddress.fromUriPart(__part);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			
			return null;
		}
	}
}

