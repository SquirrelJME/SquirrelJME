// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.midlet;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.RawJarPackageBracketInputStream;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import javax.microedition.midlet.MIDlet;
import net.multiphasicapps.collections.UnmodifiableCollection;

/**
 * Represents a source type for a given manifest for any given {@link MIDlet}.
 *
 * @since 2022/03/04
 */
public enum ManifestSourceType
{
	/** JAD Manifest. */
	JAD
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/03/04
		 */
		@Override
		public InputStream manifestStream(JarPackageBracket __ourJar)
			throws IOException, NullPointerException
		{
			if (__ourJar == null)
				throw new NullPointerException("NARG");
			
			// Make assumptions on what our JAD is called.
			String ourPath = JarPackageShelf.libraryPath(__ourJar);
			String assumedJad;
			if (ourPath.endsWith(".jar") || ourPath.endsWith(".JAR"))
			{
				boolean caps = ourPath.endsWith(".JAR");
				
				assumedJad = ourPath.substring(0,
					ourPath.length() - 4) + (caps ? ".JAD" : ".jad");
			}
			else
				assumedJad = ourPath + ".jad";
			
			// Find the matching assumed JAD
			for (JarPackageBracket otherJar : JarPackageShelf.libraries())
			{
				// If the other JAR is a match for the path we assumed, then
				// we should attempt reading the JAD to get properties from
				String otherPath = JarPackageShelf.libraryPath(otherJar);
				if (assumedJad.equals(otherPath))
					return JarPackageShelf.openResource(otherJar,
					"META-INF/MANIFEST.MF");
				
			}
			
			// Not found
			return null;
		}
	},
	
	/** KJX Embedded JAD manifest. */
	KJX_EMBEDDED_JAD
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/03/04
		 */
		@Override
		public String encoding()
		{
			// For some reason, Shift-JIS is used for the manifests instead
			// of what should be UTF-8...
			return "shift-jis";
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2022/03/04
		 */
		@Override
		public InputStream manifestStream(JarPackageBracket __ourJar)
			throws IOException, NullPointerException
		{
			if (__ourJar == null)
				throw new NullPointerException("NARG");
			
			// If we are not a KJX, then do not bother trying!
			String ourPath = JarPackageShelf.libraryPath(__ourJar);
			if (!ourPath.endsWith(".kjx") && !ourPath.endsWith(".KJX"))
				return null;
			
			// We need to parse the KJX to handle this
			try (DataInputStream raw = new DataInputStream(
				new RawJarPackageBracketInputStream(__ourJar)))
			{
				// Check magic number
				byte[] magic = new byte[3];
				raw.readFully(magic);
				if (magic[0] != 'K' || magic[1] != 'J' || magic[2] != 'X')
					throw new IOException("ZZ4j");
				
				// Position of the JAD
				int jadPos = raw.readUnsignedByte();
				
				// Name of the current KJX (ignored, we do not care for it)
				int kjxNameLen = raw.readUnsignedByte();
				raw.skipBytes(kjxNameLen);
				
				// The size of the JAD
				int jadLen = raw.readUnsignedShort();
				
				// Ignore the name of the JAD, we do not care
				int jadNameLen = raw.readUnsignedByte();
				raw.skipBytes(jadNameLen);
				
				// Determine the relative position to where the JAD should
				// start and how much we have read, then skip it
				int currentAt = 3 + 1 + 1 + kjxNameLen + 2 + 1 + jadNameLen;
				if (currentAt < jadPos)
					raw.skipBytes(jadPos - currentAt);
				
				// Read in the entire JAD
				byte[] jad = new byte[jadLen];
				raw.readFully(jad);
				
				// Use whatever we read as the manifest data
				return new ByteArrayInputStream(jad);
			}
		}
	},
	
	/** JAR Manifest. */
	JAR
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/03/04
		 */
		@Override
		public InputStream manifestStream(JarPackageBracket __ourJar)
			throws IOException, NullPointerException
		{
			if (__ourJar == null)
				throw new NullPointerException("NARG");
			
			// Load the internal manifest
			return JarPackageShelf.openResource(__ourJar,
				"META-INF/MANIFEST.MF");
		}
	},
	
	/* End. */
	;
	
	/** The available values. */
	public static final Collection<ManifestSourceType> VALUES =
		UnmodifiableCollection.of(Arrays.asList(ManifestSourceType.values()));
	
	/** The number of available values. */
	public static final int COUNT =
		ManifestSourceType.VALUES.size();
	
	/**
	 * Returns the manifest stream.
	 * 
	 * @param __ourJar Our current JAR.
	 * @return The stream used to read the manifest or {@code null} if it
	 * could not be obtained.
	 * @throws IOException If it could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/03/04
	 */
	public abstract InputStream manifestStream(JarPackageBracket __ourJar)
		throws IOException, NullPointerException;
	
	/**
	 * Returns the encoding to use for the manifest.
	 * 
	 * @return The encoding to use for the manifest.
	 * @since 2022/03/04
	 */
	public String encoding()
	{
		return "utf-8";
	}
}
