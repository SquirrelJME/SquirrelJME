/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Front end bindings.
 * 
 * @since 2024/09/04
 */

#ifndef SQUIRRELJME_FRONTEND_H
#define SQUIRRELJME_FRONTEND_H

#include "sjme/stdTypes.h"
#include "sjme/error.h"
#include "sjme/multithread.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_FRONTEND_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * A wrapper used by front ends, which is reserved for use, which stores a
 * natively bound object accordingly as needed.
 * 
 * @since 2023/12/06
 */
typedef sjme_pointer sjme_frontEndWrapper;

/**
 * Any data that is needed by the front end, which is reserved for use.
 *
 * @since 2023/12/14
 */
typedef sjme_pointer sjme_frontEndData;

/**
 * The action to perform when binding.
 * 
 * @since 2024/09/04
 */
typedef enum sjme_frontEnd_bindAction
{	
	/** Obtain the binding. */
	SJME_FRONTEND_BIND,
	
	/** Release the binding. */
	SJME_FRONTEND_RELEASE,
	
	/** The number of actions. */
	SJME_FRONTEND_NUM_BIND_ACTION,
} sjme_frontEnd_bindAction;

/**
 * The type of binding this is.
 * 
 * @since 2024/09/04
 */
typedef enum sjme_frontEnd_bindType
{
	/**
	 * Strong reference to the binding, this means that the data referred
	 * to by the front end will not disappear.
	 * 
	 * An example of a strong reference would be a Java callback that needs
	 * both it's own object along with the native structure to always exist.
	 */
	SJME_FRONTEND_STRONG,
	
	/**
	 * Weak reference to the binding, this means that the data referred to
	 * by the front end may disappear in the front end, for example if
	 * the front end has a garbage collection scheme.
	 * 
	 * An example of a weak reference would be a ScritchUI type that needs
	 * access from a garbage collected virtual machine, such as the
	 * emulator/standalone variant of SquirrelJME, in that the object this
	 * points to only exists to point back to a native structure. To continue
	 * the graphics example, a PencilBracket only needs to exist during
	 * drawing and once that gets garbage collected the native structure can
	 * be freed if nothing else refers to it. If a context is reused after
	 * the Java object was garbage collected, then that reference object
	 * will just be recreated to point to the native structure. Only once
	 * the native structure is no longer needed AND the Java object is garbage
	 * collected, then the native structure may be freed. Effectively, this
	 * allows objects in Java to exist and then stop existing, only accessing
	 * a native structure when it is needed and accessible on the Java end.
	 * Since memory management in SquirrelJME's NanoCoat is more manual
	 * with reference counting, there needs to be this information and
	 * linkage in this way to prevent native structures from being
	 * destroyed while a Java object still points to it.
	 */
	SJME_FRONTEND_WEAK,
	
	/** The number of bind types permitted. */
	SJME_FRONTEND_NUM_BIND_TYPE,
} sjme_frontEnd_bindType;

/**
 * This structure stores any front end data as needed.
 *
 * @since 2023/12/14
 */
typedef struct sjme_frontEnd sjme_frontEnd;

/**
 * This function is called when the front end binding needs to be obtained,
 * it may allocate and bind the data when this is called if applicable.
 * 
 * @param owner The owning object of the front end.
 * @param frontEnd The front end data.
 * @param resultData The resultant bound data, which points to the native
 * binding reference.
 * @param action The action being performed on the instance.
 * @return Any resultant error, if any.
 * @since 2024/09/04
 */
typedef sjme_errorCode (*sjme_frontEnd_binderFunc)(
	sjme_attrInNotNull sjme_pointer owner,
	sjme_attrInOutNotNull sjme_frontEndData* frontEnd,
	sjme_attrOutNotNull sjme_pointer* resultData,
	sjme_attrInValue sjme_frontEnd_bindAction action);

struct sjme_frontEnd
{
	/** Any wrapper as needed. */
	sjme_frontEndWrapper wrapper;

	/** Any data as needed. */
	sjme_frontEndData data;
	
	/** The lock when binding/releasing is being performed on this. */
	sjme_thread_spinLock bindLock;
	
	/** The binding type used. */
	sjme_frontEnd_bindType bindType;
	
	/** Binder to call when the front end data is needed. */
	sjme_frontEnd_binderFunc bindHandler;
};

/**
 * Wraps the given front end pointer.
 *
 * @param p The pointer to wrap.
 * @since 2023/12/08
 */
#define SJME_FRONT_END_WRAP(p) ((sjme_frontEndWrapper)(p))

/**
 * This function is called when the front end binding needs to be obtained,
 * it may allocate and bind the data when this is called if applicable.
 * 
 * @param owner The owning object of the front end.
 * @param frontEnd The front end data.
 * @param resultData The resultant bound data, which points to the native
 * binding reference.
 * @return Any resultant error, if any.
 * @since 2024/09/04
 */
sjme_errorCode sjme_frontEnd_bind(
	sjme_attrInNotNull sjme_pointer owner,
	sjme_attrInOutNotNull sjme_frontEndData* frontEnd,
	sjme_attrOutNotNull sjme_pointer* resultData);

/**
 * This is called when a front end reference needs to be released.
 * 
 * @param owner The owning object of the front end.
 * @param frontEnd The front end data.
 * @return Any resultant error, if any.
 * @since 2024/09/04
 */
sjme_errorCode sjme_frontEnd_release(
	sjme_attrInNotNull sjme_pointer owner,
	sjme_attrInOutNotNull sjme_frontEndData* frontEnd);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_FRONTEND_H
}
		#undef SJME_CXX_SQUIRRELJME_FRONTEND_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_FRONTEND_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_FRONTEND_H */
