#!/usr/bin/env sh

yarn prepack
git add lib
git commit -m "chore: prepack package"

release-it
