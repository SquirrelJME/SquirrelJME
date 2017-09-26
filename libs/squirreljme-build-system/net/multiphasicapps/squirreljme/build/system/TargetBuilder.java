// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
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
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ServiceLoader;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.build.projects.Project;
import net.multiphasicapps.squirreljme.build.projects.ProjectBinary;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.build.projects.ProjectName;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigKey;
import net.multiphasicapps.squirreljme.jit.JITConfigValue;
import net.multiphasicapps.squirreljme.jit.JITInput;
import net.multiphasicapps.squirreljme.jit.PrintStreamProgressNotifier;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

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
		JITConfigValue arch = jitopts.get(new JITConfigKey("jit.arch"));
		if (arch == null)
			throw new IllegalArgumentException("AO07");
		
		// Locate the JITConfig which creates things such as the machine code
		// output
		JITConfig jc = new JITConfig(jitopts);
			
		// {@squirreljme.error AO08 Unknown architecture specified
		// which is not supported. (The architecture)}
		if (jc == null)
			throw new IllegalArgumentException(String.format("AO08 %s", arch));
		
		// Debug
		System.err.printf("DEBUG -- Actual JIT Options: %s%n", jc);
		
		// Initialize the link state
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
		JITInput input = new JITInput();
		JITConfig jitconfig = this.jitconfig;
		ProjectBinary[] binaries = this._binaries;
		int count = 0,
			numbins = binaries.length;
		
		// Go through all binary projects and compile them
		for (ProjectBinary pb : binaries)
		{
			// {@squirreljme.error AO09 Compiling the specified project. (The
			// project name; The current binary; The number of binaries)}
			String pbname = pb.name().toString();
			System.out.printf("AO09 %s %d %d%n", pbname, ++count, numbins);
			
			// Process all classes and resources
			try (ZipStreamReader zsr = pb.openZipStreamReader())
			{
				input.readZip(pbname, zsr);
			}
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
		
		// Comparator for sorting by project name
		Comparator<ProjectBinary> comp = new Comparator<ProjectBinary>()
			{
				/**
				 * {@inheritDoc}
				 * @since 2017/07/01
				 */
				@Override
				public int compare(ProjectBinary __a, ProjectBinary __b)
				{
					return __a.name().compareTo(__b.name());
				}
			};
		
		// Sort sub-ranges alphabetically so that the given order is always
		// the same
		ProjectBinary[] rva = rv.<ProjectBinary>toArray(new ProjectBinary[n]);
		for (int l = 0, r; l < n; l++)
		{
			// Find 
			int basepiv = cc.get(l);
			for (r = l; r < n; r++)
				if (cc.get(r) != basepiv)
					break;
			
			// Sort this region
			Arrays.<ProjectBinary>sort(rva, l, r, comp);
		}
		
		// Use given project list
		return rva;
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
		
		// Allow options to be specified on the command line without requiring
		// a template be used
		boolean plus;
		if ((plus = __in.startsWith("+")) || __in.startsWith("-"))
		{
			// {@squirreljme.error AO0b Invalid argument to be parsed, it must
			// either be in the format of '{@code +key=value}' to add a JIT
			// configuration option or '{@code -key}' to remove it. (The input
			// argument)}
			int eq = __in.indexOf('=');
			if (plus == (eq < 0))
				throw new IOException(String.format("AO0b %s", __in));
			
			// Parse pair
			JITConfigKey k = new JITConfigKey(__in.substring(1, eq));
			JITConfigValue v = (eq < 0 ? null :
				new JITConfigValue(__in.substring(eq + 1)));
			
			// Add or remove option
			if (plus)
				__opt.put(k, v);
			else
				__opt.remove(k);
			
			// Do not process as a template
			return;
		}
		
		// Project file
		else if (__in.endsWith(".jar") || __in.endsWith(".JAR"))
		{
			// Remove the extension
			String extless = __in.substring(0, __in.length() - 4);
			
			// If a binary exists with the specified name then add it to
			// the list of projects to build
			ProjectName pn = new ProjectName(extless);
			ProjectBinary pb = manager.getBinary(pn);
			
			// {@squirreljme.error AO0c The specified project cannot be added
			// to the target because it does not exist. (The project to add)}
			if (pb == null)
				throw new IOException(String.format("AO0c %s", pn));
			
			// Add
			__pn.add(pn);
		}
		
		// Try to find a template
		else
		{
			// Read input resource as a manifest
			ProjectManager manager = this.manager;
			try (InputStream is = TargetBuilder.class.getResourceAsStream(
				"template/" + __in))
			{
				// {@squirreljme.error AO06 The specified template
				// does not exist. (The template name)}
				if (is == null)
					throw new IOException(String.format("AO06 %s", __in));
			
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
}

