// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.multivm.AlwaysFalse;
import cc.squirreljme.plugin.util.FossilExe;
import cc.squirreljme.plugin.util.NoteCalendarGenerator;
import cc.squirreljme.plugin.util.SimpleHTTPProtocolException;
import cc.squirreljme.plugin.util.SimpleHTTPRequest;
import cc.squirreljme.plugin.util.SimpleHTTPResponse;
import cc.squirreljme.plugin.util.SimpleHTTPResponseBuilder;
import cc.squirreljme.plugin.util.SimpleHTTPServer;
import cc.squirreljme.plugin.util.SimpleHTTPStatus;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
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
		this.onlyIf(__task -> FossilExe.isAvailable(true));
		this.getOutputs().upToDateWhen(new AlwaysFalse());
		
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
		LocalDateTime now = LocalDateTime.now();
		String filePath = this.__blogFilePath(now.toLocalDate());
		__DeveloperNoteSession__ session = new __DeveloperNoteSession__(
			filePath);
		
		// Load pre-existing blog
		FossilExe exe = FossilExe.instance();
		byte[] content = exe.unversionCatBytes(filePath);
		boolean doCreate = (content == null);
		if (doCreate)
			content = DeveloperNoteTask.__template(now);
		session._content = content;
		
		// Open server
		try (SimpleHTTPServer<__DeveloperNoteSession__> server =
			new SimpleHTTPServer<>(session))
		{
			// Note on where to get it
			String url = String.format("http://%s:%d/", server.hostname,
				server.port);
			__task.getLogger().lifecycle("Editing " + filePath);
			__task.getLogger().lifecycle("Server opened at " + url);
			
			// Launch a web browser
			DeveloperNoteTask.__launchBrowser(__task, url);
			
			// Continuous handling loop
			for (;;)
				try
				{
					// Stop the loop if we were interrupted
					if (Thread.interrupted())
						break;
					
					// Otherwise wait for another packet
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
			e.printStackTrace();
			throw new RuntimeException("Server read/write error.", e);
		}
		
		// Store the note in the un-versioned space, but only if saved
		if (session._saveCount > 0)
			exe.unversionedStoreBytes(filePath, session._content);
		
		// Recreate the calendar
		if (doCreate)
			try
			{
				NoteCalendarGenerator.generateAndStore(exe);
			}
			catch (IOException e)
			{
				throw new RuntimeException("Could not generate calendar.", e);
			}
	}
	
	/**
	 * Returns the blog file path.
	 * 
	 * @return The path to the blog file.
	 * @since 2020/06/27
	 */
	private String __blogFilePath()
	{
		return this.__blogFilePath(LocalDate.now());
	}
	
	/**
	 * Returns the blog file path.
	 * 
	 * @param __date The date to get.
	 * @return The path to the blog file.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	private String __blogFilePath(LocalDate __date)
		throws NullPointerException
	{
		return this.__blogFilePath(__date,
			FossilExe.instance().currentUser());
	}
	
	/**
	 * Returns the blog file path.
	 * 
	 * @param __date The date to get.
	 * @param __user The user to lookup.
	 * @return The path to the blog file.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	private String __blogFilePath(LocalDate __date, String __user)
		throws NullPointerException
	{
		if (__date == null || __user == null)
			throw new NullPointerException("NARG");
		
		// Determine where this is
		return String.format("developer-notes/%s/%tY/%tm/%td.mkd",
			__user.replace('.', '-'), __date, __date, __date);
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
	@SuppressWarnings("FeatureEnvy")
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
		String query;
		try
		{
			query = URLDecoder.decode(Objects.toString(
				__request.path.getRawQuery(), ""), "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("Could not decode query", e);
		}
		
		// Form content?
		if (query != null && query.startsWith("content="))
		{
			// Split off the data
			String data = query.substring("content=".length())
				.replace("\r\n", "\n");
			
			// Store into the session bytes
			__session._content = data.getBytes(StandardCharsets.UTF_8);
			__session._saveCount++;
			
			// Note it down
			System.out.println("Notes saved!");
		}
		
		// All done?
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
	
	/**
	 * Attempts to launch a browser.
	 * 
	 * @param __task The task used.
	 * @param __url The URL to launch.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	private static void __launchBrowser(Task __task, String __url)
		throws NullPointerException
	{
		if (__url == null)
			throw new NullPointerException("NARG");
		
		// Inform on the terminal what the URL is for the server
		__task.getLogger().lifecycle(String.format(
			"Notes URL is at %s !", __url));
		
		// Try to use normal AWT stuff?
		try
		{
			// Not a supported desktop?
			if (!Desktop.isDesktopSupported())
				throw new HeadlessException();
			
			// Open browser, or try to anyway
			Desktop desktop = Desktop.getDesktop();
			desktop.browse(URI.create(__url));
			
			// It worked, so stop
			return;
		}
		
		// Do not fail on this, just try more
		catch (IOException|HeadlessException ignored)
		{
		}
		
		// Open the browser using another means
		System.err.printf("TODO -- Open browser?%n");
	}
	
	/**
	 * Returns template for blank notes.
	 * 
	 * @param __at The date.
	 * @return The note template.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	@SuppressWarnings("resource")
	private static byte[] __template(LocalDateTime __at)
		throws NullPointerException
	{
		if (__at == null)
			throw new NullPointerException("NARG");
		
		// Write the template
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(
				out, true, "utf-8"))
		{
			// Header
			ps.printf("# %tY/%tm/%td\n", __at, __at, __at);
			ps.print("\n");
			
			// Current time
			ps.printf("## %tH:%tM", __at, __at);
			ps.print("\n");
			
			// Ending space line for content
			ps.print("\n");
			
			// Clear out and use this template
			ps.flush();
			return out.toByteArray();
		}
		
		// Failed to write
		catch (IOException e)
		{
			throw new RuntimeException("Could not create template", e);
		}
	}
}
