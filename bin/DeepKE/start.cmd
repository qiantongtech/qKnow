@echo off
docker exec b46fdb4affb2 python -u predict.py text="'%~1'"
