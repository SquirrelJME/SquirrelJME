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

# This is a reasonable version to base off
cmake_minimum_required(VERSION 3.13)

# To Emily and Near...
include("${CMAKE_SOURCE_DIR}/nanocoat/cmake/i-miss-you.cmake")

# Easier includes
include("${CMAKE_SOURCE_DIR}/assets/cmake/easier-includes.cmake"
	NO_POLICY_SCOPE)

# Environment and everything else
squirreljme_include("environment-depends.cmake")

# A project must be declared
# Specifying languages here with no languages means that we do not want to
# check for any languages, by default C/C++ are enabled which we do not
# care for as we are just delegating builds
project(SquirrelJMEBuild
	HOMEPAGE_URL https://squirreljme.cc/
	LANGUAGES)

# CI/CD Pipelines
squirreljme_include("cicd-pipeline.cmake")

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

# Updating the Fossil site, uploading everything to it, where possible
squirreljme_include("fossil-site.cmake")

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
