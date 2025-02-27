#!/bin/bash

# 获取变更的文件列表
CHANGED_FILES=$(git diff --name-only HEAD^ HEAD)
# 初始化一个空数组来存储发生变化的模块路径
CHANGED_MODULES=()
# 遍历变更的文件，确定发生变化的模块路径
for FILE in $CHANGED_FILES; do
    # 获取文件所在的目录
    DIR=$(dirname $FILE)

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
# 打印发生变化的模块路径
echo "Changed changed_map:"
for MODULE in "${changed_map[@]}"; do
    echo $MODULE
done

# 需要忽略的 pom.xml 文件路径（保持原样）
ignore_MODULES=(
  "./dlz.framework.db/dlz.framework.mongo/pom.xml"
  "./dlz.plugin/dlz.plugin.component/pom.xml"
  "./dlz.plugin/dlz.plugin.quartz/pom.xml"
  "./dlz.plugin/dlz.plugin.socket/pom.xml"
  "./dlz.plugin/dlz.plugin.websocket/pom.xml"
  "./dlz.plugin/dlz.plugin.weixin/pom.xml"
  "./dlz.webs/pom.xml"
  "./pom.xml"
  "./dlz.webs/dlz.web/pom.xml"
  "./dlz.webs/dlz.web.cloud/pom.xml"
)
declare -A ignore_map
for module in "${ignore_MODULES[@]}"; do
  ignore_map["$module"]=1
done
# 获取所有模块路径
ALL_MODULES=()
while IFS= read -r -d '' dir; do
  pom_path="${dir}"

  if [[ -z "${ignore_map[$pom_path]}" ]]; then
    ALL_MODULES+=("$dir")
  fi
done < <(find . -name "pom.xml" -exec dirname {} + -print0)
# 打印所有模块路径
echo "All modules:"
for MODULE in "${ALL_MODULES[@]}"; do
    echo $MODULE
done

# 构建排除列表
EXCLUDE_MODULES=()
for pom_path in "${ALL_MODULES[@]}"; do
    # 1. 去除前导的"./"（参数扩展方式）
    clean_path="${pom_path#./}" # 结果：dlz.webs/dlz.web.cloud/pom.xml
    # 2. 去除尾部的"/pom.xml"
    MODULES="${clean_path%/pom.xml}"
    # 调试语句（正式使用可删除）
    echo "Checking: $pom_path" :${changed_map[$MODULES]} >&2

    if [[ -z "${changed_map[$MODULES]}" ]]; then
      EXCLUDE_MODULES+=("$MODULES")
    else
      # 调试被忽略的项（正式使用可删除）
      echo "Ignored: $dir" >&2
    fi
done
# 打印排除的模块路径
echo "Excluded modules:"
for MODULE1 in "${EXCLUDE_MODULES[@]}"; do
    echo $MODULE1
done

## 构建Maven命令
MVN_CMD="mvn -B clean source:jar package"
if [ ${#EXCLUDE_MODULES[@]} -gt 0 ]; then
    # 使用 IFS 临时设置为逗号，并将数组元素连接成一个字符串
    MVN_CMD+=" -pl '"
    for MODULE in "${EXCLUDE_MODULES[@]}"; do
        echo $MODULE
        MVN_CMD+="!$MODULE,"
    done
    MVN_CMD+="'"
fi

# 执行Maven编译
echo "Maven command: $MVN_CMD"
eval $MVN_CMD