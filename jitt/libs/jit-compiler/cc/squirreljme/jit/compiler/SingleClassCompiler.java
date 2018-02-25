// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.compiler;

import cc.squirreljme.jit.classfile.ClassFile;
import cc.squirreljme.jit.classfile.Field;
import cc.squirreljme.jit.classfile.Method;
import cc.squirreljme.jit.library.Library;
import cc.squirreljme.jit.objectfile.CommonSectionNames;
import cc.squirreljme.jit.objectfile.DataProperties;
import cc.squirreljme.jit.objectfile.ObjectFile;
import cc.squirreljme.jit.objectfile.Section;
import cc.squirreljme.jit.objectfile.SectionFlag;

/**
 * This is a compiler which only transforms a single class that has been
 * input.
 *
 * @since 2018/02/23
 */
public final class SingleClassCompiler
	implements Runnable
{
	/** The class file to compile. */
	protected final ClassFile classfile;
	
	/** The library state. */
	protected final LibraryState libstate;
	
	/** The target object file. */
	protected final ObjectFile objectfile;
	
	/** Properties of the target compiler. */
	protected final TargetProperties targetproperties;
	
	/**
	 * Initializes the compiler for the class.
	 *
	 * @param __cf The input class to compile.
	 * @param __lib The state of the libraries being compiled.
	 * @param __of The object file to write to for compilation.
	 * @param __tp Properties of the target compiler.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/23
	 */
	public SingleClassCompiler(ClassFile __cf, LibraryState __lib,
		ObjectFile __of, TargetProperties __tp)
		throws NullPointerException
	{
		if (__cf == null || __lib == null || __of == null || __tp == null)
			throw new NullPointerException("NARG");
		
		this.classfile = __cf;
		this.libstate = __lib;
		this.objectfile = __of;
		this.targetproperties = __tp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/24
	 */
	@Override
	public final void run()
	{
		ClassFile classfile = this.classfile;
		ObjectFile objectfile = this.objectfile;
		TargetProperties targetproperties = this.targetproperties;
		DataProperties dataproperties = targetproperties.dataProperties();
		
		// Make sure the section for class table data exists
		Section classtable = objectfile.getSection(CommonSectionNames.CLASSES);
		if (classtable == null)
			classtable = objectfile.addSection(CommonSectionNames.CLASSES,
				SectionFlag.READ);
		
		if (true)
			throw new todo.TODO();
		
		// Export fields
		for (Field f : classfile.fields())
			__exportField(f);
		
		// Export methods
		for (Method m : classfile.methods())
			__exportMethod(m);
		
		throw new todo.TODO();
	}
	
	/**
	 * Exports the field in the class.
	 *
	 * @param __f The field in the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	private final void __exportField(Field __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
			
		ObjectFile objectfile = this.objectfile;
		
		throw new todo.TODO();
	}
	
	/**
	 * Exports the method in the class.
	 *
	 * @param __m The method in the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	private final void __exportMethod(Method __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
			
		ObjectFile objectfile = this.objectfile;
		
		throw new todo.TODO();
	}
}

