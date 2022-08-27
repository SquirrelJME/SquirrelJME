// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.doclet;

import cc.squirreljme.runtime.cldc.util.ReferenceList;
import com.sun.javadoc.ClassDoc;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.collections.UnmodifiableList;
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
	
	/** The super class. */
	private volatile Reference<ProcessedClass> _superClass;
	
	/** Interface classes. */
	private volatile ReferenceList<ProcessedClass> _interfaceClasses;
	
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
	 * Returns the interface classes which are used.
	 * 
	 * @return The interface classes.
	 * @since 2022/08/27
	 */
	public List<ProcessedClass> interfaceClasses()
	{
		ReferenceList<ProcessedClass> interfaces = this._interfaceClasses;
		
		if (interfaces == null)
			return null;
		
		return UnmodifiableList.of(interfaces);
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
		
		// Super class?
		ClassDoc superClass = classDoc.superclass();
		if (superClass != null)
			this._superClass = new WeakReference<>(
				doclet.processClass(superClass));
		
		// Interface classes
		ClassDoc[] interfaces = classDoc.interfaces();
		if (interfaces != null && interfaces.length > 0)
		{
			ReferenceList<ProcessedClass> result = ReferenceList.of(
				new ArrayList<>(interfaces.length));
			
			for (ClassDoc classy : interfaces)
				result.add(doclet.processClass(classy));
			
			this._interfaceClasses = result;
		}
	}
	
	/**
	 * Returns the superclass of this class.
	 * 
	 * @return The super class.
	 * @since 2022/08/27
	 */
	public ProcessedClass superClass()
	{
		Reference<ProcessedClass> ref = this._superClass;
		if (ref == null)
			return null;
			
		ProcessedClass superClass = ref.get();
		if (superClass == null)
			throw new Error("GCGC");
		return superClass;
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
		
		// For the next group...
		ProcessedClass parent = this.parent();
		ProcessedClass superClass = this.superClass();
		List<ProcessedClass> interfaceClasses = this.interfaceClasses();
		
		// Super class, interfaces, and such
		if (superClass != null || parent != null ||
			(interfaceClasses != null && !interfaceClasses.isEmpty()))
		{
			__writer.listStart();
			
			// Super class?
			if (superClass != null)
			{
				__writer.append(true, "Super: ");
				Utilities.writerLinkTo(__writer, this, superClass);
				
				__writer.listNext();
			}
			
			// Parent class?
			if (parent != null)
			{
				__writer.append(true, "Parent: ");
				Utilities.writerLinkTo(__writer, this, parent);
				
				__writer.listNext();
			}
			
			// Interfaces?
			if (interfaceClasses != null && !interfaceClasses.isEmpty())
			{
				// Note it
				__writer.append(true, "Interfaces:");
				
				// Stop listing
				__writer.listStart();
				
				// Write them all out
				for (ProcessedClass classy : interfaceClasses)
				{
					Utilities.writerLinkTo(__writer, this, classy);
					__writer.listNext();
				}
				
				// End list
				__writer.listEnd();
			}
			
			// End 
			__writer.listEnd();
			
			// Make sure paragraph follows
			__writer.paragraph();
		}
		
		// Write description of the class
		__writer.print(true, this._description);
	}
}
