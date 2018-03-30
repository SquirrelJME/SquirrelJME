// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.UUID;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This program reads standard input and builds a CSV for TODO progression.
 *
 * @since 2018/03/29
 */
public class TodoProgression
{
	/** Project detail. */
	protected final Map<UUID, Project> projects =
		new TreeMap<>();
	
	/** Tags which exist. */
	protected final Set<String> tags =
		new TreeSet<>();
	
	/**
	 * Registers data.
	 *
	 * @param __tag The tag the checkout is called.
	 * @param __name The name of the project.
	 * @param __uuid The project UUID.
	 * @param __n The number of TODOs in the project.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/29
	 */
	public void register(String __tag, String __name, UUID __uuid, int __n)
		throws NullPointerException
	{
		if (__tag == null || __name == null || __uuid == null)
			throw new NullPointerException("NARG");
		
		Map<UUID, Project> projects = this.projects;
		Project p = projects.get(__uuid);
		if (p == null)
			projects.put(__uuid, (p = new Project(__uuid)));
		
		// Count for project
		p.count(__tag, __name, __n);
	}
	
	/**
	 * Writes the results to the output stream.
	 *
	 * @param __ps The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/29
	 */
	public void write(PrintStream __ps)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		Set<String> tags = this.tags;
		
		// Build row one for column headers
		__ps.print("name");
		for (String t : tags)
		{
			__ps.print(',');
			__ps.print(t);
		}
		
		// End with line
		__ps.println();
		
		// Sort projects by their name then UUID so they appear consistent
		// and alphabetically sorted
		Set<Project> sorted = new TreeSet<>(projects.values());
		
		// Go through all projects and dump their data, ignore the UUID and
		// only use the latest name
		for (Project p : sorted)
		{
			// Print name
			__ps.print(p.lastname);
			
			// Print all counts
			Map<String, Integer> counts = p.counts;
			for (String t : tags)
			{
				// Comma before
				__ps.print(',');
				
				// Only print value if it is not null
				Integer i = counts.get(t);
				if (i != null)
					__ps.print(i);
			}
			
			// End line for the column
			__ps.println();
		}
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Not used.
	 * @since 2018/03/29
	 */
	public static void main(String... __args)
		throws IOException
	{
		TodoProgression tp = new TodoProgression();
		
		// Read in inputs
		try (BufferedReader br = new BufferedReader(
			new InputStreamReader(System.in)))
		{
			String ln;
			while (null != (ln = br.readLine()))
			{
				// Split forms
				String[] fields = ln.split("[ \t]");
				
				// Extract project name
				String name = fields[1];
				int ls = name.lastIndexOf('/');
				if (ls >= 0)
					name = name.substring(ls + 1);
				
				// Register data
				tp.register(fields[0], name, UUID.fromString(fields[2]),
					Integer.parseInt(fields[3]));
			}
		}
		
		// Write output
		tp.write(System.out);
	}
	
	/**
	 * Generates a hashcode from the UUID.
	 *
	 * @param __uuid The input UUID to hash.
	 * @return The hashcode for the UUID.
	 * @since 2018/03/29
	 */
	static final int __hashUUID(UUID __uuid)
	{
		return Long.hashCode(__uuid.getLeastSignificantBits()) ^
			~Long.hashCode(__uuid.getMostSignificantBits());
	}
	
	/**
	 * Used to keep track of projects.
	 *
	 * @since 2018/03/29
	 */
	public class Project
		implements Comparable<Project>
	{
		/** The project UUID. */
		protected final UUID uuid;
		
		/** TODO counts. */
		protected final Map<String, Integer> counts =
			new TreeMap<>();
		
		/** The last name used. */
		protected volatile String lastname;
		
		/**
		 * Initializes the project information.
		 *
		 * @param __uuid The project UUID.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/03/29
		 */
		public Project(UUID __uuid)
			throws NullPointerException
		{
			if (__uuid == null)
				throw new NullPointerException("NARG");
			
			this.uuid = __uuid;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/03/29
		 */
		@Override
		public int compareTo(Project __p)
		{
			if (this == __p)
				return 0;
			
			// Make it so names appear first
			int rv = this.lastname.compareTo(__p.lastname);
			if (rv != 0)
				return rv;
			return this.uuid.compareTo(__p.uuid);
		}
		
		/**
		 * Counts TODO for the project at a given state.
		 *
		 * @param __tag The tag of the date.
		 * @param __name The name of the project.
		 * @param __n The number of TODOs.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/03/29
		 */
		public void count(String __tag, String __name, int __n)
			throws NullPointerException
		{
			this.lastname = __name + "@" +
				String.format("%08x", __hashUUID(this.uuid));
			this.counts.put(__tag, __n);
			
			TodoProgression.this.tags.add(__tag);
		}
	}
}

