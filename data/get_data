#!/usr/bin/env bash

if [ $# -ne 1 ]; then
    echo $0: usage: get_data number_of_local_segments
    exit 1
fi

LOCAL_SEGMENTS=$1

local_file_index=local_index.txt
input_file=gdeltv2gkg.txt
segment_folder=segment

BASE_URL=http://gdelt-open-data.s3.amazonaws.com

! test -e $local_file_index || rm $local_file_index
test -d $segment_folder || mkdir $segment_folder


files=$(cat $input_file | head -n $LOCAL_SEGMENTS)

for file in $files; do
    bucket_path=$(echo $file | egrep -o '/v2/gkg/[0-9]*.gkg.csv')
    file_name=$(echo $file | egrep -o '[0-9]*.gkg.csv')
    cd $segment_folder
    wget --timestamping $BASE_URL$bucket_path
    cd -
    echo $(pwd)/$segment_folder/$file_name >> $local_file_index
done
