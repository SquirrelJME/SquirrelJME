# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Pipeline Delegation, this is for the purpose of having a
# service-independent way of performing CI/CD pipeline builds on any service,
# or even locally. This is so I can migrate between accordingly.

# Use the best version possible
# Otherwise, fallback to the lowest viable version
if(CMAKE_VERSION VERSION_GREATER_EQUAL 3.13)
	cmake_minimum_required(VERSION 3.13..3.31)
else()
	cmake_minimum_required(VERSION 3.0)
endif()

# Easier includes
set(SQUIRRELJME_NANOCOAT_SOURCE_DIR
	"${CMAKE_SOURCE_DIR}/nanocoat")
include("${CMAKE_SOURCE_DIR}/assets/cmake/easier-includes.cmake"
	NO_POLICY_SCOPE)

# Include backports and versioning
squirreljme_include_nanocoat("cmake-bp.cmake")

# Disable parallelism since Java building can run out of memory on a number
# of systems, so for job stability it is best to do that here
set(CMAKE_BUILD_PARALLEL_LEVEL 1)

# Force the build system to always be unconfigured
set(SQUIRRELJME_SYSTEM "none")
set(SQUIRRELJME_ARCH "none")

# Version number for SquirrelJME
squirreljme_include_nanocoat("identify-squirreljme-version-file.cmake")

# A project must be declared
# Specifying languages here with no languages means that we do not want to
# check for any languages, by default C/C++ are enabled which we do not
# care for as we are just delegating builds
project(SquirrelJMEBuild
	HOMEPAGE_URL https://squirreljme.cc/
	LANGUAGES)

# Version number for SquirrelJME
squirreljme_include_nanocoat("identify-squirreljme-version.cmake")

# To Emily and Near...
squirreljme_include_nanocoat("i-miss-you.cmake")

# Include system mappings
squirreljme_include_nanocoat("system-map.cmake")

# Global macros, functions, and properties
squirreljme_include("global.cmake")

# Environment and everything else
squirreljme_include("environment-depends.cmake")

## Include a specified file such as a configuration?
#if(DEFINED $ENV{SQUIRRELJME_PIPELINE_CONFIG})
#	message(STATUS "Including pipeline configuration...")
#
#	include("$ENV{SQUIRRELJME_PIPELINE_CONFIG}")
#elseif(DEFINED SQUIRRELJME_PIPELINE_CONFIG)
#	message(STATUS "Including pipeline configuration...")
#
#	include("${SQUIRRELJME_PIPELINE_CONFIG}")
#endif()

# CI/CD Pipelines
###DISABLED:squirreljme_include("cicd-pipeline.cmake"):DISABLED###

# Updating the Fossil site, uploading everything to it, where possible
squirreljme_include("fossil-site.cmake")

# Find compilers and such
squirreljme_include("find.cmake")

# Automated tests
squirreljme_include("automated-tests.cmake")

# Building sources
squirreljme_include("sources.cmake")

# Building the NanoCoat runtime
squirreljme_include("nanocoat-runtime.cmake")

# Building standalone
squirreljme_include("standalone.cmake")

# Creation of the Install4J Installers and Portables
squirreljme_include("install4j.cmake")

# Building of the Linux Flatpak
# Disabled because of the reasoning in the given file
###DISABLED:squirreljme_include("flatpak.cmake"):DISABLED###

# Exporting of pipeline rules to build and setup CI/CD files
###DISABLEDsquirreljme_include("pipeline-export.cmake"):DISABLED###

# Sanity check for whether the host has natives available
if(TARGET
	natives.${SQUIRRELJME_HOST_SYSTEM}.${SQUIRRELJME_HOST_ARCH}.compiler.build)
	set(SQUIRRELJME_HOST_HAS_COMPILER YES)
endif()

# Because some users will demand...
if(TARGET natives.windows.ia32)
	set(SQUIRRELJME_CAN_TARGET_WINDOWS_IA32 YES)
endif()
if(TARGET natives.windows.amd64)
	set(SQUIRRELJME_CAN_TARGET_WINDOWS_AMD64 YES)
