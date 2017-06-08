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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.build.projects.Project;
import net.multiphasicapps.squirreljme.build.projects.ProjectBinary;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.build.projects.ProjectName;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.squirreljme.jit.arch.mips.MIPSConfig;
import net.multiphasicapps.squirreljme.jit.ClusterIdentifier;
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
	
	/** The projects to be compiled, in the specified array order. */
	private final ProjectBinary[] _binaries;
	
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
		
		// Obtain set of projects to compile in a given order
		this._binaries = __getBinaries(extraproj);
		
		// Debug
		System.err.printf("DEBUG -- Build order: %s%n",
			Arrays.asList(this._binaries));
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
		
		// Used for cluster counting and progress
		LinkTable linktable = this.linktable;
		JITConfig jitconfig = this.jitconfig;
		ProjectBinary[] binaries = this._binaries;
		int cluster = 0,
			numclusters = binaries.length;
		
		// Go through all binary projects and compile them
		for (ProjectBinary pb : binaries)
		{
			// {@squirreljme.error AO09 Compiling the specified project. (The
			// project name; The current cluster; The number of clusters)}
			System.out.printf("AO09 %s %d %d%n", pb.name(), ++cluster,
				numclusters);
			
			// Setup cluster
			ClusterIdentifier ci = new ClusterIdentifier(cluster);
			
			// Process all classes and resources
			try (FileDirectory fd = pb.openFileDirectory())
			{
				int didfiles = 1;
				for (String fn : fd)
				{
					// {@squirreljme.error AO0a The specified class or resource
					// is being compiled. (The current file; The number of
					// processed files)}
					System.out.printf("AO0a %s %d%n", fn, didfiles++);
					
					// Open data stream
					try (InputStream is = fd.open(fn))
					{
						Runnable t;
						
						// Class file?
						if (fn.endsWith(".class"))
							t = jitconfig.compileClass(is, ci, linktable);
					
						// Otherwise a resource
						else
							t = jitconfig.compileResource(is, fn, ci,
								linktable);
						
						// Run the task
						t.run();
					}
				}
			}
			
			throw new todo.TODO();
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * Obtains an array containing the compilation order of most dependend on
	 * to least depended on.
	 *
	 * @param __pn The projects to be compiled.
	 * @return The array of projects to be included in their compilation and
	 * link order depending on how dependend upon they are.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/31
	 */
	private ProjectBinary[] __getBinaries(Set<ProjectName> __pn)
		throws NullPointerException
	{
		// Check
		if (__pn == null)
			throw new NullPointerException("NARG");
		
		// Outer queue of projects which are used for complete processing
		ProjectManager manager = this.manager;
		Deque<ProjectBinary> fullq = new ArrayDeque<>();
		Set<ProjectBinary> didq = new HashSet<>();
		for (ProjectName p : __pn)
			fullq.offerLast(manager.getBinary(p));
		
		// This is the count of every project which is used
		Map<ProjectBinary, Integer> counts = new HashMap<>();
		
		// Process any projects in the queue until it is empty
		while (!fullq.isEmpty())
		{
			// Only process binaries once
			ProjectBinary pb = fullq.removeFirst();
			if (didq.contains(pb))
				continue;
			didq.add(pb);
			
			// Debug
			System.err.printf("DEBUG -- Full %s%n", pb.name());
			
			// Always make sure this project is always counted
			if (!counts.containsKey(pb))
				counts.put(pb, 0);
			
			// Go through all dependencies recursively
			for (ProjectBinary dep : pb.binaryDependencies(true))
			{
				// Decrement so that projects that are used more are at a
				// lower value (since sort is from lower to higher values)
				Integer v = counts.get(dep);
				if (v == null)
					counts.put(dep, -1);
				else
					counts.put(dep, v - 1);
				
				// Add to processing queue
				fullq.offerLast(dep);
			}
		}
		
		// List counts
		System.err.printf("DEBUG -- Counts: %s%n", counts);
		
		// Place counts into sorted order
		int n = counts.size();
		List<ProjectBinary> rv = new ArrayList<>(n);
		List<Integer> cc = new ArrayList<>(n);
		for (Map.Entry<ProjectBinary, Integer> e : counts.entrySet())
		{
			ProjectBinary k = e.getKey();
			Integer v = e.getValue();
			
			// Find the position where this goes
			int id = Collections.<Integer>binarySearch(cc, v);
			if (id < 0)
				id = -(id) - 1;
			
			// Add to the lists
			rv.add(id, k);
			cc.add(id, v);
		}
		
		// Use given project list
		return rv.<ProjectBinary>toArray(new ProjectBinary[rv.size()]);
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
		ProjectManager manager = this.manager;
		try (InputStream is = TargetBuilder.class.getResourceAsStream(
			"template/" + __in))
		{
			// Failed to find template, try looking for a project instead
			if (is == null)
			{
				// If a binary exists with the specified name then add it to
				// the list of projects to build
				ProjectName pn = new ProjectName(__in);
				ProjectBinary pb = manager.getBinary(pn);
				if (pb != null)
				{
					__pn.add(pn);
					return;
				}
				
				// {@squirreljme.error AO06 The specified template or project
				// does not exist.}
				throw new IOException(String.format("AO06 %s", __in));
			}
			
			// Parse the manifest
			JavaManifest man = new JavaManifest(is);
			for (Map.Entry<JavaManifestKey, String> e :
				man.getMainAttributes().entrySet())
			{
				JavaManifestKey k = e.getKey();
				String ks = k.toString(),
					v = e.getValue();
				
				// JIT option?
				if (ks.startsWith("jit-"))
					__opt.put(
						new JITConfigKey(ks.substring(4).replace('-', '.')),
						new JITConfigValue(v));
				
				// Add projects to set?
				else if (k.equals(_ADD_PROJECTS))
				{
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

