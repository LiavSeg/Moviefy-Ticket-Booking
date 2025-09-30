<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/monthlyRevenueReport">
        <html>
            <head>
                <title>Monthly Revenue</title>
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
                <h1>Monthly Revenue</h1>
                <div class="meta">
                    <strong>Generated At:</strong> <xsl:value-of select="generatedAt"/>
                </div>

                <table>
                    <thead>
                        <tr>
                            <th>Year-Month</th>
                            <th class="right">Total Income</th>
                            <th class="right">Total Bookings</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="month">
                            <xsl:sort select="yearMonth" data-type="text" order="ascending"/>
                            <tr>
                                <td><xsl:value-of select="yearMonth"/></td>
                                <td class="right">
                                    <xsl:value-of select="format-number(number(totalIncome), '#,##0.00')"/>
                                </td>
                                <td class="right"><xsl:value-of select="totalBookings"/></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                    <tfoot>
                        <tr>
                            <th>Totals</th>
                            <th class="right">
                                <xsl:variable name="sumIncome" select="sum(month/totalIncome)"/>
                                <xsl:value-of select="format-number($sumIncome, '#,##0.00')"/>
                            </th>
                            <th class="right">
                                <xsl:value-of select="sum(month/totalBookings)"/>
                            </th>
                        </tr>
                    </tfoot>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
