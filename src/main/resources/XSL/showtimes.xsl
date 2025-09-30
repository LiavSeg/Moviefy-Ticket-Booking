<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:output method="html" indent="yes"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>Showtimes Report</title>
                <style>
                    table { border-collapse: collapse; width: 100%; }
                    th, td { border: 1px solid black; padding: 8px; }
                </style>
            </head>
            <body>
                <h2>Showtimes</h2>
                <table>
                    <tr>
                        <th>Movie ID</th>
                        <th>Theater</th>
                        <th>Start Time</th>
                        <th>End Time</th>
                        <th>Price</th>
                    </tr>
                    <xsl:for-each select="showtimes/showtime">
                        <tr>
                            <td><xsl:value-of select="movieId"/></td>
                            <td><xsl:value-of select="theater"/></td>
                            <td><xsl:value-of select="startTime"/></td>
                            <td><xsl:value-of select="endTime"/></td>
                            <td><xsl:value-of select="price"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
