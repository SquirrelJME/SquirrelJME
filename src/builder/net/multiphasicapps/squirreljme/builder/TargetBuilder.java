// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Random;
import java.util.ServiceLoader;
import net.multiphasicapps.squirreljme.basicassets.BasicAsset;
import net.multiphasicapps.squirreljme.exe.ExecutableOutput;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This is the base class for builders which can generate binaries for a given
 * target.
 *
 * @since 2016/07/22
 */
public abstract class TargetBuilder
{
	/** Builder services. */
	private static final ServiceLoader<TargetBuilder> _SERVICES =
		ServiceLoader.<TargetBuilder>load(TargetBuilder.class);
	
	/** Supports the JIT? */
	protected final boolean canjit;
	
	/** Suggestions. */
	private final TargetSuggestion[] _suggestions;
	
	/**
	 * Initializes the target builder.
	 *
	 * @param __jit Is the JIT supported?
	 * @param __sugs The suggested build targets, a triplet followed by a
	 * short description.
	 * @since 2016/07/22
	 */
	public TargetBuilder(boolean __jit, String... __sugs)
	{
		// Set
		this.canjit = __jit;
		
		// Setup suggestions
		if (__sugs != null)
		{
			// Setup target
			int n = __sugs.length;
			TargetSuggestion[] tss = new TargetSuggestion[n >>> 1];
			this._suggestions = tss;
			
			// Fill suggestions
			for (int i = 0, j = 0; (i + 1) < n; i += 2)
				tss[j++] = new TargetSuggestion(new JITTriplet(__sugs[i]),
					__sugs[i + 1]);
		}
		
		// No suggestions
		else
			this._suggestions = new TargetSuggestion[0];
	}
	
	/**
	 * Links together a binary which is capable of running on the target and
	 * places it into the given ZIP. Other files that are important to the
	 * target system may also be included.
	 *
	 * @param __zsw The output ZIP file.
	 * @param __names The names of the namespaces to write.
	 * @param __blobs The namespace blob input streams.
	 * @param __conf The build configuration.
	 * @param __vmcp The virtual machine class path.
	 * @throws JITException On link errors.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/23
	 */
	public abstract void linkBinary(ZipStreamWriter __zsw, String[] __names,
		InputStream[] __blobs, BuildConfig __conf, String[] __vmcp)
		throws JITException, IOException, NullPointerException;
	
	/**
	 * This potentially modifies and sets the initial configuration state which
	 * is used for the JIT.
	 *
	 * After this is executed, the builder will associate a cache creator and
	 * a triplet.
	 *
	 * @param __conf The target configuration to use.
	 * @param __bc The build configuration.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public abstract void outputConfig(JITOutputConfig __conf, BuildConfig __bc)
		throws NullPointerException;
	
	/**
	 * Is the given configuration supported?
	 *
	 * @param __conf The configuration to target.
	 * @return {@code true} if it is supported.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public abstract boolean supportsConfig(BuildConfig __conf)
		throws NullPointerException;
	
	/**
	 * When determining the packages for inclusion, for a given configuration
	 * always include packages that contain a matching
	 * {@code X-SquirrelJME-Target} manifest key value.
	 *
	 * @param __conf The configuration to target.
	 * @return The inclusion group to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/23
	 */
	public abstract String targetPackageGroup(BuildConfig __conf)
		throws NullPointerException;
	
	/**
	 * Adds standard system properties to the binary to be linked.
	 *
	 * @param __conf The build configuration.
	 * @param __eo The executable output to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/24
	 */
	public final void addStandardSystemProperties(BuildConfig __conf,
		ExecutableOutput __eo)
		throws NullPointerException
	{
		// Check
		if (__conf == null || __eo == null)
			throw new NullPointerException("NARG");
		
		// Some build properties
		__eo.addSystemProperty("net.multiphasicapps.squirreljme.buildtime",
			Long.toString(System.currentTimeMillis()));
		JITTriplet triplet;
		__eo.addSystemProperty("net.multiphasicapps.squirreljme.triplet",
			(triplet = __conf.triplet()).toString());
		__eo.addSystemProperty("net.multiphasicapps.squirreljme.hastests",
			Boolean.toString(__conf.includeTests()));
		
		// Generate a cookie
		Random randa = new Random(System.currentTimeMillis()),
			randb = new Random(System.nanoTime()),
			randc = new Random((
				Objects.toString(System.getProperty("os.name")) +
				Objects.toString(System.getProperty("os.version"))).
				hashCode()),
			randd = new Random(System.identityHashCode(new Object()));
		__eo.addSystemProperty("net.multiphasicapps.squirreljme.cookie",
			String.format("0x%08x%08x%08x%08x", randa.nextInt(),
				randb.nextInt(), randc.nextInt(), randd.nextInt()));
		
		// Always prefer that UTF-8 be used regardless of the host OS
		__eo.addSystemProperty("microedition.encoding", "utf-8");
		
		// Java VM details
		__eo.addSystemProperty("java.version",
			"1.8");
		__eo.addSystemProperty("java.vendor",
			"Steven Gawroriski (Multi-Phasic Applications)");
		__eo.addSystemProperty("java.vendor.url",
			"http://multiphasicapps.net/");
		__eo.addSystemProperty("java.vm.version",
			__readFirstLine("squirreljme-version"));
		__eo.addSystemProperty("java.vm.vendor",
			"Steven Gawroriski (Multi-Phasic Applications)");
		__eo.addSystemProperty("java.vm.name",
			"SquirrelJME");
		__eo.addSystemProperty("java.vm.info",
			"SquirrelJME");
		
		// VM Specification
		__eo.addSystemProperty("java.vm.specification.name",
			"Java Virtual Machine Specification");
		__eo.addSystemProperty("java.vm.specification.version",
			"1.7");
		__eo.addSystemProperty("java.vm.specification.vendor",
			"Oracle Corporation");
		
		// Runtime Specification
		__eo.addSystemProperty("java.specification.name",
			"JSR 360");
		__eo.addSystemProperty("java.specification.vendor",
			"Oracle Corporation");
		__eo.addSystemProperty("java.specification.version",
			"8.0");
		
		// Some libraries may depend on these
		__eo.addSystemProperty("java.runtime.name",
			"SquirrelJME");
		__eo.addSystemProperty("java.runtime.version",
			"1.8");
		
		// The OS arch, name, and version are derived from the triplet
		// Although this might not be the best way, it is the simplet
		__eo.addSystemProperty("os.arch",
			triplet.architectureProperty());
		__eo.addSystemProperty("os.name",
			triplet.operatingSystem());
		__eo.addSystemProperty("os.version",
			triplet.operatingSystemVariant());
	}
	
