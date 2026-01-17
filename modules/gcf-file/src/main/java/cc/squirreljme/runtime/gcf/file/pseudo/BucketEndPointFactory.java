// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.pseudo;

import cc.squirreljme.jvm.mle.BucketShelf;
import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.jvm.mle.constants.StandardBucketType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.file.FileEndPoint;
import cc.squirreljme.runtime.gcf.file.FileEndPointFactory;
import cc.squirreljme.runtime.gcf.uri.UriAuthority;
import cc.squirreljme.runtime.gcf.uri.UriGenericPart;
import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;
import org.jetbrains.annotations.Nullable;

import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * Factory which makes {@link BucketEndPoint}.
 *
 * @since 2026/01/16
 */
@SquirrelJMEVendorApi
public class BucketEndPointFactory
	implements FileEndPointFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2026/01/16
	 */
	@Override
	public FileEndPoint connect(UriGenericPart __uri, int __mode,
		@Nullable UriGenericPart __dotDot)
		throws ConnectionNotFoundException, IOException, NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error GF0g Bucket connection has an improper host.
		(The URI)} */
		UriAuthority auth = __uri.getAuthority();
		String fullHost = auth.host();
		if (fullHost == null ||
			!fullHost.startsWith(BucketEndPoint.DECODED_HOST) ||
			fullHost.length() <= BucketEndPoint.DECODED_HOST.length())
			throw new ConnectionNotFoundException(
				__error__("GF0g %s", __uri));
		
		// The desired index/name, is everything at the end
		String desireName = fullHost.substring(
			BucketEndPoint.DECODED_HOST.length());
		
		// Which bucket to access?
		int type;
		switch (desireName)
		{
			case "0":
			case "data":
				type = StandardBucketType.DATA_BUCKET;
				break;
				
			case "1":
			case "libraries":
				type = StandardBucketType.LIBRARIES_BUCKET;
				break;
				
			case "2":
			case "extra":
				type = StandardBucketType.EXTRA_BUCKET;
				break;
			
			default:
				/* {@squirreljme.error GF0h Invalid bucket specified.
				(The URI)} */
				throw new ConnectionNotFoundException(
					__error__("GF0h %s", __uri));
		}
		
		// Get an instance of the bucket
		BucketBracket bracket;
		try
		{
			bracket = BucketShelf.bucket(type);
		}
		catch (MLECallError __e)
		{
			ConnectionNotFoundException toss =
				new ConnectionNotFoundException(__e.getMessage());
			toss.initCause(__e);
			throw toss;
		}
		
		// Setup target connection
		return new BucketEndPoint(__uri, __mode, __dotDot,
			bracket);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/16
	 */
	@Override
	public boolean handleAuthority(UriAuthority __auth)
		throws NullPointerException
	{
		if (__auth == null)
			throw new NullPointerException("NARG");
		
		// Ignore if no host was specified
		String host = __auth.host();
		if (host == null)
			return false;
		
		// Buckets may have the specific bucket specified following
		return host.startsWith(BucketEndPoint.DECODED_HOST);
	}
}
