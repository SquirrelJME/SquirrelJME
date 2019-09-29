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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * This class implements a bootstrap which is capable to build an environment
 * which is capable of building SquirrelJME target binaries.
 *
 * You should only compile and run this class manually if your system is not
 * able to use the pre-existing build scripts. If this is the case then you
 * must set the following system properties:
 *
 * {@code cc.squirreljme.bootstrap.binary} is the location
 * where output binaries are to be placed when they are compiled, along with
 * the bootstrap.
 * {@code cc.squirreljme.builder.root} is the location
 * of the SquirrelJME source tree.
 *
 * @since 2016/10/26
 */
public class NewBootstrap
	implements Runnable
{
	/** Deletes files. */
	public static final Consumer<Path, Object, IOException> DELETE =
		new Consumer<Path, Object, IOException>()
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/10/27
			 */
			@Override
			public void accept(Path __v, Object __s)
				throws IOException
			{
				Files.delete(__v);
			}
		};
	
	/** Returns the latest date. */
	public static final Consumer<Path, Long[], IOException> DATE =
		new Consumer<Path, Long[], IOException>()
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/10/27
			 */
			@Override
			public void accept(Path __v, Long[] __s)
				throws IOException
			{
				// Dates on directories might not truly be valid
				if (Files.isDirectory(__v))
					return;
				
				// Get date
				FileTime ft = Files.getLastModifiedTime(__v);
				long millis = ft.toMillis();
				
				// If it is newer, use that
				if (millis > __s[0])
					__s[0] = millis;
			}
		};
	
	/** Cache of the compiler so it does not need to be read multiple times. */
	private static BootstrapCompiler _javac;
	
	/** The binary path. */
	protected final Path binarypath;
	
	/** The source path. */
	protected final Path sourcepath;
	
	/** The input launch arguments. */
	protected final String[] launchargs;
	
	/** Projects available for usage. */
	protected final Map<String, BuildProject> projects;
	
	/** The output bootstrap binary. */
	protected final Path bootstrapout;
	
	/** The directory where JARs are placed. */
	protected final Path buildjarout;
	
	/**
	 * Initializes the bootstrap base.
	 *
	 * @param __bin The binary output directory.
	 * @param __src The source input namespace directories.
	 * @param __args Arguments to the bootstrap.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/26
	 */
	public NewBootstrap(Path __bin, Path __src, String[] __args)
		throws IOException, NullPointerException
	{
		// Check
		if (__bin == null || __src == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.binarypath = __bin;
		this.sourcepath = __src;
		this.launchargs = __args.clone();
		this.buildjarout = __bin.resolve("bootsjme");
		this.bootstrapout = __bin.resolve("sjmeboot.jar");
		
		// Load all projects in the build directory
		Map<String, BuildProject> projects = new LinkedHashMap<>();
		this.projects = projects;
		
		// Java SE special host libraries
		__loadProjects(projects, __src.resolve("bldt/javase/libs"));
		
		// Run-time projects
		__loadProjects(projects, __src.resolve("runt/apis"));
		__loadProjects(projects, __src.resolve("runt/libs"));
		__loadProjects(projects, __src.resolve("runt/mids"));
		__loadProjects(projects, __src.resolve("runt/klib"));
		__loadProjects(projects, __src.resolve("runt/kmid"));
		
		// JIT-time projects
		__loadProjects(projects, __src.resolve("jitt/libs"));
		
		// Build-time projects
		__loadProjects(projects, __src.resolve("bldt/libs"));
		__loadProjects(projects, __src.resolve("bldt/mids"));
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2016/10/26
	 */
	@Override
	public void run()
	{
		// {@squirreljme.error NB03 The entry point project does not exist.}
		Map<String, BuildProject> projects = this.projects;
		BuildProject bp = projects.get("builder");
		if (bp == null)
			throw new IllegalStateException("NB03");
		
		// Could fail
		Path tempjar = null;
		try
		{
			// Compile JARs to be merged together as one
			Set<BuildProject> mergethese = bp.compile();
			
			// Get the time and date for the JARs to merge
			Path bootjar = this.bootstrapout;
			Long[] out = new Long[1];
			out[0] = Long.MIN_VALUE;
			NewBootstrap.<Long[]>__walk(this.buildjarout, out, DATE);
			long depjartime = out[0];
			
			// Get the time of the output JAR
			long bootjartime;
			if (Files.exists(bootjar))
			{
				out[0] = Long.MIN_VALUE;
				NewBootstrap.<Long[]>__walk(bootjar, out, DATE);
				bootjartime = out[0];
			}
			else
				bootjartime = Long.MIN_VALUE;
			
			// Repackage
			if (bootjartime == Long.MIN_VALUE || depjartime > bootjartime)
			{
				// {@squirreljme.error NB0a Merging output JAR file.}
				System.err.println("NB0a");
				
				// {@squirreljme.error NB0b Expected the entry point boot JAR
				// to be within the merge JAR set.}
				List<BuildProject> mergeorder = new ArrayList<>(mergethese);
				if (!mergeorder.remove(bp))
					throw new RuntimeException("NB0b");
				
				// Make the boot JAR always first
				mergeorder.add(0, bp);
				
				// {@squirreljme.error NB0d Expected the CLDC libraries to be
				// present in the build.}
				BuildProject cldccompact = projects.get("cldc-compact");
				if (!mergeorder.remove(cldccompact))
					throw new RuntimeException("NB0d");
				
				// Make the CLDC compact JAR always last, so that its system
				// entries are always shaded last. This is because they need
				// to be replaced accordingly to operate correctly
				mergeorder.add(cldccompact);
				
				// Create temporary JAR
				tempjar = Files.createTempFile("squirreljme-boot-out", ".jar");
				
				// Open output
				Map<String, BuildProject> shaded = new HashMap<>();
				Map<String, Set<String>> services = new LinkedHashMap<>();
				try (ZipOutputStream zos = new ZipOutputStream(Files.
					newOutputStream(tempjar,
					StandardOpenOption.WRITE, StandardOpenOption.CREATE)))
				{
					// Copy contents from other JARs
					for (BuildProject dp : mergeorder)
						__mergeInto(dp, zos, dp == bp, services, shaded);
					
					// Write services as needed
					byte[] buf = new byte[512];
					for (Map.Entry<String, Set<String>> e :
						services.entrySet())
					{
						// Write service descriptor
						zos.putNextEntry(
							new ZipEntry("META-INF/services/" + e.getKey()));
			
						// Write classes to provide services for 
						for (String v : e.getValue())
						{
							// Write name followed by \r\n pair
							zos.write(v.getBytes("utf-8"));
							zos.write('\r');
							zos.write('\n');
						}
			
						// Always end in a blank line
						zos.write('\r');
						zos.write('\n');
			
						// Done writing it
						zos.closeEntry();
					}
						
					// Finish it
					zos.finish();
					zos.flush();
				}
				
				// Move it
				Files.move(tempjar, bootjar,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		
		// {@squirreljme.error NB04 Failed to compile the bootstrap due to
		// a read/write error.}
		catch (IOException e)
		{
			throw new RuntimeException("NB04", e);
		}
		
		// Cleanup temporary JAR if it was created
		finally
		{
			if (tempjar != null)
				try
				{
					Files.delete(tempjar);
				}
				catch (IOException e)
				{
				}
		}
	}
	
	/**
	 * Loads projects in the given directory and places them into the existing
	 * map if they do not exist.
	 *
	 * @param __t The target project mapping.
	 * @param __p The source path.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/20
	 */
	private void __loadProjects(Map<String, BuildProject> __t, Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__t == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Go through files
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__p))
		{
			// Go through all directories
			for (Path p : ds)
			{
				// Must be a directory
				if (!Files.isDirectory(p))
					continue;
			
				// See if the manifest exists
				Path man = p.resolve("META-INF").resolve("MANIFEST.MF");
				if (!Files.exists(man))
					continue;
			
				// Load project
				BuildProject bp = new BuildProject(p, man, this.buildjarout,
					this.projects);
				
				// Add project, but never replace projects (this way the
				// build directory takes priority)
				String pn = bp.projectName();
				if (!__t.containsKey(pn))
					__t.put(bp.projectName(), bp);
			}
		}
	}
	
	/**
	 * Main entry point for the new bootstrap system.
	 *
	 * @param __args Program arguments.
	 * @throws IOException On any read/write errors.
	 * @since 2016/10/26
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Compiling under JamVM fails because it cannot find a resource
		// bundle for the Java compiler
		if (__args != null && __args.length >= 1 &&
			"--toolsflaw".equals(__args[0]))
		{
			__detectToolsFlaw();
			return;
		}
		
		// Get directories for input and output
		Path bin = Paths.get(System.getProperty(
			"cc.squirreljme.bootstrap.binary")),
			src = Paths
			.get(System.getProperty(
			"cc.squirreljme.builder.root"));
		
		// Only build?
		NewBootstrap nb = new NewBootstrap(bin, src, __args);
		
		// Run it
		nb.run();
	}
	
	/**
	 * This method detects if the virtual machine has a flaw where it cannot
	 * find the javac resource because of odd handling of JARs dynamically
	 * loaded at run-time.
	 *
	 * If the flaw needs to have a work-around then a JAR will be printed
	 * to standard output.
	 *
	 * @sine 2018/01/13
	 */
	private static final void __detectToolsFlaw()
	{
		String vmname = System.getProperty("java.vm.name", "");
		
		// JamVM has an issue where when tools.jar is dynamically loaded it
		// will fail to find the named resource. This will cause all
		// compilation with the system Java compiler to fail.
		if (vmname.equalsIgnoreCase("jamvm"))
		{
			// The JAR to locate will be in the specified location, likely
			// /usr/lib/jvm/java-7-openjdk-powerpc/lib/tools.jar
			Path jhome = Paths.get(System.getProperty("java.home", ""));
			
			// Maybe it is here?
			Path mightbe = jhome.resolve("tools.jar");
			if (Files.exists(mightbe))
			{
				System.out.println(mightbe);
				return;
			}
			
			// Try to see if we can get in the library directory
			jhome = jhome.getParent();
			if (jhome != null)
				jhome = jhome.resolve("lib");
				
			// It could be here
			mightbe = jhome.resolve("tools.jar");
			if (Files.exists(mightbe))
			{
				System.out.println(mightbe);
				return;
			}
			
			// These paths will need to be searched for the tools.jar
			// It will be in a path like:
			String[] bootpaths = System.getProperty("sun.boot.class.path", "").
				split(Pattern.quote(
				System.getProperty("path.separator", ":")));
			
			// Go through each path and try to find tools.jar
			for (String rbp : bootpaths)
			{
				Path p = Paths.get(rbp);
				
				// If this refers to a file then try to its directory
				if (!Files.isDirectory(p))
					p = p.getParent();
				
				// Ignore it because it could not be found
				if (p == null || !Files.isDirectory(p))
					continue;
				
				// If there is a tools.jar here, indicate that
				p = p.resolve("tools.jar");
				if (Files.exists(p))
				{
					System.out.println(p);
					return;
				}
			}
		}
	}
	
	/**
	 * Merges the input JAR into the output JAR.
	 *
	 * @param __bp The source.
	 * @param __zos The destination.
	 * @param __useman Use the manifest?
	 * @param __svs Service list.
	 * @param __shade Used to detect shading.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/28
	 */
	private static void __mergeInto(BuildProject __bp, ZipOutputStream __zos,
		boolean __useman, Map<String, Set<String>> __svs,
		Map<String, BuildProject> __shade)
		throws IOException, NullPointerException
	{
		// Check
		if (__bp == null || __zos == null || __svs == null || __shade == null)
			throw new NullPointerException("NARG");
		
		// Go through the input
		try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(
			__bp.jarout, StandardOpenOption.READ)))
		{
			// Copy all entries
			ZipEntry e;
			byte[] buf = new byte[4096];
			while (null != (e = zis.getNextEntry()))
			{
				// If the entry is the manifest, only use it if it was
				// requested
				String name = e.getName();
				boolean ismanifest;
				if ((ismanifest = name.equals("META-INF/MANIFEST.MF")))
					if (!__useman)
					{
						zis.closeEntry();
						continue;
					}
				
				// This is a service, needs to be handled later
				if (name.startsWith("META-INF/services/") &&
					name.length() > "META-INF/services/".length())
				{
					// Record service to write later
					String k = name.substring("META-INF/services/".length());
					Set<String> into = __svs.get(k);
					if (into == null)
						__svs.put(k, (into = new LinkedHashSet<>()));
					
					// Read in services completely
					byte[] data = null;
					try (ByteArrayOutputStream baos =
						new ByteArrayOutputStream())
					{
						// Copy loop
						for (;;)
						{
							int rc = zis.read(buf);
					
							// EOF?
							if (rc < 0)
								break;
					
							// Write
							baos.write(buf, 0, rc);
						}
						
						// Write in
						baos.flush();
						data = baos.toByteArray();
					}
					
					// Parse the data
					try (BufferedReader br = new BufferedReader(
						new InputStreamReader(new ByteArrayInputStream(data))))
					{
						for (;;)
						{
							String ln = br.readLine();
							
							// EOF?
							if (ln == null)
								break;
							
							// Ignore whitespace and empty lines
							ln = ln.trim();
							if (ln.length() <= 0)
								continue;
							
							// Add service
							into.add(ln);
						}
					}
					
					// Do not write this entry
					continue;
				}
				
				// {@squirreljme.error NB0c The specified entry has been
				// shaded out, an earlier file is taking priority. (The entry;
				// The base project; The current project)}
				BuildProject shadeout = __shade.get(name);
				if (shadeout != null)
				{
					System.err.printf("NB0c %s %s %s%n", e, shadeout.name,
						__bp.name);
					continue;
				}
				__shade.put(name, __bp);
				
				// Write to target
				__zos.putNextEntry(e);
				
				// Input source for where to read actual data
				InputStream sourcedata;
				Manifest realman = null;
				if (!ismanifest || !__useman)
					sourcedata = zis;
				
				// Make a fake manifest which is used instead
				else
				{
					Manifest fakeman = new Manifest(zis);
					Attributes fakeattr = fakeman.getMainAttributes();
					
					// Copy the real manifest so that it is placed in the
					// output tree
					realman = new Manifest(fakeman);
					
					// Move the main-class to a fake class
					fakeattr.putValue("X-SquirrelJME-Booted-Main-Class",
						fakeattr.getValue("Main-Class"));
					
					// Use instead a wrapped main which sets up the CLDC stuff
					// as needed so the run-time functions
					fakeattr.putValue("Main-Class",
						"cc.squirreljme.runtime.javase.Main");
					
					// Read from the fake manifest instead
					try (ByteArrayOutputStream baos = 
						new ByteArrayOutputStream())
					{
						fakeman.write(baos);
						baos.flush();
						
						// Use this manifest as the source instead
						sourcedata = new ByteArrayInputStream(
							baos.toByteArray());
					}
				}
				
				// Copy loop
				for (;;)
				{
					int rc = sourcedata.read(buf);
					
					// EOF?
					if (rc < 0)
						break;
					
					// Write
					__zos.write(buf, 0, rc);
				}
				
				// Close
				__zos.closeEntry();
				zis.closeEntry();
				
				// Write real manifest as backup?
				if (realman != null)
				{
					__zos.putNextEntry(
						new ZipEntry("SQUIRRELJME-BOOTSTRAP.MF"));
					realman.write(__zos);
					__zos.closeEntry();
				}
			}
		}
	}
	
	/**
	 * Walks the given path and calls the given consumer for every file and
	 * directory.
	 *
	 * @param <S> The secondary value to pass.
	 * @param __p The path to walk.
	 * @param __s The secondary value.
	 * @param __c The function to call for paths.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments, except for the secondary
	 * value.
	 * @since 2016/09/18
	 */
	static <S> void __walk(Path __p, S __s,
		Consumer<Path, S, IOException> __c)
		throws IOException, NullPointerException
	{
		// Check
		if (__p == null || __c == null)
			throw new NullPointerException("NARG");
		
		// If a directory, walk through all the files
		if (Files.isDirectory(__p))
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(__p))
			{
				for (Path s : ds)
					__walk(s, __s, __c);
			}
		
		// Always accept, directories are accepted last since directories
		// cannot be deleted if they are not empty
		__c.accept(__p, __s);
	}
	
	/**
	 * Calculates the name that a file would appear as inside of a ZIP file.
	 *
	 * @param __root The root path.
	 * @param __p The file to add.
	 * @return The ZIP compatible name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	static String __zipName(Path __root, Path __p)
		throws NullPointerException
	{
		// Check
		if (__root == null || __p == null)
			throw new NullPointerException();
		
		// Calculate relative name
		Path rel = __root.toAbsolutePath().relativize(__p.toAbsolutePath());
		
		// Build name
		StringBuilder sb = new StringBuilder();
		for (Path comp : rel)
		{
			// Prefix slash
			if (sb.length() > 0)
				sb.append('/');
			
			// Add component
			sb.append(comp);
		}
		
		// Return it
		return sb.toString();
	}
}

