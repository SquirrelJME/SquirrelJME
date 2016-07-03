// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import java.io.IOException;
import java.util.ServiceLoader;

/**
 * This factory is used to create instances of the JIT compiler which reads an
 * input class and produces output from them.
 *
 * This is used with the service loader.
 *
 * @since 2016/07/02
 */
public abstract class JITFactory
{
	/** Service for JIT factory lookup. */
	private static final ServiceLoader<JITFactory> _JIT_SERVICES =
		ServiceLoader.<JITFactory>load(JITFactory.class);
	
	/** OS modifiers. */
	private static final ServiceLoader<JITOSModifier> _OS_SERVICES =
		ServiceLoader.<JITOSModifier>load(JITOSModifier.class);
	
	/** The name of the architecture. */
	protected final String archname;
	
	/**
	 * Initializes the base factory.
	 *
	 * @param __arch The name of the architecture this targets.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/03
	 */
	public JITFactory(String __arch)
		throws NullPointerException
	{
		// Check
		if (__arch == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.archname = __arch;
	}
	
	/**
	 * Returns the default variant to use when targetting a given architecture.
	 *
	 * @return The default variant to use.
	 * @since 2016/07/03
	 */
	public abstract JITCPUVariant defaultArchitectureVariant();
	
	/**
	 * Returns the default endianess for this factory.
	 *
	 * @return The default endianess for this factory.
	 * @since 2016/07/03
	 */
	public abstract JITCPUEndian defaultEndianess();
	
	/**
	 * Produces a JIT which derives details from the given producer.
	 *
	 * @param __fp The producer for the JIT.
	 * @param __ns The class namespace.
	 * @param __src The source class file to JIT on.
	 * @return The JIT for the given producer.
	 * @since 2016/07/03
	 */
	protected abstract JIT produceJIT(JITFactory.Producer __fp,
		String __ns, InputStream __src);
	
	/**
	 * Checks whether the given endianess is supported.
	 *
	 * @param __end The endianess to check support for.
	 * @return {@code true} if the specified endianess is supported.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/03
	 */
	public abstract boolean supportsEndianess(JITCPUEndian __end)
		throws NullPointerException;
	
	/**
	 * Returns an array of variants which are supported by a given CPU.
	 *
	 * @return The array of variants supported by the given CPU.
	 * @since 2016/07/03
	 */
	public abstract JITCPUVariant[] variants();
	
	/**
	 * Returns the name of the architecture this compiles for.
	 *
	 * @return The name of the architecture.
	 * @since 2016/07/02
	 */
	public final String architectureName()
	{
		return this.archname;
	}
	
	/**
	 * Locates the given variant using the specified name.
	 *
	 * @param __s The name of the variant to find, if {@code "generic"} is
	 * used then the default variant is selected.
	 * @return The variant using the given name or {@code null} if it was not
	 * found.
	 * @since 2016/07/03
	 */
	public final JITCPUVariant getVariant(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// If generic, use default
		if (__s.equals("generic"))
			return defaultArchitectureVariant();
		
		// Go through all
		for (JITCPUVariant v : variants())
			if (__s.equals(v.variantName()))
				return v;
		
		// Unknown
		return null;
	}
	
	/**
	 * Creates a producer which is capable of creating the requested JIT to
	 * be used during class compilation.
	 *
	 * @param __arch The architecture to compile for.
	 * @param __archvar The variant of the architecture to use.
	 * @param __os The operating system to target.
	 * @return The producer for the given target, or {@code null} if it was not
	 * found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/03
	 */
	public static JITFactory.Producer createProducer(String __arch,
		String __archvar, String __os)
		throws NullPointerException
	{
		// Check
		if (__arch == null || __archvar == null || __os == null)
			throw new NullPointerException("NARG");
		
		// Does the architecture variant have an endian specified?
		int avc = __archvar.indexOf(',');
		JITCPUEndian endian = (avc >= 0 ? JITCPUEndian.of(
			__archvar.substring(avc + 1)) : null);
		if (avc >= 0)
			__archvar = __archvar.substring(0, avc);
		
		// Look for the operating system service for the requested OS
		JITOSModifier josm = null;
		ServiceLoader<JITOSModifier> osservices = _OS_SERVICES;
		synchronized (osservices)
		{
			for (JITOSModifier os : osservices)
				if (__arch.equals(os.architectureName()) &&
					__os.equals(os.operatingSystemName()))
				{
					if (josm == null)
					{
						josm = os;
						break;
					}
				}
		}
		
		// Missing?
		if (josm == null)
			return null;
		
		// Look for a JIT service for the given architecture+variant
		JITFactory fact = null;
		JITCPUVariant var = null;
		JITCPUEndian endy = null;
		ServiceLoader<JITFactory> jitservices = _JIT_SERVICES;
		synchronized (jitservices)
		{
			// Go through all JITs for architectures
			for (JITFactory jf : jitservices)
				if (__arch.equals(jf.architectureName()))
				{
					// Supports the given variant?
					String wantvar = (__archvar.equals("generic") ?
						josm.defaultArchitectureVariant() :
						jf.defaultArchitectureVariant().variantName());
					var = jf.getVariant(wantvar);
					if (var == null)
						continue;
					
					// Supports the requested endianess?
					JITCPUEndian wantend = (endian != null ? endian :
						(josm != null ? josm.defaultEndianess() :
						jf.defaultEndianess()));
					if (!jf.supportsEndianess(wantend))
						continue;
					endy = wantend;
					
					// Set it
					if (fact == null)
					{
						fact = jf;
						break;
					}
				}
		}
		
		// Not found, fail
		if (fact == null)
			return null;
		
		// Setup producer
		return new Producer(josm, fact, var, endy);
	}
	
	/**
	 * A producer for JIT instances (since there is only a single JIT per
	 * class).
	 *
	 * @since 2016/07/03
	 */
	public static final class Producer
	{
		/** The target operating system. */
		protected final JITOSModifier os;
		
		/** The factory to use. */
		protected final JITFactory factory;
		
		/** The architecture variant. */
		protected final JITCPUVariant archvar;
		
		/** The endian of the CPU. */
		protected final JITCPUEndian endian;
		
		/** The basic doublet. */
		protected final String doublet;
		
		/**
		 * Initializes the producer.
		 *
		 * @param __os The operating system used.
		 * @param __fact The factory to create a JIT for.
		 * @param __var The CPU variant to use.
		 * @param __end The endianess of the CPU.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/07/03
		 */
		private Producer(JITOSModifier __os, JITFactory __fact,
			JITCPUVariant __var, JITCPUEndian __end)
			throws NullPointerException
		{
			// Check
			if (__os == null || __fact == null || __var == null ||
				__end == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.os = __os;
			this.factory = __fact;
			this.archvar = __var;
			this.endian = __end;
			
			// Setup the double
			this.doublet = __fact.architectureName() + "+" +
				__var.variantName() + "," + __end.endianName() + "." +
				__os.operatingSystemName();
		}
		
		/**
		 * Returns the architecture variant to target.
		 *
		 * @return The architecture variant used.
		 * @since 2016/07/03
		 */
		public final JITCPUVariant architectureVariant()
		{
			return this.archvar;
		}
		
		/**
		 * Returns the dublet of the target, which does not contain the
		 * operating system variant.
		 *
		 * @return The doublet.
		 * @since 2016/07/03
		 */
		public final String doublet()
		{
			return this.doublet;
		}
		
		/**
		 * Returns the endian to target.
		 *
		 * @return The target endianess.
		 * @since 2016/07/03
		 */
		public final JITCPUEndian endian()
		{
			return this.endian;
		}
		
		/**
		 * Creates a JIT which reads the given input class
		 *
		 * @param __ns The namespace where the class resides, this should be
		 * the name of a JAR file.
		 * @param __is The class to produce code for.
		 * @return The JIT for this given class.
		 * @throws IOException On read/write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/07/03
		 */
		public final JIT produce(String __ns, InputStream __is)
			throws IOException, NullPointerException
		{
			// Check
			if (__ns == null || __is == null)
				throw new NullPointerException("NARG");
			
			// Create it
			return this.factory.produceJIT(this, __ns, __is);
		}
	}
}

