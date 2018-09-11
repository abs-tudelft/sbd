
param (
 [Parameter(Mandatory=$true)][int]$no_local_segments
)

$local_file_index="local_index.txt"
$input_file="gdeltv2gkg.txt"
$segment_folder="segment"

$base_url="http://gdelt-open-data.s3.amazonaws.com"
IF(!(test-path $segment_folder)) {
	mkdir $segment_folder
}

IF(test-path $local_file_index) {
	rm $local_file_index
}

New-Item $local_file_index

$files=cat -head $no_local_segments $input_file

[regex]$bucket_regex = "/v2/gkg/[0-9]*.gkg.csv"
[regex]$file_regex = "[0-9]*.gkg.csv"
$ProgressPreference = 'SilentlyContinue'

foreach($file in $files) {
	$bucket_path = $bucket_regex.Matches($file).Value
	$filename = $file_regex.Matches($file).Value
	
	Write-Host $bucket_path
	
	iwr -Uri "$base_url$bucket_path" -Outfile "$segment_folder\$filename"
	"$pwd\$segment_folder\$filename" >> $local_file_index
}