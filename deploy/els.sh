#!/usr/bin/env bash
#################################### env ####################################
readonly USERNAME=elastic
readonly PASSWORD=1q2w3e4R
readonly ELS_SERVER="http://localhost:9200"
#################################### env ####################################

readonly PROJECT_ROOT=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
readonly SCRIPT_NAME=${0##*/}
readonly USERNAME_PASSWORD="${USERNAME}:${PASSWORD}"

function create_index() {
  local INDEX_NAME="${1}"
  local INDEX_FILE="${2}"

  curl -XPUT -u "${USERNAME_PASSWORD}" -H 'Content-type: application/json' ${ELS_SERVER}'/'${INDEX_NAME}"?pretty" --data "$(cat ${INDEX_FILE})"
}

function drop_index() {
  local INDEX_NAME="${1}"

  curl -XDELETE -u "${USERNAME_PASSWORD}" ${ELS_SERVER}"/"${INDEX_NAME}"?pretty"
}

function create_alias() {
  local ALIAS_NAME="${1}"
  local INDEX_NAME="${2}"

  curl -XPOST -u "${USERNAME_PASSWORD}" -H "Content-type: application/json" ${ELS_SERVER}"/_aliases?pretty" -d '
    {
      "actions":[
        {
          "add":{
            "index":"'${INDEX_NAME}'",
            "alias":"'${ALIAS_NAME}'"
          }
        }
      ]
    }
  '
}
# bash ./els.sh create product
function main() {
  command="$1"
  els_index="$2"

  case "$command" in
    create)
      create_index "${els_index}-v1" "${PROJECT_ROOT}/${els_index}.json"
      create_alias "${els_index}" "${els_index}-v1"
      ;;

    drop)
      drop_index "${els_index}-v1"
      ;;

    *)
      #err "unknown command";
      exit 2;
      ;;
  esac
}

main "$@"