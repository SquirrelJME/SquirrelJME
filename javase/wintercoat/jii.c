/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * Implements the Java invoke interface calls.
 *
 * @since 2016/10/19
 */

#include "jii.h"

/**
 * {@inheritDoc}
 * @since 2016/10/19
 */
jint JNICALL WC_JII_AttachCurrentThread(JavaVM* pvm, void** penv, void* pargs)
{
	WC_JavaVM* jvm;
	WC_JNIEnv* env;
	WC_JNIEnv* rv;
	pthread_t self, *new;
	pthread_mutex_t* emutex;
	
	// {@squirreljme.error WC0d The owning virtual machine is NULL.}
	WC_ASSERT("WC0d", pvm == NULL);
	
	// {@squirreljme.error WC0e No output environment was specified.}
	WC_ASSERT("WC0e", penv == NULL);
	
	// Get current thread
	self = pthread_self();
	
	// Cast
	jvm = (WC_JavaVM*)pvm;
	
	// {@squirreljme.error WC0g Failed to lock the VM mutex when attaching the
	// current thread.}
	WC_ASSERT("WC0g", pthread_mutex_lock(jvm->furry.mutex) != 0);
	
	// Go through all threads to see if the thread was already allocated
	rv = NULL;
	for (env = jvm->furry.envchain; env != NULL; env = env->furry.next)
		if (pthread_equal(self, *env->furry.thread))
		{
			rv = env;
			break;
		}
	
	// If the thread was not bound then create an environment for it
	if (rv == NULL)
	{
		// Allocate
		rv = WC_ForcedMalloc(sizeof(*rv));
		
		// {@squirreljme.error WC0h Could not initialize the thread mutex.}
		emutex = WC_ForcedMalloc(sizeof(*emutex));
		WC_ASSERT("WC0h", pthread_mutex_init(emutex, NULL) != 0);
		rv->furry.mutex = emutex;
		
		// Set
		rv->furry.vm = jvm;
		new = WC_ForcedMalloc(sizeof(*new));
		*new = self;
		rv->furry.thread = new;
		
		// Setup function arguments
		WC_TODO();
		
		// Link into the chain
		rv->furry.next = jvm->furry.envchain;
		jvm->furry.envchain = rv;
		
		// Use this one
		(*penv) = rv;
	}
	
	// {@squirreljme.error EC0h Failed to unlock the VM mutex when attaching
	// the current thread.}
	WC_ASSERT("WC0h", pthread_mutex_unlock(jvm->furry.mutex) != 0);
	
	// Ok
	return JNI_OK;
}

jint JNICALL WC_JII_AttachCurrentThreadAsDaemon(JavaVM* pvm, void** penv,
	void* pargs)
{
	WC_TODO();
	return 1;
}

jint JNICALL WC_JII_DestroyJavaVM(JavaVM* pvm)
{
	WC_TODO();
	return 1;
}

jint JNICALL WC_JII_DetachCurrentThread(JavaVM* pvm)
{
	WC_TODO();
	return 1;
}

jint JNICALL WC_JII_GetEnv(JavaVM* pvm, void** penv, jint pversion)
{
	WC_TODO();
	return 1;
}

