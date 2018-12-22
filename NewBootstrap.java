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
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.attribute.FileTime;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

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
	private static volatile JavaCompiler _javac;
	
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
		
		// Use them
		this.projects = projects;
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
				try (ZipOutputStream zos = new ZipOutputStream(Channels.
					newOutputStream(FileChannel.open(tempjar,
					StandardOpenOption.WRITE, StandardOpenOption.CREATE))))
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
				BuildProject bp = new BuildProject(p, man);
				
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
	 * Lowercases the specified string.
	 *
	 * @param __s The string to check.
	 * @return The string in correct form.
	 * @throws IllegalArgumentException If it is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/27
	 */
	private static String __correctProjectName(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Check if any characters need lowering first
		int n = __s.length();
		boolean dolower = false;
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Lowercase?
			if (c >= 'A' && c <= 'Z')
				dolower = true;
			
			// Legal character?
			else if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') ||
				c == '-')
				continue;
			
			// {@squirreljme.error NB01 Illegal character used in project
			// name. (The input project name)}
			else
				throw new IllegalArgumentException(String.format("NB01 %s",
					__s));
		}
		
		// Use original string
		if (!dolower)
			return __s;
		
		// Lowercase everything
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Lowercase?
			if (c >= 'A' && c <= 'Z')
				sb.append((char)((c - 'A') + 'a'));
			
			// Keep
			else
				sb.append(c);
		}
		
		// Use that
		return sb.toString();
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
		try (ZipInputStream zis = new ZipInputStream(Channels.newInputStream(
			FileChannel.open(__bp.jarout, StandardOpenOption.READ))))
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
	private static <S> void __walk(Path __p, S __s,
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
	private static String __zipName(Path __root, Path __p)
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
	
	/**
	 * This represents a single project which may be built.
	 *
	 * @since 2016/10/27
	 */
	public class BuildProject
	{
		/** The project base path. */
		protected final Path basepath;
		
		/** The project name. */
		protected final String name;
		
		/** The output path for this JAR. */
		protected final Path jarout;
		
		/** Dependencies of this build project. */
		protected final Set<String> depends;
		
		/** In compilation? */
		private volatile boolean _incompile;
		
		/** Was the source date calculated? */
		private volatile long _sourcedate =
			Long.MIN_VALUE;
		
		/**
		 * Initializes the build project.
		 *
		 * @param __b The project base path.
		 * @param __mp The manifest path.
		 * @throws IOException On read/write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/10/27
		 */
		BuildProject(Path __b, Path __mp)
			throws IOException, NullPointerException
		{
			// Check
			if (__b == null || __mp == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.basepath = __b;
			
			// Load the manifest
			Manifest man;
			try (InputStream in = Channels.newInputStream(
				FileChannel.open(__mp, StandardOpenOption.READ)))
			{
				man = new Manifest(in);
			}
			
			// Handle main attributes
			Attributes attr = man.getMainAttributes();
			
			// The project name depends on the uppermost name of the directory
			String name;
			this.name = (name = __correctProjectName(
				__b.getFileName().toString()));
			
			// Where is this output?
			this.jarout = NewBootstrap.this.buildjarout.resolve(name + ".jar");
			
			// Determine dependencies
			Set<String> depends = new LinkedHashSet<>();
			String rd = attr.getValue("X-SquirrelJME-Depends");
			if (rd != null)
				for (String s : rd.split("[ \t]"))
					depends.add(__correctProjectName(s.trim()));
			
			// Force a dependency on the CLDC Compact library
			if (!name.equals("cldc-compact"))
				depends.add(__correctProjectName("cldc-compact"));
			
			// Set
			this.depends = depends;
		}
		
		/**
		 * Returns the date of the binary.
		 *
		 * The value here is not cached.
		 *
		 * @return The date of the binary.
		 * @since 2016/10/27
		 */
		public long binaryDate()
		{
			// If it does not exist, the date is not valid
			Path jarout = this.jarout;
			if (!Files.exists(jarout))
				return Long.MIN_VALUE;
			
			// Calculate
			try
			{
				Long[] out = new Long[1];
				out[0] = Long.MIN_VALUE;
				NewBootstrap.<Long[]>__walk(jarout, out, DATE);
				return out[0];
			}
			
			// Ignore
			catch (IOException e)
			{
				return Long.MIN_VALUE;
			}
		}
		
		/**
		 * Compiles this project and any dependencies it may have.
		 *
		 * @return The set of projects which were compiled.
		 * @throws IOException On read/write errors.
		 * @since 2016/10/27
		 */
		public Set<BuildProject> compile()
			throws IOException
		{
			Set<BuildProject> rv = new LinkedHashSet<>();
			
			// Build loop
			try
			{
				// {@squirreljme.error NB02 The specified project eventually
				// depends on itself. (The project name)}
				if (this._incompile)
					throw new IllegalStateException(String.format(
						"NB02 %s", this.name));
				
				// Currently compiling
				this._incompile = true;
				
				// Compile dependencies first
				Map<String, BuildProject> projects =
					NewBootstrap.this.projects;
				for (String dep : this.depends)
				{
					// {@squirreljme.error NB06 The dependency of a given
					// project does not exist. (This project; The project it
					// depends on)}
					BuildProject dp = projects.get(dep);
					if (dp == null)
						throw new IllegalStateException(String.format(
							"NB06 %s %s", this.name, dep));
					
					// Compile the dependency and add it to the merge group
					for (BuildProject bp : dp.compile())
						rv.add(bp);
				}
				
				// Other complation state
				__compileStep(rv);
			}
			
			// Clear compile state
			finally
			{
				this._incompile = false;
			}
			
			// Add self to dependency chain
			rv.add(this);
			
			// Return it
			return rv;
		}
		
		/**
		 * Returns the name of this project.
		 *
		 * @return The project name.
		 * @since 2016/10/27
		 */
		public String projectName()
		{
			return this.name;
		}
		
		/**
		 * Returns the date when the project sources were last touched.
		 *
		 * @return The project sources date.
		 * @since 2016/10/27
		 */
		public long sourcesDate()
		{
			// Use cached value
			long rv = this._sourcedate;
			if (rv != Long.MIN_VALUE)
				return rv;
			
			// Calculate otherwise
			try
			{
				Long[] out = new Long[1];
				out[0] = Long.MIN_VALUE;
				NewBootstrap.<Long[]>__walk(this.basepath, out, DATE);
				this._sourcedate = (rv = out[0]);
			}
			
			// Ignore
			catch (IOException e)
			{
			}
			
			// Return it
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/25
		 */
		@Override
		public String toString()
		{
			return this.name;
		}
		
		/**
		 * Secondary compilation step.
		 *
		 * @param __deps Dependencies used.
		 * @throws IOException On read/write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/10/27
		 */
		private void __compileStep(Set<BuildProject> __deps)
			throws IOException, NullPointerException
		{
			// Check
			if (__deps == null)
				throw new NullPointerException("NARG");
			
			// Best dependency date
			long depdate = Long.MIN_VALUE;
			for (BuildProject bp : __deps)
				depdate = Math.max(depdate, bp.binaryDate());
			
			// Do not recompile if it is up to date
			long srcdate = sourcesDate(),
				bindate =  binaryDate();
			Path jarout = this.jarout;
			if (Files.exists(jarout) && srcdate <= bindate &&
				bindate > depdate)
				return;
			
			// {@squirreljme.error NB09 Now compiling the specified project.
			// (The name of the project being compiled)}
			String name = this.name;
			System.err.printf("NB09 %s%n", name);
			
			// {@squirreljme.error NB07 No Java compiler is available through
			// the Java class library (javax.tools.JavaCompiler), you may need
			// to make sure that tools.jar is loaded into your classpath
			// either manually (although in most installations it should be
			// automatic).}
			JavaCompiler javac = NewBootstrap._javac;
			if (javac == null)
			{
				javac = ToolProvider.getSystemJavaCompiler();
				if (javac == null)
					throw new IllegalStateException("NB07");
				NewBootstrap._javac = javac;
			}
			
			// Need to clear files
			Path tempdir = null;
			try
			{
				// Create temporary directory
				tempdir = Files.createTempDirectory(
					"squirreljme-build-" + name);
				
				// Source code is just this project
				final Path basepath = this.basepath;
				
				// Get all source code to be compiled
				final Set<File> fsources = new LinkedHashSet<>();
				NewBootstrap.<Object>__walk(basepath, null,
					new Consumer<Path, Object, IOException>()
					{
						/**
						 * {@inheritDoc}
						 * @since 2016/10/27
						 */
						@Override
						public void accept(Path __p, Object __s)
							throws IOException
						{
							// Ignore directories
							if (Files.isDirectory(__p))
								return;
							
							// Add to sources if it is a source file
							if (__p.getFileName().toString().endsWith(".java"))
								fsources.add(__p.toFile());
						}
					});
				
				// If there are no sources to compile, do not bother
				if (!fsources.isEmpty())
				{
					// Need to access the disk
					StandardJavaFileManager jfm = javac.getStandardFileManager(
						null, null, null);
					
					// Source code is just this project
					jfm.setLocation(StandardLocation.SOURCE_PATH,
						Arrays.<File>asList(basepath.toFile()));
				
					// Outputs to the temporary directory
					jfm.setLocation(StandardLocation.CLASS_OUTPUT,
						Arrays.<File>asList(tempdir.toFile()));
				
					// The class path is just the dependencies
					Set<File> fdeps = new LinkedHashSet<>();
					for (BuildProject bp : __deps)
						fdeps.add(bp.jarout.toFile());
					jfm.setLocation(StandardLocation.CLASS_PATH, fdeps);
					
					// Create task
					JavaCompiler.CompilationTask task = javac.getTask(null,
						jfm, null, Arrays.<String>asList("-source", "1.7",
						"-target", "1.7", "-g", "-Xlint:deprecation",
						"-Xlint:unchecked"), null, jfm.getJavaFileObjects(
						fsources.<File>toArray(new File[fsources.size()])));
				
					// {@squirreljme.error NB08 Compilation of this project
					// failed. (The name of this project)}
					if (!task.call())
						throw new RuntimeException(String.format("NB08 %s",
							name));
				}
				
				// Create directory used for output
				Path jpar = jarout.getParent();
				if (jpar != null)
					Files.createDirectories(jpar);
				
				// Generate JAR output
				Path tempjar = Files.createTempFile("buildjar", "jar");
				try (final ZipOutputStream zos = new ZipOutputStream(Channels.
					newOutputStream(FileChannel.open(tempjar,
					StandardOpenOption.WRITE, StandardOpenOption.CREATE))))
				{
					// Write files in the output and input
					final byte[] buf = new byte[4096];
					Consumer<Path, Path, IOException> func =
						new Consumer<Path, Path, IOException>()
						{
							/**
							 * {@inheritDoc}
							 * @since 2016/10/28
							 */
							@Override
							public void accept(Path __p, Path __s)
								throws IOException
							{
								ZipOutputStream out = zos;
								
								// Ignore directories
								if (Files.isDirectory(__p) || __p.
									getFileName().toString().endsWith("java"))
									return;
								
								// Ignore files ending in .java
								Path fn = __p.getFileName();
								String fns = __p.getFileName().toString();
								if (fns.endsWith(".java"))
									return;
								
								// Create new entry
								out.putNextEntry(
									new ZipEntry(__zipName(__s, __p)));
								
								// Copy data
								try (InputStream is = Channels.newInputStream(
									FileChannel.open(__p,
									StandardOpenOption.READ)))
								{
									for (;;)
									{
										int rc = is.read(buf);
										
										// EOF?
										if (rc < 0)
											break;
										
										// Copy
										out.write(buf, 0, rc);
									}
								}
								
								// Close
								out.closeEntry();
							}
						};
					
					// Add sources and classes
					NewBootstrap.<Path>__walk(basepath, basepath, func);
					NewBootstrap.<Path>__walk(tempdir, tempdir, func);
					
					// Finish it
					zos.finish();
					zos.flush();
					
					// Move JAR
					Files.move(tempjar, jarout,
						StandardCopyOption.REPLACE_EXISTING);
				}
				
				// Failed, delete output JAR
				catch (IOException|RuntimeException|Error e)
				{
					// Delete file
					try
					{
						Files.delete(tempjar);
					}
					catch (IOException f)
					{
						// Ignore
					}
					
					// Rethrow
					throw e;
				}
			}
			
			// Always clear at the end
			finally
			{
				if (tempdir != null)
					try
					{
						NewBootstrap.<Object>__walk(tempdir, null, DELETE);
					}
					catch (IOException e)
					{
						// Ignore
					}
			}
		}
	}
	
	/**
	 * Consumes a value which may throw an exception.
	 *
	 * @param <V> The value to consume.
	 * @param <S> A secondary value that may be passed.
	 * @param <E> The exception to potentially throw.
	 * @since 2016/10/27
	 */
	public static interface Consumer<V, S, E extends Exception>
	{
		/**
		 * Accepts a value.
		 *
		 * @param __v The value to access.
		 * @param __s An optional secondary value.
		 * @throws E If this given exception type is thrown.
		 * @since 2016/10/27
		 */
		public abstract void accept(V __v, S __s)
			throws E;
	}
	/**
	 * This represents the alphabet that is used for Base64.
	 *
	 * @since 2018/03/05
	 */
	public enum MIMECharAlphabet
	{
		/** The basic and MIME alphabet. */
		BASIC('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/', '='),
		
		/** The URL alphabet. */
		URL('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_', '='),
		
		/** End. */
		;
		
		/** The alphabet for the characters. */
		final char[] _alphabet;
		
		/**
		 * Initializes the alphabet.
		 *
		 * @param __alphabet The alphabet.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/03/05
		 */
		private MIMECharAlphabet(char... __alphabet)
			throws NullPointerException
		{
			if (__alphabet == null)
				throw new NullPointerException("NARG");
			
			this._alphabet = __alphabet;
		}
	}
	
	/**
	 * This decodes the base64 character set, ignoring invalid characters, and
	 * provides the binary data for the input. If the padding character is
	 * reached or if the input stream runs out of characters then EOF is
	 * triggered.
	 *
	 * @since 2018/03/05
	 */
	public static final class MIMECharDecoder
		extends InputStream
	{
		/** The source reader. */
		protected final Reader in;
		
		/** Ignore padding characters. */
		protected final boolean ignorepadding;
		
		/** The alphabet to use for decoding. */
		private final char[] _alphabet;
		
		/** The ASCII map for quick lookup. */
		private final byte[] _ascii;
		
		/** Output bytes to drain. */
		private final byte[] _drain =
			new byte[3];
		
		/** The current fill buffer. */
		private volatile int _buffer;
		
		/** The number of bits which are in the buffer. */
		private volatile int _bits;
		
		/** Has EOF been reached if the pad has been detected? */
		private volatile boolean _readeof;
		
		/** The current output drain position. */
		private volatile int _drained =
			-1;
		
		/** The maximum value for drained values. */
		private volatile int _drainedmax =
			-1;
		
		/**
		 * Initializes the decode the default MIME alphabet.
		 *
		 * @param __in The input set of characters.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/23
		 */
		public MIMECharDecoder(Reader __in)
		{
			this(__in, MIMECharAlphabet.BASIC);
		}
		
		/**
		 * Initializes the decoder using the specified alphabet.
		 *
		 * @param __in The input set of characters.
		 * @param __chars The pre-defined character set to use for the
		 * alphabet.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/03/05
		 */
		public MIMECharDecoder(Reader __in, MIMECharAlphabet __chars)
			throws NullPointerException
		{
			this(__in, __chars._alphabet, false);
		}
		
		/**
		 * Initializes the decoder using the specified custom alphabet.
		 *
		 * @param __in The input set of characters.
		 * @param __chars The characters to use for the alphabet.
		 * @throws IllegalArgumentException If the alphabet is of the incorrect
		 * size.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/03/05
		 */
		public MIMECharDecoder(Reader __in, String __chars)
			throws IllegalArgumentException, NullPointerException
		{
			this(__in, __chars.toCharArray(), false);
		}
		
		/**
		 * Initializes the decoder using the specified custom alphabet.
		 *
		 * @param __in The input set of characters.
		 * @param __chars The characters to use for the alphabet.
		 * @throws IllegalArgumentException If the alphabet is of the incorrect
		 * size.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/03/05
		 */
		public MIMECharDecoder(Reader __in, char[] __chars)
			throws IllegalArgumentException, NullPointerException
		{
			this(__in, __chars, false);
		}
		
		/**
		 * Initializes the decoder using the default alphabet.
		 *
		 * @param __in The input set of characters.
		 * @param __chars The pre-defined character set to use for the alphabet.
		 * @param __ip Ignore padding characters and do not treat them as the
		 * end of the stream.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/03/05
		 */
		public MIMECharDecoder(Reader __in, MIMECharAlphabet __chars,
			boolean __ip)
			throws NullPointerException
		{
			this(__in, __chars._alphabet, __ip);
		}
		
		/**
		 * Initializes the decoder using the specified custom alphabet.
		 *
		 * @param __in The input set of characters.
		 * @param __chars The characters to use for the alphabet.
		 * @param __ip Ignore padding characters and do not treat them as the
		 * end of the stream.
		 * @throws IllegalArgumentException If the alphabet is of the incorrect
		 * size.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/03/05
		 */
		public MIMECharDecoder(Reader __in, String __chars, boolean __ip)
			throws IllegalArgumentException, NullPointerException
		{
			this(__in, __chars.toCharArray(), __ip);
		}
		
		/**
		 * Initializes the decoder using the specified custom alphabet.
		 *
		 * @param __in The input set of characters.
		 * @param __chars The characters to use for the alphabet.
		 * @param __ip Ignore padding characters and do not treat them as the
		 * end of the stream.
		 * @throws IllegalArgumentException If the alphabet is of the incorrect
		 * size.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/03/05
		 */
		public MIMECharDecoder(Reader __in, char[] __chars, boolean __ip)
			throws IllegalArgumentException, NullPointerException
		{
			if (__in == null || __chars == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.erorr NB8g The alphabet to use for the base64
			// decoder must be 64 characters plus one padding character.
			// (The character count)}
			int n;
			if ((n = __chars.length) != 65)
				throw new IllegalArgumentException(
					String.format("NB8g %d", n));
			
			// Set
			this.in = __in;
			this.ignorepadding = __ip;
			this._alphabet = (__chars = __chars.clone());
			
			// Build ASCII map for quick in-range character lookup
			byte[] ascii = new byte[128];
			for (int i = 0; i < 128; i++)
				ascii[i] = -1;
			for (int i = 0; i < 65; i++)
			{
				int dx = __chars[i];
				if (dx < 128)
					ascii[dx] = (byte)i;
			}
			this._ascii = ascii;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/25
		 */
		@Override
		public final int available()
			throws IOException
		{
			int drained = this._drained;
			
			// There are bytes which are ready and in the drain that we do not
			// need to block reading them?
			if (drained != -1)
				return this._drainedmax - drained;
			return 0;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/03/05
		 */
		@Override
		public final void close()
			throws IOException
		{
			this.in.close();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/03/05
		 */
		@Override
		public final int read()
			throws IOException
		{
			// If there is stuff to be drained, quickly drain that so we do not
			// need to go deeper into the heavier method
			int drained = this._drained;
			if (drained != -1)
			{
				// Read in drained character
				int rv = this._drain[drained++] & 0xFF;
				
				// Reached the drain limit?
				if (drained == this._drainedmax)
				{
					this._drained = -1;
					this._drainedmax = -1;
				}
				
				// Would still be drain
				else
					this._drained = drained;
				
				// Return the value
				return rv;
			}
			
			// Previously read EOF, so this will just return EOF
			if (this._readeof)
				return -1;
			
			// Otherwise decode and read
			byte[] next = new byte[1];
			for (;;)
			{
				int rc = this.read(next, 0, 1);
				
				// EOF?
				if (rc < 0)
					return -1;
				
				// Missed read
				else if (rc == 0)
					continue;
				
				return (next[0] & 0xFF);
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/25
		 */
		@Override
		public final int read(byte[] __b)
			throws IOException, NullPointerException
		{
			if (__b == null)
				throw new NullPointerException("NARG");
			
			return this.read(__b, 0, __b.length);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/03/05
		 */
		@Override
		public final int read(byte[] __b, int __o, int __l)
			throws IndexOutOfBoundsException, IOException, NullPointerException
		{
			if (__b == null)
				throw new NullPointerException("NARG");
			if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
				throw new IndexOutOfBoundsException("IOOB");
			
			// Did a previous read cause a padded EOF?
			boolean readeof = this._readeof;
			
			// Need lookups
			Reader in = this.in;
			boolean ignorepadding = this.ignorepadding;
			char[] alphabet = this._alphabet;
			byte[] ascii = this._ascii;
			byte[] drain = this._drain;
			
			// This buffer is filled into as needed when input characters are
			// read
			int buffer = this._buffer,
				bits = this._bits,
				drained = this._drained,
				drainedmax = this._drainedmax;
			
			// Keep trying to fill bytes in
			int rv = 0;
			while (rv < __l)
			{
				// Still need to drain bytes away
				if (drained != -1 && drained < drainedmax)
				{
					// Drain it
					__b[__o++] = drain[drained++];
					rv++;
					
					// Drained all the characters
					if (drained == drainedmax)
						drained = drainedmax = -1;
					
					// Try again
					else
						continue;
				}
				
				// EOF was reached
				if (readeof)
					break;
				
				// Read in character and decode it
				int ch = in.read();
				
				// Is EOF?
				if (ch < 0)
				{
					// {@squirreljme.error BD20 Read EOF from input when there
					// were expected to be more characters or the ending
					// padding character. (The bits in the buffer)}
					if (bits != 0)
						throw new IOException("BD20 " + bits);
					
					// Did read EOF
					readeof = true;
					break;
				}
				
				// Determine the value of the character
				if (ch < 128)
					ch = ascii[ch];
				else
				{
					ch = -1;
					for (int i = 0; i < 65; i++)
						if (i == alphabet[i])
						{
							ch = i;
							break;
						}
				}
				
				// Invalid, ignore and continue
				if (ch == -1 || (ignorepadding && ch == 64))
					continue;
				
				// Decoded padding character
				else if (ch == 64)
				{
					// {@squirreljme.error BD21 Did not expect a padding
					// character.
					// (The number of decoded bits in queue)}
					if (bits == 0 || bits == 24)
						throw new IOException("BD21 " + bits);
					
					// Only want to store a single extra byte since that is
					// all that is valid
					else if (bits < 16)
					{
						// {@squirreljme.error BD22 Expected another padding
						// character.}
						if (in.read() != alphabet[64])
							throw new IOException("BD22");
						
						drain[0] = (byte)(buffer >>> 4);
						
						drainedmax = 1;
					}
					
					// Otherwise there will be two characters to drain
					else
					{
						drain[0] = (byte)(buffer >>> 10);
						drain[1] = (byte)(buffer >>> 2);
						
						drainedmax = 2;
					}
					
					// Need to drain all
					drained = 0;
						
					// Clear the buffer
					buffer = bits = 0;
					
					// Did read EOF
					readeof = true;
				}
				
				// Normal data
				else
				{
					// Shift in six bits
					buffer <<= 6;
					buffer |= ch;
					bits += 6;
					
					// Drain and empty the buffer
					if (bits == 24)
					{
						// Fill the drain
						drain[0] = (byte)(buffer >>> 16);
						drain[1] = (byte)(buffer >>> 8);
						drain[2] = (byte)buffer;
						
						// Set these to drain
						drained = 0;
						drainedmax = 3;
						
						// Clear the buffer
						buffer = bits = 0;
					}
				}
			}
			
			// Store state for next run
			this._buffer = buffer;
			this._bits = bits;
			this._readeof = readeof;
			this._drained = drained;
			this._drainedmax = drainedmax;
			
			// Return the read count
			if (readeof && rv == 0)
				return -1;
			return rv;
		}
		
		/**
		 * Decodes the input string to byte values.
		 *
		 * @param __in The string to decode.
		 * @param __ab The alphabet to use.
		 * @return The resulting byte array.
		 * @throws IllegalArgumentException If the input string is not valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/06
		 */
		public static final byte[] decode(String __in, MIMECharAlphabet __ab)
			throws IllegalArgumentException, NullPointerException
		{
			return MIMECharDecoder.decode(__in, __ab, false);
		}
		
		/**
		 * Decodes the input string to byte values.
		 *
		 * @param __in The string to decode.
		 * @param __ab The alphabet to use.
		 * @param __ip Is padding ignored?
		 * @return The resulting byte array.
		 * @throws IllegalArgumentException If the input string is not valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/04
		 */
		public static final byte[] decode(String __in, MIMECharAlphabet __ab,
			boolean __ip)
			throws IllegalArgumentException, NullPointerException
		{
			if (__in == null || __ab == null)
				throw new NullPointerException("NARG");
			
			// Wrap in a reader to decode
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
			{
				byte[] buf = new byte[32];
				
				// Loop handle bytes
				try (InputStream in = new MIMECharDecoder(
					new StringReader(__in), __ab, __ip))
				{
					for (;;)
					{
						int rc = in.read(buf);
						
						// EOF?
						if (rc < 0)
							break;
						
						// Copy
						baos.write(buf, 0, rc);
					}
				}
				
				// Return resulting byte array
				return baos.toByteArray();
			}
			
			// {@squirreljme.error NB82 Could not decode the input string.}
			catch (IOException e)
			{
				throw new IllegalArgumentException("NB82", e);
			}
		}
	}
	
	/**
	 * This class is used to decode input streams which have been encoded in
	 * the MIME Base64 format. This file format is genearted by
	 * {@code uuencode -m}.
	 * This format usually begins with {@code begin-base64 mode filename} and
	 * ends with the padding sequence {@code ====}.
	 *
	 * This class is not thread safe.
	 *
	 * @since 2018/03/05
	 */
	public static final class MIMEFileDecoder
		extends InputStream
	{
		/** The input base64 data. */
		protected MIMECharDecoder mime;
		
		/** The read mode. */
		private int _mode =
			Integer.MIN_VALUE;
		
		/** The read filename. */
		private String _filename;
		
		/**
		 * Initializes the MIME file decoder using the default encoding.
		 *
		 * @param __in The input source.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/30
		 */
		public MIMEFileDecoder(InputStream __in)
			throws NullPointerException
		{
			this(new InputStreamReader(__in));
		}
		
		/**
		 * Initializes the MIME file decoder using the given encoding.
		 *
		 * @param __in The input source.
		 * @param __enc The encoding used.
		 * @throws NullPointerException On null arguments.
		 * @throws UnsupportedEncodingException If the encoding is not
		 * supported.
		 * @since 2018/11/30
		 */
		public MIMEFileDecoder(InputStream __in, String __enc)
			throws NullPointerException, UnsupportedEncodingException
		{
			this(new InputStreamReader(__in, __enc));
		}
		
		/**
		 * Initializes the MIME file decoder from the given set of characters.
		 *
		 * @param __in The input source.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/03/05
		 */
		public MIMEFileDecoder(Reader __in)
			throws NullPointerException
		{
			if (__in == null)
				throw new NullPointerException("NARG");
			
			// Directly wrap the reader with the MIME decoder which reads from
			// a source reader that is internally maintained
			this.mime = new MIMECharDecoder(new __SubReader__(__in));
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/25
		 */
		@Override
		public final int available()
			throws IOException
		{
			return this.mime.available();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/03/05
		 */
		@Override
		public final void close()
			throws IOException
		{
			this.mime.close();
		}
		
		/**
		 * Returns the filename which was read.
		 *
		 * @return The read filename, {@code null} will be returned if it has
		 * not been read yet or has not been specified.
		 * @since 2018/03/05
		 */
		public final String filename()
		{
			return this._filename;
		}
		
		/**
		 * Returns the UNIX mode of the stream.
		 *
		 * @return The UNIX mode, a negative value will be returned if it has
		 * not been read yet.
		 * @since 2018/03/05
		 */
		public final int mode()
		{
			return this._mode;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/03/05
		 */
		@Override
		public final int read()
			throws IOException
		{
			return this.mime.read();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/25
		 */
		@Override
		public final int read(byte[] __b)
			throws IOException
		{
			return this.mime.read(__b);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/03/05
		 */
		@Override
		public final int read(byte[] __b, int __o, int __l)
			throws IndexOutOfBoundsException, IOException, NullPointerException
		{
			return this.mime.read(__b, __o, __l);
		}
		
		/**
		 * This is a sub-reader which handles parsing of the MIME header and
		 * otherwise just passing the data to the MIMECharDecoder instance.
		 *
		 * @since 2018/11/25
		 */
		private final class __SubReader__
			extends Reader
		{
			/** The line-by-line reader for data. */
			protected final BufferedReader in;
			
			/** The input character buffer. */
			private final StringBuilder _sb =
				new StringBuilder(80);
			
			/** The current read in the buffer. */
			private int _at;
			
			/** The current limit of the buffer. */
			private int _limit;
			
			/** Did we read the header? */
			private boolean _didheader;
			
			/** Did we read the footer? */
			private boolean _didfooter;
			
			/**
			 * Initializes the sub-reader for the MIME data.
			 *
			 * @param __in The source reader.
			 * @throws NullPointerException On null arguments.
			 * @since 2018/11/24
			 */
			__SubReader__(Reader __in)
				throws NullPointerException
			{
				if (__in == null)
					throw new NullPointerException("NARG");
				
				this.in = new BufferedReader(__in, 80);
			}
			
			/**
			 * {@inheritDoc}
			 * @since 2018/11/25
			 */
			@Override
			public void close()
				throws IOException
			{
				this.in.close();
			}
			
			/**
			 * {@inheritDoc}
			 * @since 2018/11/25
			 */
			@Override
			public int read()
				throws IOException
			{
				// Read header?
				if (!this._didheader)
					this.__readHeader();
				
				// If the footer was read, this means EOF
				if (this._didfooter)
					return -1;
				
				// Need to read more from the buffer
				int at = this._at,
					limit = this._limit;
				if (at >= limit)
				{
					// Read line next
					if (!this.__readNext())
						return -1;
					
					// Re-read
					at = this._at;
					limit = this._limit;
				}
				
				// Read the next character
				int rv = this._sb.charAt(at);
				this._at = at + 1;
				return rv;
			}
			
			/**
			 * {@inheritDoc}
			 * @since 2018/11/25
			 */
			@Override
			public int read(char[] __c)
				throws IOException
			{
				if (__c == null)
					throw new NullPointerException("NARG");
				
				return this.read(__c, 0, __c.length);
			}
			
			/**
			 * {@inheritDoc}
			 * @since 2018/11/25
			 */
			@Override
			public int read(char[] __c, int __o, int __l)
				throws IOException
			{
				if (__c == null)
					throw new NullPointerException("NARG");
				if (__o < 0 || __l < 0 || (__o + __l) > __c.length)
					throw new IndexOutOfBoundsException("IOOB");
				
				// Read header?
				if (!this._didheader)
					this.__readHeader();
				
				// If the footer was read, this means EOF
				if (this._didfooter)
					return -1;
				
				// Where to read from
				StringBuilder sb = this._sb;
				int at = this._at,
					limit = this._limit;
				
				// Read in all characters
				int rv = 0;
				while (rv < __l)
				{
					// Need to read more?
					if (at >= limit)
					{
						// EOF?
						if (!this.__readNext())
							return (rv == 0 ? -1 : rv);
						
						// Re-read
						at = this._at;
						limit = this._limit;
					}
					
					// Read the next character
					__c[__o++] = sb.charAt(at++);
				}
				
				// Store new at position
				this._at = at;
				
				return rv;
			}
			
			/**
			 * Reads the header information.
			 *
			 * @throws IOException On read errors.
			 * @since 2018/11/25
			 */
			private final void __readHeader()
				throws IOException
			{
				BufferedReader in = this.in;
				
				// {@squirreljme.error BD23 Unexpected end of file while trying
				// to read MIME header.}
				String ln = in.readLine();
				if (ln == null)
					throw new IOException("BD23");
				
				// The header is in this format:
				// begin-base64 <unixmode> <filename>
				// {@squirreljme.error BD24 MIME encoded does not start with
				// MIME header.}
				if (!ln.startsWith("begin-base64"))
					throw new IOException("BD24");
				
				// UNIX mode?
				int fs = ln.indexOf(' ');
				if (fs >= 0)
				{
					int ss = ln.indexOf(' ', fs + 1);
					
					// Decode octal mode bits
					MIMEFileDecoder.this._mode = Integer.parseInt(
						ln.substring(fs + 1, (ss < 0 ? ln.length() : ss)), 8);
					
					// Filename?
					if (ss >= 0)
						MIMEFileDecoder.this._filename =
							ln.substring(ss + 1);
				}
				
				// Set as read
				this._didheader = true;
			}
			
			/**
			 * Reads the next line into the character.
			 *
			 * @return If a line was read.
			 * @throws IOException On read errors.
			 * @since 2018/11/25
			 */
			private final boolean __readNext()
				throws IOException
			{
				// {@squirreljme.error BD25 Unexpected EOF while read the MIME
				// file data.}
				String ln = this.in.readLine();
				if (ln == null)
					throw new IOException("BD25");
				
				// End of MIME data?
				if (ln.equals("===="))
				{
					// Footer was read, so EOF now
					this._didfooter = true;
					
					// Was EOF
					return false;
				}
				
				// Fill buffer
				StringBuilder sb = this._sb;
				sb.setLength(0);
				sb.append(ln);
				
				// Set properties
				this._at = 0;
				this._limit = ln.length();
				
				// Was not EOF
				return true;
			}
		}
	}
	
	/**
	 * This is a reader which can read from a string.
	 *
	 * This class is not thread safe.
	 *
	 * @since 2018/11/04
	 */
	public static class StringReader
		extends Reader
	{
		/** The string to read from. */
		protected final String string;
		
		/** The string length. */
		protected final int length;
		
		/** The current position. */
		private int _at;
		
		/**
		 * Initializes the reader.
		 *
		 * @param __s The input string.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/04
		 */
		public StringReader(String __s)
			throws NullPointerException
		{
			if (__s == null)
				throw new NullPointerException("NARG");
			
			this.string = __s;
			this.length = __s.length();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/04
		 */
		@Override
		public void close()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/04
		 */
		@Override
		public int read(char[] __c, int __o, int __l)
			throws IndexOutOfBoundsException, NullPointerException
		{
			if (__c == null)
				throw new NullPointerException("NARG");
			if (__o < 0 || __l < 0 || (__o + __l) > __c.length)
				throw new IndexOutOfBoundsException("IOOB");
			
			// Determine the current position and string length
			int at = this._at,
				length = this.length,
				left = length - at;
			
			// EOF?
			if (at >= length)
				return -1;
			
			// Can only read so many characters
			String string = this.string;
			int max = Math.min(__l, left),
				limit = at + max;
			for (int o = __o; at < limit; at++, o++)
				__c[o] = string.charAt(at);
			
			// Set position for next time
			this._at = at;
			
			return max;
		}
	}
}

