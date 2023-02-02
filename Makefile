SHELL := /bin/bash

# ======================================================================================
default: help;

build: ## build the project locally using maven; call with SKIPTESTS=true to skip tests"
	@if [ ! -z "$(SKIPTESTS)" ]; then SKIP="-DskipTests"; else SKIP=""; fi && \
	mvn clean package $$SKIP;
rde-reset: #reset the RDE to a pristine state
	@aio aem:rde:reset

project := $(shell ls all/target/ | grep zip )
dispatcher := $(shell ls dispatcher/target/ | grep zip )
rde-install: ## build and install the project on an RDE using aio
	@aio aem:rde:install -t content-package all/target/$(project)
	@aio aem:rde:install -t dispatcher-config dispatcher/target/$(dispatcher)

local-it: ## run ITs locally
	source secrets.sh && \
	cd it.tests && \
	mvn clean verify \
	-Plocal \
	-Dit.author.url="$${AUTHOR_URL}" \
	-Dit.author.user="$${AUTHOR_USER}" \
	-Dit.author.password="$${AUTHOR_PASSWORD}" \
	-Dit.publish.url="$${PUBLISH_URL}" \
	-Dit.publish.user="$${PUBLISH_USER}" \
	-Dit.publish.password="$${PUBLISH_PASSWORD}" \
	-Dmaven.javadoc.skip=true \
	-Dmaven.surefire.debug

clean: ## clean the build environment
	@mvn clean

help: ## Show this help
	@egrep '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST)  | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m·%-20s\033[0m %s\n", $$1, $$2}'