#!/bin/bash


# Guardamos el mensaje de commit
COMMIT_MESSAGE="$1"

# Ejecutamos los comandos de Git
git add .
git commit -m "$COMMIT_MESSAGE"
git push