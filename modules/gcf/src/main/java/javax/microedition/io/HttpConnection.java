// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;

/**
 * This is a connection which enables HTTP based API access.
 *
 * This connection is within three states:
 *  - SETUP where the connection is being setup.
 *  - CONNECTED where the connection has been made.
 *  - CLOSED where the connection has been completed and closed.
 *
 * The {@link #openOutputStream()} and {@link #openDataOutputStream()} may only
 * be called when in the SETUP state, when either of these streams are closed
 * the connection will transition to the connected state. Additionally if
 * the state transitions to the CONNECTED state for any reason then the stream
 * will be implicitly closed and its data will be used.
 *
 * @since 2019/05/12
 */
@Api
public interface HttpConnection
	extends ContentConnection
{
	@Api
	String CONNECT =
		"CONNECT";
	
	@Api
	String DELETE =
		"DELETE";
	
	@Api
	String GET =
		"GET";
	
	@Api
	String HEAD =
		"HEAD";
	
	@Api
	int HTTP_ACCEPTED =
		202;
	
	@Api
	int HTTP_BAD_GATEWAY =
		502;
	
	@Api
	int HTTP_BAD_METHOD =
		405;
	
	@Api
	int HTTP_BAD_REQUEST =
		400;
	
	@Api
	int HTTP_CLIENT_TIMEOUT =
		408;
	
	@Api
	int HTTP_CONFLICT =
		409;
	
	@Api
	int HTTP_CREATED =
		201;
	
	@Api
	int HTTP_ENTITY_TOO_LARGE =
		413;
	
	@Api
	int HTTP_EXPECT_FAILED =
		417;
	
	@Api
	int HTTP_FORBIDDEN =
		403;
	
	@Api
	int HTTP_GATEWAY_TIMEOUT =
		504;
	
	@Api
	int HTTP_GONE =
		410;
	
	@Api
	int HTTP_INTERNAL_ERROR =
		500;
	
	@Api
	int HTTP_LENGTH_REQUIRED =
		411;
	
	@Api
	int HTTP_MOVED_PERM =
		301;
	
	@Api
	int HTTP_MOVED_TEMP =
		302;
	
	@Api
	int HTTP_MULT_CHOICE =
		300;
	
	@Api
	int HTTP_NOT_ACCEPTABLE =
		406;
	
	@Api
	int HTTP_NOT_AUTHORITATIVE =
		203;
	
	@Api
	int HTTP_NOT_FOUND =
		404;
	
	@Api
	int HTTP_NOT_IMPLEMENTED =
		501;
	
	@Api
	int HTTP_NOT_MODIFIED =
		304;
	
	@Api
	int HTTP_NO_CONTENT =
		204;
	
	@Api
	int HTTP_OK =
		200;
	
	@Api
	int HTTP_PARTIAL =
		206;
	
	@Api
	int HTTP_PAYMENT_REQUIRED =
		402;
	
	@Api
	int HTTP_PRECON_FAILED =
		412;
	
	@Api
	int HTTP_PROXY_AUTH =
		407;
	
	@Api
	int HTTP_REQ_TOO_LONG =
		414;
	
	@Api
	int HTTP_RESET =
		205;
	
	@Api
	int HTTP_SEE_OTHER =
		303;
	
	@Api
	int HTTP_TEMP_REDIRECT =
		307;
	
	@Api
	int HTTP_UNAUTHORIZED =
		401;
	
	@Api
	int HTTP_UNAVAILABLE =
		503;
	
	@Api
	int HTTP_UNSUPPORTED_RANGE =
		416;
	
	@Api
	int HTTP_UNSUPPORTED_TYPE =
		415;
	
	@Api
	int HTTP_USE_PROXY =
		305;
	
	@Api
	int HTTP_VERSION =
		505;
	
	@Api
	String OPTIONS =
		"OPTIONS";
	
	@Api
	String POST =
		"POST";
	
	@Api
	String PUT =
		"PUT";
	
	@Api
	String TRACE =
		"TRACE";
	
	@Api
	AccessPoint getAccessPoint()
		throws IOException;
	
	@Api
	long getDate()
		throws IOException;
	
	@Api
	long getExpiration()
		throws IOException;
	
	@Api
	String getFile();
	
	@Api
	String getHeaderField(String __a)
		throws IOException;
	
	@Api
	String getHeaderField(int __a)
		throws IOException;
	
	@Api
	long getHeaderFieldDate(String __a, long __b)
		throws IOException;
	
	@Api
	int getHeaderFieldInt(String __a, int __b)
		throws IOException;
	
	@Api
	String getHeaderFieldKey(int __a)
		throws IOException;
	
	@Api
	String getHost();
	
	@Api
	long getLastModified()
		throws IOException;
	
	@Api
	int getPort();
	
	@Api
	String getProtocol();
	
	@Api
	String getQuery();
	
	@Api
	String getRef();
	
	@Api
	String getRequestMethod();
	
	@Api
	String getRequestProperty(String __a);
	
	@Api
	int getResponseCode()
		throws IOException;
	
	@Api
	String getResponseMessage()
		throws IOException;
	
	@Api
	String getURL();
	
	/**
	 * Sets the request method to use.
	 *
	 * @param __m The method to use.
	 * @throws IOException If this is not in the setup phase.
	 * @throws NullPointerException If no method was specified.
	 * @since 2019/05/12
	 */
	@Api
	void setRequestMethod(String __m)
		throws IOException, NullPointerException;
	
	/**
	 * Adds or replaces an existing request property, note that for multiple
	 * request property specifications they need to manually be comma
	 * separated.
	 *
	 * @param __k The request header key.
	 * @param __v The value to use, {@code null} clears.
	 * @throws IOException If this is not in the setup phase.
	 * @throws NullPointerException If the key was null.
	 * @since 2019/05/12
	 */
	@Api
	void setRequestProperty(String __k, String __v)
		throws IOException;
}


