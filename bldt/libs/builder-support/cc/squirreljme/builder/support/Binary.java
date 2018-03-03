// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

import cc.squirreljme.jit.library.Library;
import cc.squirreljme.kernel.suiteinfo.DependencyInfo;
import cc.squirreljme.kernel.suiteinfo.MatchResult;
import cc.squirreljme.kernel.suiteinfo.ProvidedInfo;
import cc.squirreljme.kernel.suiteinfo.SuiteInfo;
import cc.squirreljme.kernel.suiteinfo.SuiteName;
import cc.squirreljme.kernel.suiteinfo.SuiteVendor;
import cc.squirreljme.kernel.suiteinfo.SuiteVersion;
import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.attribute.FileTime;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeSet;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;
import net.multiphasicapps.zip.blockreader.FileChannelBlockAccessor;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This class represents a binary which has been loaded by the binary manager.
 *
 * @since 2017/10/31
 */
public final class Binary
{
	/** The name of this binary. */
	protected final SourceName name;
	
	/** The source code for this binary, may be null if there is none. */
	protected final Source source;
	
	/** The path to the binary for this executable. */
	protected final Path path;
	
	/** The cached manifest for this entry. */
	private volatile Reference<JavaManifest> _manifest;
	
	/** The suite information. */
	private volatile Reference<SuiteInfo> _suiteinfo;
	
	/** The wrapped JIT library. */
	private volatile Reference<Library> _library;
	
	/**
	 * Initializes the binary.
	 *
	 * @param __name The name of this binary.
	 * @param __source The source of this binary, may be {@code null} if there
	 * is no source.
	 * @throws InvalidBinaryException If the tiven 
	 * @throws NoSuchBinaryException If the given binary does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/02
	 */
	Binary(SourceName __name, Source __source, Path __path)
		throws InvalidBinaryException, NoSuchBinaryException,
			NullPointerException
	{
		if (__name == null || __path == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.source = __source;
		this.path = __path;
	}
	
	/**
	 * Returns the input stream over the binary itself.
	 *
	 * @return The input stream over the binary.
	 * @throws IOException On read errors.
	 * @since 2017/11/17
	 */
	public final InputStream inputStream()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns if the binary is newer.
	 *
	 * @return If the binary is newer.
	 * @since 2017/11/06
	 */
	public final boolean isBinaryNewer()
	{
		Source source = this.source;
		return lastModifiedTime() >=
			(source != null ? source.lastModifiedTime() : Long.MIN_VALUE);
	}
	
	/**
	 * Returns if the source code is newer.
	 *
	 * @return If the source is newer.
	 * @since 2017/11/06
	 */
	public final boolean isSourceNewer()
	{
		return !isBinaryNewer();
	}
	
	/**
	 * Returns the time that the binary was last modified.
	 *
	 * @return The time the binary was last modified.
	 * @since 2017/11/06
	 */
	public final long lastModifiedTime()
	{
		// The file might not actually exist if it has not been built
		try
		{
			FileTime t = Files.getLastModifiedTime(this.path);
			if (t != null)
				return t.toMillis();
			return Long.MIN_VALUE;
		}
		
		// File does not exist or another error, so unknown an unknown time
		catch (IOException e)
		{
			return Long.MIN_VALUE;
		}
	}
	
	/**
	 * Returns the library to use as input for the JIT compiler.
	 *
	 * @return The library used for JIT compilation.
	 * @since 2018/02/21
	 */
	public final Library library()
	{
		Reference<Library> ref = this._library;
		Library rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._library = new WeakReference<>(
				(rv = new BinaryLibrary(this)));
		
		return rv;
	}
	
	/**
	 * Returns the manifest for this binary.
	 *
	 * @return The manifest for this project.
	 * @since 2017/11/05
	 */
	public final JavaManifest manifest()
	{
		// Approximate the manifest to use
		if (isSourceNewer())
			return this.source.manifest();
		
		// Open the binary instead
		try (ZipBlockReader zip = zipBlock())
		{
			try (InputStream in = zip.open("META-INF/MANIFEST.MF"))
			{
				return new JavaManifest(in);
			}
		}
		
		// {@squirreljme.error AU01 Could not read the binary manifest.}
		catch (IOException e)
		{
			throw new InvalidBinaryException("AU01", e);
		}
	}
	
	/**
	 * Returns a dependency match result which contains the results of a
	 * dependency match between the provided dependencies and the provided
	 * dependencies for this project.
	 *
	 * @param __d The input dependencies to check.
	 * @return The result of the match.
	 * @throws NullPointerException On null arguments.
	 * @sine 2017/11/30
	 */
	public final MatchResult matchedDependencies(DependencyInfo __d)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		return __d.match(this.suiteInfo().provided());
	}
	
