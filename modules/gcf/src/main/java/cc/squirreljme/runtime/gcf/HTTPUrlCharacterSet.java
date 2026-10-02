// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents a character set within an HTTP URL.
 *
 * @since 2022/10/11
 */
@SquirrelJMEVendorApi
public enum HTTPUrlCharacterSet
{
	/** Host part of the address. */
	@SquirrelJMEVendorApi
	NET_LOCATOR,
	
	/** Path part of the address. */
	@SquirrelJMEVendorApi
	PATH,
	
	/** Query or parameters. */
	@SquirrelJMEVendorApi
	QUERY_OR_FRAGMENT,
	
	/** Unreserved characters. */
	@SquirrelJMEVendorApi
	HTTP_UNRESERVED,
	
	/** Reserved characters. */
	@SquirrelJMEVendorApi
	HTTP_RESERVED,
	
	/** Path characters. */
	@SquirrelJMEVendorApi
	HTTP_PCHAR,
	
	/** Uchar characters. */
	@SquirrelJMEVendorApi
	HTTP_UCHAR,
	
	/** Safe characters. */
	@SquirrelJMEVendorApi
	HTTP_SAFE,
	
	/** Extra characters. */
	@SquirrelJMEVendorApi
	HTTP_EXTRA,
	
	/** Alphanumeric characters. */
	@SquirrelJMEVendorApi
	HTTP_ALPHANUMERIC,
	
	/* End. */
	;
	
	/**
	 * Checks whether this character is valid or not.
	 * 
	 * @param __c The character to check.
	 * @return If this character is valid within the set.
	 * @since 2022/10/11
	 */
	@SquirrelJMEVendorApi
	public final boolean isValid(char __c)
	{
		switch (this)
		{
			case HTTP_ALPHANUMERIC:
				return (__c >= 'a' && __c <= 'z') ||
					(__c >= 'A' && __c <= 'Z') ||
					(__c >= '0' && __c <= '9');
				
			case HTTP_SAFE:
				return __c == '$' ||
					__c == '-' ||
					__c == '_' ||
					__c == '.';
			
			case HTTP_EXTRA:
				return __c == '!' ||
					__c == '*' ||
					__c == '\"' ||
					__c == '(' ||
					__c == ')' ||
					__c == ',';
				
			case HTTP_UNRESERVED:
				return HTTPUrlCharacterSet.HTTP_ALPHANUMERIC.isValid(__c) ||
					HTTPUrlCharacterSet.HTTP_SAFE.isValid(__c) ||
					HTTPUrlCharacterSet.HTTP_EXTRA.isValid(__c);
				
			case HTTP_RESERVED:
				return __c == ';' ||
					__c == '/' ||
					__c == '?' ||
					__c == ':' ||
					__c == '@' ||
					__c == '&' ||
					__c == '=' ||
					__c == '+';
				
			case HTTP_PCHAR:
				return HTTPUrlCharacterSet.HTTP_UCHAR.isValid(__c) ||
					__c == ':' ||
					__c == '@' ||
					__c == '&' ||
					__c == '=' ||
					__c == '+';
				
			case HTTP_UCHAR:
				return HTTPUrlCharacterSet.HTTP_UNRESERVED.isValid(__c);
				
			case NET_LOCATOR:
				return HTTPUrlCharacterSet.HTTP_PCHAR.isValid(__c) ||
					__c == ';' ||
					__c == '?';
				
			case PATH:
				return HTTPUrlCharacterSet.HTTP_PCHAR.isValid(__c);
				
			case QUERY_OR_FRAGMENT:
				return HTTPUrlCharacterSet.HTTP_UCHAR.isValid(__c) ||
					HTTPUrlCharacterSet.HTTP_RESERVED.isValid(__c);
					
			default:
				throw Debugging.oops();
		}
	}
}