endif()
if(TARGET natives.macosx.amd64)
	set(SQUIRRELJME_CAN_TARGET_MACOSX_AMD64 YES)
endif()
if(TARGET natives.macosx.arm64l)
	set(SQUIRRELJME_CAN_TARGET_MACOSX_ARM64L YES)
endif()
if(TARGET natives.linux.amd64)
	set(SQUIRRELJME_CAN_TARGET_LINUX_AMD64 YES)
endif()

# CMake 4.0 changes how things work and can break things at times
if(CMAKE_VERSION VERSION_LESS 4.0)
	set(SQUIRRELJME_CMAKE_THREE YES)
endif()

if(CMAKE_VERSION VERSION_GREATER_EQUAL 3.13)
	set(SQUIRRELJME_CMAKE_NEW_ENOUGH YES)
endif()

# Function for printing build capabilities better
function(squirreljme_build_capability flag yesMessage noMessage)
	if(${${flag}})
		message(STATUS "++ ${yesMessage}")
	else()
		message(STATUS "-- ${noMessage}")
	endif()
endfunction()

# What this build is capable of... this is mostly for troubleshooting so that
# users that are not really developers can more quickly find out what is wrong
# or not wrong
squirreljme_build_capability(SQUIRRELJME_HAS_JAVA
	"SquirrelJME Runtime can be built and possibly tested!"
	"Java is missing, only building natives might be possible.")
squirreljme_build_capability(SQUIRRELJME_REPO_FOSSIL
	"Within a Fossil checkout!"
	"Not within a Fossil checkout...")
squirreljme_build_capability(SQUIRRELJME_REPO_GIT
	"Within a Git checkout!"
	"Not within a Git checkout...")
squirreljme_build_capability(SQUIRRELJME_REPO_FOSSIL
	"Cached natives via Fossil are available for Standalone."
	"Standalone natives only build for discovered compilers.")
squirreljme_build_capability(SQUIRRELJME_HAS_INSTALL4JC
	"Install4J Installers can be built."
	"No Install4J Installers are buildable, missing Install4J.")
squirreljme_build_capability(SQUIRRELJME_CAN_CONVERT_ICONS
	"Icon conversion is possible."
	"Icon conversion is not possible, missing basic utilities.")
squirreljme_build_capability(SQUIRRELJME_REPO_ANY
	"Source archives can be created as version control is possible."
	"Not in version control, thus cannot archive sources.")
squirreljme_build_capability(SQUIRRELJME_CMAKE_THREE
	"Running on CMake 3.0+ line!"
	"Running the build with CMake 4.0+ will work, however there may be bugs!")
squirreljme_build_capability(SQUIRRELJME_CMAKE_NEW_ENOUGH
	"CMake is newer than 3.13!"
	"Older CMake is soft-patched by this build, which works in most cases.")
squirreljme_build_capability(SQUIRRELJME_HOST_HAS_COMPILER
	"Host system has a compiler available, you can build for your own system!"
	"You cannot build natives for your own system as you lack a C Compiler.")
squirreljme_build_capability(SQUIRRELJME_CAN_TARGET_WINDOWS_IA32
	"Can bundle 32-bit Windows natives into Standalone."
	"No 32-bit Windows natives are available.")
squirreljme_build_capability(SQUIRRELJME_CAN_TARGET_WINDOWS_AMD64
	"Can bundle 64-bit Windows natives into Standalone."
	"No 64-bit Windows natives are available.")
squirreljme_build_capability(SQUIRRELJME_CAN_TARGET_MACOSX_AMD64
	"Can bundle 64-bit Intel macOS natives into Standalone."
	"No 64-bit Intel macOS natives are available.")
squirreljme_build_capability(SQUIRRELJME_CAN_TARGET_MACOSX_ARM64L
	"Can bundle ARM M1 macOS natives into Standalone."
	"No ARM M1 macOS natives are available.")
squirreljme_build_capability(SQUIRRELJME_CAN_TARGET_LINUX_AMD64
	"Can bundle 64-bit Linux (amd64) natives into Standalone."
	"No 64-bit Linux (amd64) natives are available.")
