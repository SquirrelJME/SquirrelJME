# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Threading and atomics support

# For compatibility for Windows, do not use pthreads even if available
find_package(Threads)
if(WIN32)
	add_compile_definitions(SJME_CONFIG_HAS_THREADS=1)
	add_compile_definitions(SJME_CONFIG_HAS_THREADS_WIN32=1)
elseif(Threads_FOUND)
	if(CMAKE_USE_PTHREADS_INIT)
		add_compile_definitions(
			SJME_CONFIG_HAS_THREADS=1)
		add_compile_definitions(
			SJME_CONFIG_HAS_THREADS_PTHREAD=1)

		# Specific OSes?
		if (LINUX)
			add_compile_definitions(
				SJME_CONFIG_HAS_THREADS_PTHREAD_LINUX=1)
		elseif (APPLE OR BSD)
			add_compile_definitions(
				SJME_CONFIG_HAS_THREADS_PTHREAD_BSD=1)
		endif()
	endif()
endif()