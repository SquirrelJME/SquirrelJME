// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.system;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.build.projects.Project;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.build.projects.ProjectName;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.squirreljme.jit.arch.mips.MIPSConfig;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigKey;
import net.multiphasicapps.squirreljme.jit.JITConfigValue;
import net.multiphasicapps.squirreljme.jit.LinkTable;

/**
 * This is used to build a target executable for compilation.
 *
 * @since 2017/05/29
 */
public class TargetBuilder
{
	/** Projects to be added to the project set for inclusion. */
	private static final JavaManifestKey _ADD_PROJECTS =
		new JavaManifestKey("add-projects");
	
	/** The manager for projects. */
	protected final ProjectManager manager;
	
	/** The target link table which contains the binary linkages. */
	protected final LinkTable linktable =
		new LinkTable();
	
	/** The JIT configuration (arch dependent). */
	protected final JITConfig jitconfig;
	
	/**
	 * Initializes the target builder.
	 *
	 * @param __pm The project manager.
	 * @param __templates The template file(s) to load.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/29
	 */
	public TargetBuilder(ProjectManager __pm, String... __templates)
		throws IOException, NullPointerException
	{
		// Check
		if (__pm == null || __templates == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.manager = __pm;
		
		// Parse the minimal template which is always included
		Map<JITConfigKey, JITConfigValue> jitopts = new HashMap<>();
		Set<ProjectName> extraproj = new HashSet<>();
		__parse(jitopts, extraproj, "minimal");
		
		// Parse template files
		for (String t : __templates)
			__parse(jitopts, extraproj, t);
		
		// Debug
		System.err.printf("DEBUG -- JIT Options: %s, Proj: %s%n", jitopts,
			extraproj);
		
		// {@squirreljme.error AO07 No CPU architecture has been specified,
		// compilation cannot continue.}
		JITConfigValue arch = jitopts.get(new JITConfigKey("cpu.arch"));
		if (arch == null)
			throw new IllegalArgumentException("AO07");
		
		// Depends on the architecture
		JITConfig jc;
		switch (arch.toString())
		{
				// MIPS
			case "mips":
				jc = new MIPSConfig(jitopts);
				break;
			
				// {@squirreljme.error AO08 Unknown architecture specified
				// which is not supported. (The architecture)}
			default:
				throw new IllegalArgumentException(String.format("AO08 %s",
					arch));
		}
		this.jitconfig = jc;
	}
	
	/**
	 * Generates the output for the given target to the specified output
	 * stream.
	 *
	 * @param __os The stream to write the executable to.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/29
	 */
	public void run(OutputStream __os)
		throws IOException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses the specified template file and loads the manifest keys into
	 * the option map.
	 *
	 * @param __opt The target options for the JIT.
	 * @param __pn The names of projects to use for compilation.
	 * @param __in The input file to parse.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/29
	 */
	private void __parse(Map<JITConfigKey, JITConfigValue> __opt,
		Set<ProjectName> __pn, String __in)
		throws IOException, NullPointerException
	{
		// Check
		if (__opt == null || __pn == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Read input resource as a manifest
		try (InputStream is = TargetBuilder.class.getResourceAsStream(
			"template/" + __in))
		{
			// {@squirreljme.error AO06 The specified template does not
			// exist.}
			if (is == null)
				throw new IOException(String.format("AO06 %s", __in));
			
			// Parse the manifest
			JavaManifest man = new JavaManifest(is);
			for (Map.Entry<JavaManifestKey, String> e :
				man.getMainAttributes().entrySet())
			{
				JITConfigKey jk = new JITConfigKey(e.getKey().toString());
				String jks = jk.toString(),
					v = e.getValue();
				
				// Debug
				System.err.printf("DEBUG -- Parse %s: %s (%s)%n", jk, v,
					man.getMainAttributes().get(jk));
				System.err.printf("DEBUG -- %s `%s` ?= `%s`%n",
					jk.equals(_ADD_PROJECTS), jk, _ADD_PROJECTS);
				
				// JIT option?
				if (jks.startsWith("jit-"))
					__opt.put(
						new JITConfigKey(jks.substring(4).replace('-', '.')),
						new JITConfigValue(v));
				
				// Add projects to set?
				else if (jk.equals(_ADD_PROJECTS))
				{System.err.println("DEBUG -- Adding projects!");
					// Projects appear in a space split list
					for (int i = 0, n = v.length(); i < n; i++)
					{
						// Ignore spaces
						char c = v.charAt(i);
						if (c == ' ')
							continue;
						
						// Find last space
						int s = v.indexOf(' ', i);
						if (s < 0)
							s = n;
						
						// Add project
						__pn.add(new ProjectName(v.substring(i, s)));
						i = s;
					}
				}
			}
		}
	}
}

