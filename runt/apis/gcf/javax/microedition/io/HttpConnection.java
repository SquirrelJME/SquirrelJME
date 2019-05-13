// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
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
 * The {@link openOutputStream} and {@link openDataOutputStream} may only be
 * called when in the SETUP state, when either of these streams are closed
 * the connection will transition to the connected state. Additionally if
 * the state transitions to the CONNECTED state for any reason then the stream
 * will be implicitly closed and its data will be used.
 *
 * @since 2019/05/12
 */
public interface HttpConnection
	extends ContentConnection
{
	public static final String CONNECT =
		"CONNECT";
	
	public static final String DELETE =
		"DELETE";
	
	public static final String GET =
		"GET";
	
	public static final String HEAD =
		"HEAD";
	
	public static final int HTTP_ACCEPTED =
		202;
	
	public static final int HTTP_BAD_GATEWAY =
		502;
	
	public static final int HTTP_BAD_METHOD =
		405;
	
	public static final int HTTP_BAD_REQUEST =
		400;
	
	public static final int HTTP_CLIENT_TIMEOUT =
		408;
	
	public static final int HTTP_CONFLICT =
		409;
	
	public static final int HTTP_CREATED =
		201;
	
	public static final int HTTP_ENTITY_TOO_LARGE =
		413;
	
	public static final int HTTP_EXPECT_FAILED =
		417;
	
	public static final int HTTP_FORBIDDEN =
		403;
	
	public static final int HTTP_GATEWAY_TIMEOUT =
		504;
	
	public static final int HTTP_GONE =
		410;
	
	public static final int HTTP_INTERNAL_ERROR =
		500;
	
	public static final int HTTP_LENGTH_REQUIRED =
		411;
	
	public static final int HTTP_MOVED_PERM =
		301;
	
	public static final int HTTP_MOVED_TEMP =
		302;
	
	public static final int HTTP_MULT_CHOICE =
		300;
	
	public static final int HTTP_NOT_ACCEPTABLE =
		406;
	
	public static final int HTTP_NOT_AUTHORITATIVE =
		203;
	
	public static final int HTTP_NOT_FOUND =
		404;
	
	public static final int HTTP_NOT_IMPLEMENTED =
		501;
	
	public static final int HTTP_NOT_MODIFIED =
		304;
	
	public static final int HTTP_NO_CONTENT =
		204;
	
	public static final int HTTP_OK =
		200;
	
	public static final int HTTP_PARTIAL =
		206;
	
	public static final int HTTP_PAYMENT_REQUIRED =
		402;
	
	public static final int HTTP_PRECON_FAILED =
		412;
	
	public static final int HTTP_PROXY_AUTH =
		407;
	
	public static final int HTTP_REQ_TOO_LONG =
		414;
	
	public static final int HTTP_RESET =
		205;
	
	public static final int HTTP_SEE_OTHER =
		303;
	
	public static final int HTTP_TEMP_REDIRECT =
		307;
	
	public static final int HTTP_UNAUTHORIZED =
		401;
	
	public static final int HTTP_UNAVAILABLE =
		503;
	
	public static final int HTTP_UNSUPPORTED_RANGE =
		416;
	
	public static final int HTTP_UNSUPPORTED_TYPE =
		415;
	
	public static final int HTTP_USE_PROXY =
		305;
	
	public static final int HTTP_VERSION =
		505;
	
	public static final String OPTIONS =
		"OPTIONS";
	
	public static final String POST =
		"POST";
	
	public static final String PUT =
		"PUT";
	
	public static final String TRACE =
		"TRACE";
	
	public abstract AccessPoint getAccessPoint()
		throws IOException;
	
	public abstract long getDate()
		throws IOException;
	
	public abstract long getExpiration()
		throws IOException;
	
	public abstract String getFile();
	
	public abstract String getHeaderField(String __a)
		throws IOException;
	
	public abstract String getHeaderField(int __a)
		throws IOException;
	
	public abstract long getHeaderFieldDate(String __a, long __b)
		throws IOException;
	
	public abstract int getHeaderFieldInt(String __a, int __b)
		throws IOException;
	
	public abstract String getHeaderFieldKey(int __a)
		throws IOException;
	
	public abstract String getHost();
	
	public abstract long getLastModified()
		throws IOException;
	
	public abstract int getPort();
	
	public abstract String getProtocol();
	
	public abstract String getQuery();
	
	public abstract String getRef();
	
	public abstract String getRequestMethod();
	
	public abstract String getRequestProperty(String __a);
	
	public abstract int getResponseCode()
		throws IOException;
	
	public abstract String getResponseMessage()
		throws IOException;
	
	public abstract String getURL();
	
	/**
	 * Sets the request method to use.
	 *
	 * @param __m The method to use.
	 * @throws IOException If this is not in the setup phase.
	 * @throws NullPointerException If no method was specified.
	 * @since 2019/05/12
	 */
	public abstract void setRequestMethod(String __m)
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
	public abstract void setRequestProperty(String __k, String __v)
		throws IOException;
}


