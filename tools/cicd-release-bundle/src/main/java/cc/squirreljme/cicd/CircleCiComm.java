// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.cicd;

import cc.squirreljme.cicd.circleci.CircleCiJobArtifacts;
import cc.squirreljme.cicd.circleci.CircleCiWorkflowJobs;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

/**
 * CircleCi communication.
 *
 * @since 2024/10/05
 */
public class CircleCiComm
{
	/** The root for API communications. */
	public static final String API_ROOT =
		"https://circleci.com/api/v2";
	
	/**
	 * Calls the given API.
	 *
	 * @param <T> The type to read as.
	 * @param __type The type to read as.
	 * @param __method The HTTP method.
	 * @param __path The path to send a request to.
	 * @return The resultant value of the request.
	 * @throws IOException On read/write errors, or request errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/06
	 */
	public static <T> T api(Class<T> __type, String __method, String... __path)
		throws IOException, NullPointerException
	{
		if (__type == null || __method == null || __path == null)
			throw new NullPointerException("NARG");
		
		// Setup call to server
		HttpURLConnection http =
			(HttpURLConnection)CircleCiComm.combine(__path)
				.toURL().openConnection();
		http.setRequestMethod(__method);
		http.addRequestProperty("Accept", "application/json");
		http.addRequestProperty("Content-Type", "application/json");
		/*http.addRequestProperty("Circle-Token",
			System.getenv("CIRCLE_TOKEN"));*/
		
		// Connect to read the result data
		byte[] result;
		try
		{
			// Connect to the server
			http.connect();
			
			// Read in all the data
			try (InputStream in = http.getInputStream())
			{
				result = StreamUtils.readAll(1048576, in);
			}
		}
		finally
		{
			http.disconnect();
		}
		
		// Failed?
		if ((http.getResponseCode() / 100) != 2)
			throw new IOException("HTTP " + http.getResponseCode());
		
		// Parse JSON
		return new ObjectMapper().readValue(result, __type);
	}
	
	/**
	 * Combines all the path elements.
	 *
	 * @param __path The input paths.
	 * @return The resultant final combined path.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/06
	 */
	public static URI combine(String... __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// Build path
		StringBuilder sb = new StringBuilder(CircleCiComm.API_ROOT);
		for (String path : __path)
		{
			if (sb == null)
				throw new NullPointerException("NARG");
			
			if (sb.length() > 0)
				sb.append("/");
			sb.append(path);
		}
		
		// Return resultant path
		return URI.create(sb.toString());
	}
	
	/**
	 * Returns the artifacts in a job.
	 *
	 * @param __jobNumber The job number.
	 * @return The artifacts in this job.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/06
	 */
	public static CircleCiJobArtifacts jobArtifacts(int __jobNumber)
		throws IOException, NullPointerException
	{
		return CircleCiComm.api(CircleCiJobArtifacts.class,
			"GET", "project",
			"github%2FSquirrelJME%2FSquirrelJME",
			Integer.toString(__jobNumber), "artifacts");
	}
	
	/**
	 * Gets the workflow jobs.
	 *
	 * @param __id The workflow ID.
	 * @return The jobs under the workflow.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/10/06
	 */
	public static CircleCiWorkflowJobs workflowJobs(String __id)
		throws IOException, NullPointerException
	{
		if (__id == null)
			throw new NullPointerException("NARG");
		
		return CircleCiComm.api(CircleCiWorkflowJobs.class,
			"GET", "workflow", __id, "job");
	}
}
