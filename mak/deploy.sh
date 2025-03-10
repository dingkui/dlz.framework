#!/bin/bash

# 获取变更的文件列表
CHANGED_FILES=$(git diff --name-only HEAD^ HEAD)
# 初始化一个空数组来存储发生变化的模块路径
CHANGED_MODULES=()
# 打印发生变化的模块路径
echo "Changed files:"
# 遍历变更的文件，确定发生变化的模块路径
for FILE in $CHANGED_FILES; do
    # 获取文件所在的目录
    DIR=$(dirname $FILE)
    echo $MODULE
    # 查找最近的pom.xml文件所在的目录
    while [ "$DIR" != "." ]; do
        if [ -f "$DIR/pom.xml" ]; then
            CHANGED_MODULES+=("$DIR")
            break
        fi
        DIR=$(dirname $DIR)
    done
done
# 去重
UNIQUE_MODULES=($(printf "%s\n" "${CHANGED_MODULES[@]}" | sort -u))
declare -A changed_map
for module in "${UNIQUE_MODULES[@]}"; do
  changed_map["$module"]=1
done
# 打印发生变化的模块路径
echo "Changed modules:"
for MODULE in "${UNIQUE_MODULES[@]}"; do
    echo $MODULE
done

# 需要忽略的 pom.xml 文件路径（保持原样）
ignore_MODULES=(
  "dlz.framework.db/dlz.framework.mongo"
  "dlz.plugin/dlz.plugin.component"
  "dlz.plugin/dlz.plugin.quartz"
  "dlz.plugin/dlz.plugin.socket"
  "dlz.plugin/dlz.plugin.websocket"
  "dlz.plugin/dlz.plugin.weixin"
  "dlz.webs"
  "dlz.webs/dlz.web"
  "dlz.webs/dlz.web.cloud"
)
declare -A ignore_map
for module in "${ignore_MODULES[@]}"; do
  ignore_map["$module"]=1
done

BUILD_MODULES=()
for module in "${UNIQUE_MODULES[@]}"; do
    # 调试语句（正式使用可删除）
    if [[ -z "${ignore_map[$module]}" ]]; then
      BUILD_MODULES+=("$module")
    else
      # 调试被忽略的项（正式使用可删除）
      echo "Ignored: $module" >&2
    fi
done


## 构建Maven命令
MVN_CMD="mvn -B clean source:jar deploy"
if [ ${#BUILD_MODULES[@]} -gt 0 ]; then
    # 使用 IFS 临时设置为逗号，并将数组元素连接成一个字符串
    IFS=','; EXCLUDE_MODULES_STR="'${BUILD_MODULES[*]}'"
    MVN_CMD+=" -pl $EXCLUDE_MODULES_STR"

    # 执行Maven编译
    echo "Maven command: $MVN_CMD"
    eval $MVN_CMD
else
    echo "No modules to deploy."
fi

