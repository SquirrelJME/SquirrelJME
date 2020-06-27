// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.util.FossilExe;
import cc.squirreljme.plugin.util.SimpleHTTPProtocolException;
import cc.squirreljme.plugin.util.SimpleHTTPRequest;
import cc.squirreljme.plugin.util.SimpleHTTPResponse;
import cc.squirreljme.plugin.util.SimpleHTTPResponseBuilder;
import cc.squirreljme.plugin.util.SimpleHTTPServer;
import cc.squirreljme.plugin.util.SimpleHTTPStatus;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;

/**
 * Edits the current day in developer notes.
 *
 * @since 2020/06/26
 */
public class DeveloperNoteTask
	extends DefaultTask
{
	/** The server only handles this single path. */
	private static final String _THE_ONLY_PATH =
		"/";
	
	/**
	 * Initializes the task.
	 * 
	 * @param __exeTask The executable task.
	 * @since 2020/06/26
	 */
	@Inject
	public DeveloperNoteTask(FossilExeTask __exeTask)
	{
		// Set details of this task
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Edits the developer note for the current day.");
		
		// The executable task must run first
		this.dependsOn(__exeTask);
		
		// Fossil must exist
		this.onlyIf(__task -> FossilExe.isAvailable());
		
		// Action to perform
		this.doLast(this::action);
	}
	
	/**
	 * Performs the task action.
	 * 
	 * @param __task The called task.
	 * @since 2020/06/26
	 */
	private void action(Task __task)
	{
		// Setup session
		__DeveloperNoteSession__ session = new __DeveloperNoteSession__();
		
		// Load pre-existing blog
		System.err.println("TODO -- Load pre-existing blog.");
		session._content = "TODO -- Load?".getBytes(StandardCharsets.UTF_8);
		
		// Open server
		try (SimpleHTTPServer<__DeveloperNoteSession__> server =
			new SimpleHTTPServer<>(session))
		{
			// Note on where to get it
			__task.getLogger().lifecycle(String.format(
				"Server opened at http://%s:%d/", server.hostname,
				server.port));
			
			// Continuous handling loop
			for (;;)
				try
				{
					if (!server.next(this::__httpHandler))
						break;
				}
				catch (SimpleHTTPProtocolException e)
				{
					e.printStackTrace();
				}
		}
		
		// Problem with the server
		catch (IOException e)
		{
			throw new RuntimeException("Server read/write error.", e);
		}
		
		// Store the note in the unversioned space
		if (true)
			throw new Error("TODO");
		
		throw new Error("TODO");
	}
	
	/**
	 * Handles the HTTP request.
	 * 
	 * @param __session The session being used.
	 * @param __request The request.
	 * @return The response, may be {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/26
	 */
	@SuppressWarnings("MagicNumber")
	private SimpleHTTPResponse __httpHandler(
		__DeveloperNoteSession__ __session, SimpleHTTPRequest __request)
		throws NullPointerException
	{
		if (__session == null || __request == null)
			throw new NullPointerException("NARG");
		
		// Setup base response, always returning the same path
		SimpleHTTPResponseBuilder response = new SimpleHTTPResponseBuilder();
		
		// If we got a request that is not from the root, it is somewhere else
		if (!DeveloperNoteTask._THE_ONLY_PATH.equals(
			__request.path.getPath()))
			return response.status(SimpleHTTPStatus.NOT_FOUND).build();
		
		// Submission of the form? Load in that data
		String query = __request.path.getQuery();
		if (query != null && query.startsWith("content="))
		{
			// Split off the data
			String data = query.substring("content=".length());
			
			// Store into the session bytes
			__session._content = data.getBytes(StandardCharsets.UTF_8);
		}
		
		// All done!
		else if (query != null && query.startsWith("finish="))
			return null;
		
		// Everything is okay
		response.status(SimpleHTTPStatus.OK);
		
		// Setup some headers
		response.addHeader("Content-Type",
			"text/html; charset=UTF-8");
		response.addHeader("Server",
			"SquirrelJME-Build/0.3.0");
		response.addHeader("Connection",
			"close");
		
		// Splice in resources
		response.spliceResources(DeveloperNoteTask.class,
			"form-header.html", "form-footer.html",
			__session._content);
		
		// Build final response
		return response.build();
	}
}
