#!/bin/bash
echo "开始运行打包dap-paas-console-platform"
echo "删除镜像"
docker rmi -f dappaasconsole/dap-paas-console-platform:5.0.3

# . 表示当前目录 -f 参数指定Dockerfile文件  -t 表示 制作的镜像tag
# 制作docker镜像
docker build -f Dockerfile -t dappaasconsole/dap-paas-console-platform:5.0.3 .