<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/popularMoviesReport">
        <html>
            <head>
                <title>Popular Movies</title>
                <meta charset="UTF-8"/>
                <style>
                    body { font-family: Arial, sans-serif; background:#f9f9f9; margin:20px; }
                    h1 { color:#333; margin:0 0 10px; }
                    .meta { color:#555; margin-bottom:16px; }
                    table { border-collapse: collapse; width:100%; background:#fff; box-shadow:0 0 8px rgba(0,0,0,.1); }
                    th, td { border:1px solid #ddd; padding:8px; text-align:left; }
                    th { background:#4CAF50; color:#fff; }
                    tr:nth-child(even) { background:#f2f2f2; }
                    .right { text-align:right; }
                </style>
            </head>
            <body>
                <h1>Popular Movies</h1>
                <div class="meta">
                    <div><strong>Generated At:</strong> <xsl:value-of select="generatedAt"/></div>
                    <div>
                        <strong>Range:</strong>
                        <xsl:value-of select="range/from"/> — <xsl:value-of select="range/to"/>
                    </div>
                </div>

                <table>
                    <thead>
                        <tr>
                            <th>Rank</th>
                            <th>Movie ID</th>
                            <th>Movie Title</th>
                            <th class="right">Bookings</th>
                            <th class="right">Tickets Sold</th>
                            <th class="right">Total Income</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="movie">
                            <xsl:sort select="bookingsCount" data-type="number" order="descending"/>
                            <tr>
                                <td><xsl:value-of select="position()"/></td>
                                <td><xsl:value-of select="movieId"/></td>
                                <td><xsl:value-of select="movieTitle"/></td>
                                <td class="right"><xsl:value-of select="bookingsCount"/></td>
                                <td class="right"><xsl:value-of select="ticketsSold"/></td>
                                <td class="right">
                                    <xsl:value-of select="format-number(number(totalIncome), '#,##0.00')"/>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>

                <!-- Totals -->
                <div class="meta" style="margin-top:16px;">
                    <div>
                        <strong>Total Income (All Movies):</strong>
                        <xsl:variable name="sumIncome" select="sum(movie/totalIncome)"/>
                        <xsl:value-of select="format-number($sumIncome, '#,##0.00')"/>
                    </div>
                    <div>
                        <strong>Total Bookings (All Movies):</strong>
                        <xsl:value-of select="sum(movie/bookingsCount)"/>
                    </div>
                    <div>
                        <strong>Total Tickets (All Movies):</strong>
                        <xsl:value-of select="sum(movie/ticketsSold)"/>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
