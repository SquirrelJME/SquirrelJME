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

# build_linux_aarch64_natives
# build_linux_aarch64_standalone_flatpak
# build_linux_amd64
# build_linux_amd64_natives
# build_linux_amd64_standalone
# build_linux_amd64_standalone_flatpak
# build_linux_arm64
# build_linux_riscv64
# build_linux_riscv64_natives
# build_macosx_aarch64
# build_macosx_aarch64_natives
# build_macosx_amd64
# build_macosx_amd64_natives
# build_windows_amd64
# build_windows_amd64_natives
# build_windows_amd64_setup
# build_windows_i386
# build_windows_i386_natives
# build_windows_i386_setup
# bundle
# construct_testJars
# export_user_guide
# sonatype_publish
# test_springcoat_debug
# test_springcoat_release
