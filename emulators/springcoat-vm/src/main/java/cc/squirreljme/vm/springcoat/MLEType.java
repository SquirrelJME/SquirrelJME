// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.JarPackageObject;
import cc.squirreljme.vm.springcoat.brackets.TypeObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringClassNotFoundException;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.util.Objects;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.PrimitiveType;

/**
 * Functions for {@link TypeShelf}.
 *
 * @since 2020/06/18
 */
public enum MLEType
	implements MLEFunction
{
	/** {@link TypeShelf#binaryName(TypeBracket)}. */
	BINARY_NAME("binaryName:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/27
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.asVMObject(MLEType.__type(__args[0])
				.getSpringClass().name().binaryName().toString());
		}
	}, 
	
	/** {@link TypeShelf#binaryPackageName(TypeBracket)}. */
	BINARY_PACKAGE_NAME("binaryPackageName:(Lcc/squirreljme/jvm/" +
		"mle/brackets/TypeBracket;)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.asVMObject(MLEType.__type(__args[0])
				.getSpringClass().name().binaryName().inPackage()
				.toString());
		}
	},
	
	/** {@link TypeShelf#classToType(Class)}. */
	CLASS_TO_TYPE("classToType:(Ljava/lang/Class;)" +
		"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEType.__simple(__args[0]).fieldByField(
				__thread.resolveClass(new ClassName("java/lang/Class"))
				.lookupField(false, "_type",
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;")).get();
		}
	},
	
	/** {@link TypeShelf#equals(TypeBracket, TypeBracket)}. */
	EQUALS("equals:(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;" +
		"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return Objects.equals(
				MLEType.__type(__args[0]).getSpringClass(),
				MLEType.__type(__args[1]).getSpringClass());
		}
	},
	
	/** {@link TypeShelf#findType(String)}. */
	FIND_TYPE("findType:(Ljava/lang/String;)" +
		"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringObject name = MLEType.__notNullObject(__args[0]);
			
			try
			{
				return new TypeObject(__thread.loadClass(
					__thread.<String>asNativeObject(String.class, name)));
			}
			
			// Since the method returns null when not found, we want to return
			// this here
			catch (SpringClassNotFoundException e)
			{
				return null;
			}
		}
	},
	
	/** {@link TypeShelf#inJar(TypeBracket)}. */
	IN_JAR("inJar:(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)" +
		"Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new JarPackageObject(MLEType.__type(__args[0])
				.getSpringClass().inJar());
		}
	},
	
	/** {@link TypeShelf#isArray(TypeBracket)}. */
	IS_ARRAY("isArray:(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEType.__type(__args[0]).getSpringClass().isArray();
		}
	},
	
	/** {@link TypeShelf#isInterface(TypeBracket)}. */
	IS_INTERFACE("isInterface:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEType.__type(__args[0]).getSpringClass().flags()
				.isInterface();
		}
	},
	
	/** {@link TypeShelf#isPrimitive(TypeBracket)}. */
	IS_PRIMITIVE("isPrimitive:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEType.__type(__args[0]).getSpringClass().name()
				.isPrimitive();
		}
	},
	
	/** {@link TypeShelf#objectType(Object)}. */
	OBJECT_TYPE("objectType:(Ljava/lang/Object;)" +
		"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new TypeObject(__thread.loadClass(
				MLEType.__notNullObject(__args[0]).type().name().toString()));
		}
	},
	
	/** {@link TypeShelf#runtimeName(TypeBracket)}. */
	RUNTIME_NAME("runtimeName:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEType.__type(__args[0]).getSpringClass()
				.name().toRuntimeString();
		}
	},
	
	/** {@link TypeShelf#superClass(TypeBracket)}. */
	SUPER_CLASS("superClass:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;)Lcc/squirreljme/jvm/mle/brackets/TypeBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringClass superClass = MLEType.__type(__args[0]).getSpringClass()
				.superClass();
			
			if (superClass == null)
				return SpringNullObject.NULL;
			return new TypeObject(superClass);
		}
	},
	
	/** {@link TypeShelf#typeOfBoolean()}. */
	TYPE_OF_BOOLEAN("typeOfBoolean:()Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new TypeObject(__thread.loadClass(
				ClassName.fromPrimitiveType(PrimitiveType.BOOLEAN)));
		}
	},
	
	/** {@link TypeShelf#typeOfByte()}. */
	TYPE_OF_BYTE("typeOfByte:()Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new TypeObject(__thread.loadClass(
				ClassName.fromPrimitiveType(PrimitiveType.BYTE)));
		}
	},
	
	/** {@link TypeShelf#typeOfCharacter()}. */
	TYPE_OF_CHARACTER("typeOfCharacter:()Lcc/squirreljme/jvm/mle/" +
		"brackets/TypeBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new TypeObject(__thread.loadClass(
				ClassName.fromPrimitiveType(PrimitiveType.CHARACTER)));
		}
	},
	
	/** {@link TypeShelf#typeOfDouble()}. */
	TYPE_OF_DOUBLE("typeOfDouble:()Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new TypeObject(__thread.loadClass(
				ClassName.fromPrimitiveType(PrimitiveType.DOUBLE)));
		}
	},
	
	/** {@link TypeShelf#typeOfFloat()}. */
	TYPE_OF_FLOAT("typeOfFloat:()Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new TypeObject(__thread.loadClass(
				ClassName.fromPrimitiveType(PrimitiveType.FLOAT)));
		}
	},
	
	/** {@link TypeShelf#typeOfInteger()}. */
	TYPE_OF_INTEGER("typeOfInteger:()Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new TypeObject(__thread.loadClass(
				ClassName.fromPrimitiveType(PrimitiveType.INTEGER)));
		}
	},
	
	/** {@link TypeShelf#typeOfLong()}. */
	TYPE_OF_LONG("typeOfLong:()Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new TypeObject(__thread.loadClass(
				ClassName.fromPrimitiveType(PrimitiveType.LONG)));
		}
	},
	
	/** {@link TypeShelf#typeOfShort()}. */
	TYPE_OF_SHORT("typeOfShort:()Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new TypeObject(__thread.loadClass(
				ClassName.fromPrimitiveType(PrimitiveType.SHORT)));
		}
	},
	
	/** {@link TypeShelf#typeToClass(TypeBracket)}. */
	TYPE_TO_CLASS("typeToClass:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;)Ljava/lang/Class;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.asVMObject(MLEType.__type(__args[0])
				.getSpringClass());
		}
	},
	
	/* End. */
	;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/18
	 */
	MLEType(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/18
	 */
	@Override
	public String key()
	{
		return this.key;
	}
	
	/**
	 * Checks if the object is {@code null}.
	 * 
	 * @param __object The object to check.
	 * @return The object.
	 * @throws SpringMLECallError If is null.
	 * @since 2020/06/22
	 */
	static SpringObject __notNullObject(Object __object)
		throws SpringMLECallError
	{
		if (__object == null || SpringNullObject.NULL == __object)
			throw new SpringMLECallError("Null object.");
		
		return (SpringObject)__object;
	}
	
	/**
	 * Checks if this is a simple object.
	 * 
	 * @param __object The object to check.
	 * @return The simple object.
	 * @throws SpringMLECallError If not a simple object.
	 * @since 2020/06/22
	 */
	static SpringSimpleObject __simple(Object __object)
	{
		if (!(__object instanceof SpringSimpleObject))
			throw new SpringMLECallError("Not a SpringSimpleObject.");
		
		return (SpringSimpleObject)__object; 
	}
	
	/**
	 * Checks if this is a {@link TypeObject}.
	 * 
	 * @param __object The object to check.
	 * @return As a {@link TypeObject} if this is one.
	 * @throws SpringMLECallError If this is not a {@link TypeObject}.
	 * @since 2020/06/22
	 */
	static TypeObject __type(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof TypeObject))
			throw new SpringMLECallError("Not a TypeObject.");
		
		return (TypeObject)__object; 
	}
}
