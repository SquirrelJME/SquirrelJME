// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.util.FossilExe;
import cc.squirreljme.plugin.util.NoteCalendarGenerator;
import cc.squirreljme.plugin.util.SimpleHTTPProtocolException;
import cc.squirreljme.plugin.util.SimpleHTTPServer;
import java.io.IOException;
import java.time.LocalDateTime;
import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * Task action for {@link DeveloperNoteTask}.
 *
 * @since 2022/07/10
 */
class DeveloperNoteTaskAction
	implements Action<Task>
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/07/10
	 */
	@SuppressWarnings("HttpUrlsUsage")
	@Override
	public void execute(Task __task)
	{
		// Setup session
		LocalDateTime now = LocalDateTime.now();
		String filePath = DeveloperNoteTask.__blogFilePath(now.toLocalDate());
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
		 new SimpleHTTPServer<>(
			session))
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
					
					// Otherwise, wait for another packet
					if (!server.next(DeveloperNoteTask::__httpHandler))
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
			exe.unversionStoreBytes(filePath, session._content);
		
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
}
