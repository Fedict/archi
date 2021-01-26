# archi

Experimental Confluence-to-ArchiMate command line tool, the goal is to be able to load existing documentation into the [Archi tool](https://www.archimatetool.com/).

Currently it only converts title and description of Confluence wiki pages to an [ArchiMate Model Exchange](http://www.opengroup.org/xsd/archimate/3.0/) XML file, using the [Confluence REST API](https://developer.atlassian.com/server/confluence/confluence-rest-api-examples/). Note that this open file format is _not_ the proprietary Archi format (.archimate). 

Please consult the [User Guide, p91 Importing from an Open Exchange XML file](https://www.archimatetool.com/downloads/Archi%20User%20Guide.pdf) for more information on how to load the data into Archi.

Requires Java runtime 11

# Example

This example assumes that there is a confluence instance available on https://demo.atlassian.example.com with a few wiki pages tagged with `architecture`, and that user jane.doe has created an API token (https://id.atlassian.com/manage-profile/security/api-tokens) for authentication.
Output will be written to `output.xml`

```
java -jar archi-1.0-SNAPSHOT.jar -u jane.doe@example.com -p ConfluenceAPIToken -l https://demo.atlassian.example.com -t architecture -f output.xml
```
