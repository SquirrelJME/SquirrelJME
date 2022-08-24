// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.doclet;

import com.sun.javadoc.ClassDoc;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.markdownwriter.MarkdownWriter;

/**
 * Represents a single processed clased within a file.
 *
 * @since 2022/08/24
 */
public final class ProcessedClass
{
	/** The name of this class. */
	protected final ClassName name;
	
	/** The class document used. */
	protected final ClassDoc classDoc;
	
	/** The doclet this is a part of. */
	private final Reference<MarkdownDoclet> _doclet;
	
	/** The class that is the parent of this one. */
	private volatile Reference<ProcessedClass> _parentClass;
	
	/** Where is this document located? */
	volatile Path _documentPath;
	
	/** Is this class implicit, as in part of this package? */
	volatile boolean _implicit;
	
	/** The class description. */
	private volatile String _description;
	
	/**
	 * Processes the given class.
	 * 
	 * @param __doclet Doclet state.
	 * @param __name The name of the class.
	 * @param __classDoc The class document.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/24
	 */
	public ProcessedClass(MarkdownDoclet __doclet, ClassName __name,
		ClassDoc __classDoc)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this._doclet = new WeakReference<>(__doclet);
		this.name = __name;
		this.classDoc = __classDoc;
	}
	
	/**
	 * Returns the class description.
	 * 
	 * @since 2022/08/24
	 */
	public String description()
	{
		String description = this._description;
		
		if (description == null || description.trim().isEmpty())
			return "NOT DOCUMENTED";
		
		return description;
	}
	
	/**
	 * Returns the doclet this is in.
	 * 
	 * @return The doclet this is in.
	 * @since 2022/08/24
	 */
	public MarkdownDoclet doclet()
	{
		MarkdownDoclet result = this._doclet.get();
		if (result == null)
			throw new Error("GCGC");
		return result;
	}
	
	/**
	 * Returns the parent of this class.
	 * 
	 * @return The parent class.
	 * @since 2022/08/24
	 */
	public ProcessedClass parent()
	{
		Reference<ProcessedClass> ref = this._parentClass;
		if (ref == null)
			return null;
			
		ProcessedClass parent = ref.get();
		if (parent == null)
			throw new Error("GCGC");
		return parent;
	}
	
	/**
	 * Performs first stage processing.
	 * 
	 * @since 2022/08/24
	 */
	public void stageOne()
	{
		ClassDoc classDoc = this.classDoc;
		MarkdownDoclet doclet = this.doclet();
		
		// Is there a description?
		this._description = Utilities.neatText(classDoc.commentText());
		
		// Determine classes this is a parent of
		ClassDoc[] innerClasses = classDoc.innerClasses(true);
		if (innerClasses != null)
			for (ClassDoc innerClass : innerClasses)
				doclet.processClass(innerClass)._parentClass =
					new WeakReference<>(this);
	}
	
	/**
	 * Writes the document to the output.
	 * 
	 * @param __writer The writer to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/24
	 */
	public void write(MarkdownWriter __writer)
		throws IOException, NullPointerException
	{
		if (__writer == null)
			throw new NullPointerException("NARG");
		
		// The name of the class
		__writer.header(true, 1, this.name.toRuntimeString());
	}
}