	/**
	 * Adds the virtual machine classpath to the system property set.
	 *
	 * @param __conf The build configuration.
	 * @param __eo The executable output to use.
	 * @throws JITException If the classpath contains a NUL character.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/24
	 */
	public final void addVirtualMachineClassPath(BuildConfig __conf,
		ExecutableOutput __eo, String[] __vmcp)
		throws JITException, NullPointerException
	{
		// Check
		if (__conf == null || __eo == null || __vmcp == null)
			throw new NullPointerException("NARG");
		
		// Build target string
		int n = __vmcp.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++)
		{
			// {@squirreljme.error DW0s The VM classpath element contains the
			// NUL character.}
			String s = __vmcp[i];
			if (s.indexOf((char)0) >= 0)
				throw new JITException("DW0s");
			
			// Pad with NUL
			if (i > 0)
				sb.append((char)0);
			
			// Append
			sb.append(s);
		}
		
		// Add property
		__eo.addSystemProperty("net.multiphasicapps.squirreljme.vmclasspath",
			sb.toString());
	}	
	
	/**
	 * Is the JIT supported for this target?
	 *
	 * @return {@code true} if the JIT is supported.
	 * @since 2016/07/22
	 */
	public final boolean canJIT()
	{
		return this.canjit;
	}
	
	/**
	 * Creates a last run recording for use in debugging.
	 *
	 * @return The output stream of the last run.
	 * @throws IOException On read/write errors.
	 * @since 2016/07/26
	 */
	protected final OutputStream createLastRun()
		throws IOException
	{
		return Channels.newOutputStream(FileChannel.open(
			Paths.get("lastrun.rec"), StandardOpenOption.WRITE,
			StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
	}
	
	/**
	 * Sets up an target emulator builder which knows how to initialize an
	 * emulator (if one is available).
	 *
	 * The default implementation throws an exception if emulation is not
	 * supported.
	 *
	 * @param __conf The configuration to use.
	 * @return The emulator which was setup.
	 * @throws IllegalArgumentException If emulation is not supported.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/25
	 */
	public TargetEmulator emulate(BuildConfig __conf)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error DW0v Emulation is not supported for this
		// target.}
		throw new IllegalArgumentException("DW0v");
	}
	
	/**
	 * Returns the suggested targets.
	 *
	 * @return An array of suggested targets.
	 * @since 2016/07/22
	 */
	public final TargetSuggestion[] suggestedTargets()
	{
		return this._suggestions.clone();
	}
	
	/**
	 * Goes through all builders to find one that supports the given
	 * configuration.
	 *
	 * @param __conf The configuration to target.
	 * @return The found builder or {@code null} if none was found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public static final TargetBuilder findBuilder(BuildConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Lock
		ServiceLoader<TargetBuilder> services = _SERVICES;
		synchronized (services)
		{
			// Go through all of them
			for (TargetBuilder tb : services)
				if (tb.supportsConfig(__conf))
					return tb;
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Reads the first line from a basic asset file.
	 *
	 * @param __asset The asset to read.
	 * @return The first line of the asset.
	 * @throws JITException If the string could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/24
	 */
	private static String __readFirstLine(String __asset)
		throws JITException, NullPointerException
	{
		// Check
		if (__asset == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error DW0o Could not locate the specified asset.
		// (The asset name)}
		BasicAsset ba = BasicAsset.getAsset(__asset);
		if (ba == null)
			throw new JITException(String.format("DW0o %s", __asset));
		
		// Opens the input asset
		try (InputStream is = ba.open();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr))
		{
			// Read a line
			String rv = br.readLine();
			
			// {@squirreljme.error DW0q No line was read from the asset.
			// (The asset name)}
			if (rv == null)
				throw new JITException(String.format("DW0q %s", __asset));
			
			// {@squirreljme.error DW0r The read line from the asset was
			// blank. (The asset name)}
			rv = rv.trim();
			if (rv.length() <= 0)
				throw new JITException(String.format("DW0r %s", __asset));
			
			// Use it
			return rv;
		}
		
		// {@squirreljme.error DW0p Could not read the first line of the
		// given asset. (The asset name)}	
		catch (IOException e)
		{
			throw new JITException(String.format("DW0p %s", __asset), e);
		}
	}
}

