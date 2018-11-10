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

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import net.multiphasicapps.javac.CompilerException;
import net.multiphasicapps.javac.CompilerInput;
import net.multiphasicapps.javac.CompilerPathSet;
import net.multiphasicapps.javac.FilePathSet;
import net.multiphasicapps.tool.manifest.JavaManifest;

/**
 * This represents a project which exists solely in the filesystem and does
 * not have virtually generated parts.
 *
 * @since 2018/03/06
 */
public final class BasicSource
	extends Source
{
	/** The path to the source code root. */
	protected final Path root;
	
	/** The manifest for this source. */
	private volatile Reference<JavaManifest> _manifest;
	
	/**
	 * Initializes the project source.
	 *
	 * @param __name The name of the source.
	 * @param __p The path to the source code.
	 * @param __t The type of project this is.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public BasicSource(SourceName __name, Path __p, ProjectType __t)
		throws NullPointerException
	{
		super(__name, __t);
		
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.root = __p;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final CompilerPathSet pathSet(SourcePathSetType __spst)
		throws NullPointerException
	{
		if (__spst == null)
			throw new NullPointerException("NARG");
		
		// The same path set is used for compilation and for sources
		return new FilePathSet(this.root);
	}
	
	/**
	 * Returns the root directory of the project source code.
	 *
	 * @return The project source code root.
	 * @since 2017/11/28
	 */
	public final Path root()
	{
		return this.root;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final JavaManifest sourceManifest()
	{
		Reference<JavaManifest> ref = this._manifest;
		JavaManifest rv;
		
		if (ref == null || null == (rv = ref.get()))
			try (CompilerPathSet ps = this.pathSet(SourcePathSetType.SOURCE);
				InputStream in = ps.input("META-INF/MANIFEST.MF").open())
			{
				this._manifest = new WeakReference<>(
					(rv = new JavaManifest(in)));
			}
			
			// {@squirreljme.error AU01 Could not read the source manifest.}
			catch (IOException|CompilerException e)
			{
				throw new NoSuchSourceException("AU01", e);
			}
		
		return rv;
	}
}