	/**
	 * Returns the name of the project.
	 *
	 * @return The project name.
	 * @since 2017/11/02
	 */
	public final SourceName name()
	{
		return this.name;
	}
	
	/**
	 * Returns the path to the binary.
	 *
	 * @return The binary path.
	 * @since 2017/11/28
	 */
	public final Path path()
	{
		return this.path;
	}
	
	/**
	 * Returns the source project that this binary is built from.
	 *
	 * @return The source project or {@code null} if there is no source.
	 * @since 2017/11/06
	 */
	public final Source source()
	{
		return this.source;
	}
	
	/**
	 * Returns the suite information for this binary.
	 *
	 * @return The binary suite information.
	 * @since 2017/11/29
	 */
	public final SuiteInfo suiteInfo()
	{
		// Use binary information
		if (isBinaryNewer())
		{
			Reference<SuiteInfo> ref = this._suiteinfo;
			SuiteInfo rv;
		
			if (ref == null || null == (rv = ref.get()))
				this._suiteinfo = new WeakReference<>((rv =
					new SuiteInfo(this.manifest())));
			
			return rv;
		}
		
		// {@squirreljme.error AU02 Cannot get suite information for the
		// binary because there is no source code.}
		Source source = this.source;
		if (source == null)
			throw new InvalidBinaryException("AU02");
		return this.source.suiteInfo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/28
	 */
	@Override
	public final String toString()
	{
		return this.name.toString();
	}
	
	/**
	 * Returns the type of project that this is.
	 *
	 * @return The project type.
	 * @since 2017/11/26
	 */
	public final ProjectType type()
	{
		JavaManifestAttributes attr = this.manifest().getMainAttributes();
		
		// Midlet?
		if (attr.definesValue("midlet-name"))
			return ProjectType.MIDLET;
		
		// APIs are like liblets but have a special flag to them
		else if ("true".equals(attr.getValue("x-squirreljme-isapi")))
			return ProjectType.API;
		
		// Otherwise everything else is a liblet
		return ProjectType.LIBLET;
	}
	
	/**
	 * Opens the binary as a ZIP file for reading the contents.
	 *
	 * @return The reader for the ZIP file as a block.
	 * @throws IOException On read errors.
	 * @since 2017/11/06
	 */
	public final ZipBlockReader zipBlock()
		throws IOException
	{
		// {@squirreljme.error AU03 Cannot get the ZIP for this binary because
		// it is out of date. (The name of this binary)}
		if (isSourceNewer())
			throw new OutOfDateBinaryException(
				String.format("AU03 %s", this.name));
		
		// Open it
		return new ZipBlockReader(new FileChannelBlockAccessor(this.path));
	}
	
	/**
	 * Opens the binary as a ZIP stream for reading the contents.
	 *
	 * @return The stream over the ZIP's contents.
	 * @throws IOException On read errors.
	 * @since 2017/11/02
	 */
	public final ZipStreamReader zipStream()
		throws IOException
	{
		// {@squirreljme.error AU04 Cannot get the ZIP for this binary because
		// it is out of date. (The name of this binary)}
		if (isSourceNewer())
			throw new OutOfDateBinaryException(
				String.format("AU04 %s", this.name));
		
		throw new todo.TODO();
	}
}

