#!/bin/bash
# DeepKE.sh

if [ $# -lt 1 ]; then
    echo "Usage: $0 <text>"
    exit 1
fi

TEXT="$1"

# 注意：此处请换成你的docker容器id
docker exec dabea2a436b6 python -u predict.py "text='$TEXT'"
