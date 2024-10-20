// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.springcoat;

import cc.squirreljme.jvm.aot.AOTSettings;
import cc.squirreljme.jvm.aot.Backend;
import cc.squirreljme.jvm.aot.CompileSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.jvm.aot.RomSettings;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.zip.queue.ArchiveOutputQueue;
import cc.squirreljme.jvm.suite.SuiteUtils;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * Backend for SpringCoat operations.
 * 
 * {@squirreljme.error AI01 This operation is not available.}
 *
 * @since 2021/08/21
 */
public class SpringCoatBackend
	implements Backend
{
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public void compileClass(CompileSettings __settings, LinkGlob __glob,
		String __name, InputStream __in)
		throws IOException, NullPointerException
	{
		throw new IllegalArgumentException("AI01");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void compileResource(CompileSettings __settings, LinkGlob __glob,
		String __path, InputStream __in)
		throws IOException, NullPointerException
	{
		throw new IllegalArgumentException("AI01");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public void dumpGlob(AOTSettings __inGlob, byte[] __name, PrintStream __out)
		throws IOException, NullPointerException
	{
		throw new IllegalArgumentException("AI01");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public LinkGlob linkGlob(AOTSettings __aotSettings,
		CompileSettings __compileSettings,
		OutputStream __out)
		throws IOException, NullPointerException
	{
		throw new IllegalArgumentException("AI01");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public String name()
	{
		return "springcoat";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/08/21
	 */
	@Override
	public void rom(AOTSettings __aotSettings, RomSettings __settings,
		OutputStream __out, VMClassLibrary... __libs)
		throws IOException, NullPointerException
	{
		if (__settings == null || __out == null || __libs == null)
			throw new NullPointerException("NARG");
		
		// Just use a special ZIP file
		byte[] buf = StreamUtils.buffer(null);
		try (ZipStreamWriter zip = new ZipStreamWriter(__out);
			 ArchiveOutputQueue queue = new ArchiveOutputQueue(zip))
		{
			// Fossil commit
			if (__aotSettings.commitFossil != null &&
				!__aotSettings.commitFossil.isEmpty())
				try (OutputStream commitRaw = queue.nextEntry(
					"SQUIRRELJME.SQC/commit.fossil");
					DataOutputStream commit = new DataOutputStream(
						commitRaw))
				{
					commit.writeUTF(__aotSettings.commitFossil);
				}
			
			// Git commit
			if (__aotSettings.commitGit != null &&
				!__aotSettings.commitGit.isEmpty())
				try (OutputStream commitRaw = queue.nextEntry(
					"SQUIRRELJME.SQC/commit.git");
					DataOutputStream commit = new DataOutputStream(
						commitRaw))
				{
					commit.writeUTF(__aotSettings.commitGit);
				}
			
			// Launcher properties
			if (__settings.launcherMainClass != null &&
				!__settings.launcherMainClass.isEmpty())
				try (OutputStream launcherRaw = queue.nextEntry(
					"SQUIRRELJME.SQC/launcher.main");
					DataOutputStream launcher = new DataOutputStream(
						launcherRaw))
				{
					launcher.writeUTF(__settings.launcherMainClass);
				}
			
			if (__settings.launcherArgs != null &&
				!__settings.launcherArgs.isEmpty())
				try (OutputStream launcherRaw = queue.nextEntry(
					"SQUIRRELJME.SQC/launcher.args");
					DataOutputStream launcher = new DataOutputStream(
						launcherRaw))
				{
					launcher.writeInt(__settings.launcherArgs.size());
					for (String arg : __settings.launcherArgs)
						launcher.writeUTF(arg);
				}
			
			if (__settings.launcherClassPath != null &&
				!__settings.launcherClassPath.isEmpty())
				try (OutputStream launcherRaw = queue.nextEntry(
					"SQUIRRELJME.SQC/launcher.path");
					DataOutputStream launcher = new DataOutputStream(
						launcherRaw))
				{
					launcher.writeInt(__settings.launcherClassPath.size());
					for (Integer arg : __settings.launcherClassPath)
						launcher.writeInt(arg);
				}
			
			// Setup queue for SQC output
			List<String> suites = new ArrayList<>();
			
			// Copy each library individually
			for (VMClassLibrary lib : __libs)
			{
				String libName = lib.name();
				
				// Only care about the base name
				int lastSlash = libName.lastIndexOf('/');
				if (lastSlash >= 0)
					libName = libName.substring(lastSlash + 1);
				
				// If it does not end in Jar, make it so
				if (!SuiteUtils.isJar(libName))
					libName = libName + ".jar";
				
				// Store suite
				suites.add(libName);
				
				// Base name for everything within
				String outBase = "SQUIRRELJME.SQC/" + libName + "/";
				
				// Setup resource list
				List<String> rcList = new ArrayList<>();
				
				// Copy all entries over
				for (String rcName : lib.listResources())
				{
					// Ignore directories
					if (rcName.endsWith("/"))
						continue;
					
					// Record name of entry
					rcList.add(rcName);
					
					// Copy
					try (InputStream libIn = lib.resourceAsStream(rcName);
						 OutputStream zipCopy = queue.nextEntry(
							 outBase + rcName))
					{
						StreamUtils.copy(libIn, zipCopy, buf);
					}
				}
				
				// Finish resource list
				try (OutputStream launcherRaw = queue.nextEntry(
					outBase +
						"META-INF/squirreljme/resources.list");
					DataOutputStream launcher = new DataOutputStream(
						launcherRaw))
				{
					launcher.writeInt(rcList.size());
					for (String arg : rcList)
						launcher.writeUTF(arg);
				}
			}
			
			// Finish the suite list
			try (OutputStream launcherRaw = queue.nextEntry(
				"SQUIRRELJME.SQC/suites.list");
				DataOutputStream launcher = new DataOutputStream(launcherRaw))
			{
				launcher.writeInt(suites.size());
				for (String arg : suites)
					launcher.writeUTF(arg);
			}
		}
		
		// Make sure it is all written
		__out.flush();
	}
}
