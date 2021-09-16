// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

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
public interface HttpConnection
	extends ContentConnection
{
	String CONNECT =
		"CONNECT";
	
	String DELETE =
		"DELETE";
	
	String GET =
		"GET";
	
	String HEAD =
		"HEAD";
	
	int HTTP_ACCEPTED =
		202;
	
	int HTTP_BAD_GATEWAY =
		502;
	
	int HTTP_BAD_METHOD =
		405;
	
	int HTTP_BAD_REQUEST =
		400;
	
	int HTTP_CLIENT_TIMEOUT =
		408;
	
	int HTTP_CONFLICT =
		409;
	
	int HTTP_CREATED =
		201;
	
	int HTTP_ENTITY_TOO_LARGE =
		413;
	
	int HTTP_EXPECT_FAILED =
		417;
	
	int HTTP_FORBIDDEN =
		403;
	
	int HTTP_GATEWAY_TIMEOUT =
		504;
	
	int HTTP_GONE =
		410;
	
	int HTTP_INTERNAL_ERROR =
		500;
	
	int HTTP_LENGTH_REQUIRED =
		411;
	
	int HTTP_MOVED_PERM =
		301;
	
	int HTTP_MOVED_TEMP =
		302;
	
	int HTTP_MULT_CHOICE =
		300;
	
	int HTTP_NOT_ACCEPTABLE =
		406;
	
	int HTTP_NOT_AUTHORITATIVE =
		203;
	
	int HTTP_NOT_FOUND =
		404;
	
	int HTTP_NOT_IMPLEMENTED =
		501;
	
	int HTTP_NOT_MODIFIED =
		304;
	
	int HTTP_NO_CONTENT =
		204;
	
	int HTTP_OK =
		200;
	
	int HTTP_PARTIAL =
		206;
	
	int HTTP_PAYMENT_REQUIRED =
		402;
	
	int HTTP_PRECON_FAILED =
		412;
	
	int HTTP_PROXY_AUTH =
		407;
	
	int HTTP_REQ_TOO_LONG =
		414;
	
	int HTTP_RESET =
		205;
	
	int HTTP_SEE_OTHER =
		303;
	
	int HTTP_TEMP_REDIRECT =
		307;
	
	int HTTP_UNAUTHORIZED =
		401;
	
	int HTTP_UNAVAILABLE =
		503;
	
	int HTTP_UNSUPPORTED_RANGE =
		416;
	
	int HTTP_UNSUPPORTED_TYPE =
		415;
	
	int HTTP_USE_PROXY =
		305;
	
	int HTTP_VERSION =
		505;
	
	String OPTIONS =
		"OPTIONS";
	
	String POST =
		"POST";
	
	String PUT =
		"PUT";
	
	String TRACE =
		"TRACE";
	
	AccessPoint getAccessPoint()
		throws IOException;
	
	long getDate()
		throws IOException;
	
	long getExpiration()
		throws IOException;
	
	String getFile();
	
	String getHeaderField(String __a)
		throws IOException;
	
	String getHeaderField(int __a)
		throws IOException;
	
	long getHeaderFieldDate(String __a, long __b)
		throws IOException;
	
	int getHeaderFieldInt(String __a, int __b)
		throws IOException;
	
	String getHeaderFieldKey(int __a)
		throws IOException;
	
	String getHost();
	
	long getLastModified()
		throws IOException;
	
	int getPort();
	
	String getProtocol();
	
	String getQuery();
	
	String getRef();
	
	String getRequestMethod();
	
	String getRequestProperty(String __a);
	
	int getResponseCode()
		throws IOException;
	
	String getResponseMessage()
		throws IOException;
	
	String getURL();
	
	/**
	 * Sets the request method to use.
	 *
	 * @param __m The method to use.
	 * @throws IOException If this is not in the setup phase.
	 * @throws NullPointerException If no method was specified.
	 * @since 2019/05/12
	 */
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
	void setRequestProperty(String __k, String __v)
		throws IOException;
}


