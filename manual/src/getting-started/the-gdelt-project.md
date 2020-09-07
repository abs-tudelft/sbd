## The GDELT project

During the lab, we will use the GDELT Global Knowledge Graph version 2.0 (GKG
v2.0). This database is basically a massive table that "connects every person,
organization, location, count, theme, news source, and event across the planet
into a single massive network that captures what's happening around the world,
what its context is and who's involved, and how the world is feeling about it,
every single day."

The GKG is updated every 15 minutes and published as a series of compressed
comma-separated values (CSV) files (in which the columns are actually separated
using tabs). The first segment of the version 2 database was published back in
2015 and at the time of writing (September 2019) it contains 157378 segments
with a total compressed file size of 1.4 terabytes (TiB). Uncompressed, this
comes down to about 4.2 terabytes of raw data.

Read [this article] for a general introduction to the dataset, [this table] for
a quick overview of its columns and what they mean, and [this document] for a
more in depth description.

During the labs, we will use the `AllNames` column, which lists all "proper
names" (people, organizations, countries, etcetera) mentioned. It can be seen
as a summary of many of the other columns.

[this article]: https://blog.gdeltproject.org/gdelt-2-0-our-global-world-in-realtime/
[this table]: https://github.com/linwoodc3/gdelt2HeaderRows/blob/master/schema_csvs/GDELT_2.0_gdeltKnowledgeGraph_Column_Labels_Header_Row_Sep2016.tsv
[this document]: http://data.gdeltproject.org/documentation/GDELT-Global_Knowledge_Graph_Codebook-V2.1.pdf
