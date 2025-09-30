<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="1.0">

    <xsl:output method="html" indent="yes" encoding="UTF-8"/>

    <!-- מפתחות למציאת ערכים ייחודיים -->
    <xsl:key name="userKey" match="booking" use="username" />
    <xsl:key name="movieKey" match="booking" use="movieTitle" />

    <xsl:template match="/">
        <div class="report-container">
            <h1>Booking Report</h1>

            <!-- סיכום ההזמנות -->
            <div class="report-summary" style="margin-bottom:20px; padding:10px; background:#f2f2f2; border:1px solid #ccc;">
                <p><strong>Total Bookings:</strong> <xsl:value-of select="count(//booking)"/></p>
                <p><strong>Total Tickets Sold:</strong> <xsl:value-of select="count(//booking/seats/seat)"/></p>
                <p><strong>Total Income:</strong> $<xsl:value-of select="format-number(sum(//booking/price), '#,##0.00')"/></p>

                <!-- Top Customer -->
                <xsl:variable name="topUser">
                    <xsl:for-each select="//booking[generate-id() = generate-id(key('userKey', username)[1])]">
                        <xsl:sort select="count(key('userKey', username))" data-type="number" order="descending"/>
                        <xsl:if test="position() = 1">
                            <xsl:value-of select="username"/>|<xsl:value-of select="count(key('userKey', username))"/>
                        </xsl:if>
                    </xsl:for-each>
                </xsl:variable>
                <p><strong>Top Customer:</strong>
                    <xsl:value-of select="substring-before($topUser, '|')"/>
                    (<xsl:value-of select="substring-after($topUser, '|')"/> bookings)
                </p>

                <!-- Top Movie -->
                <xsl:variable name="topMovie">
                    <xsl:for-each select="//booking[generate-id() = generate-id(key('movieKey', movieTitle)[1])]">
                        <xsl:sort select="count(key('movieKey', movieTitle))" data-type="number" order="descending"/>
                        <xsl:if test="position() = 1">
                            <xsl:value-of select="movieTitle"/>|<xsl:value-of select="count(key('movieKey', movieTitle))"/>
                        </xsl:if>
                    </xsl:for-each>
                </xsl:variable>
                <p><strong>Top Movie:</strong>
                    <xsl:value-of select="substring-before($topMovie, '|')"/>
                    (<xsl:value-of select="substring-after($topMovie, '|')"/> bookings)
                </p>
            </div>

            <!-- טבלת ההזמנות -->
            <table class="report-table" border="1" cellspacing="0" cellpadding="5" style="border-collapse:collapse; width:100%;">
                <thead style="background:#4CAF50; color:white;">
                    <tr>
                        <th>Booking ID</th>
                        <th>User ID</th>
                        <th>Username</th>
                        <th>Movie Title</th>
                        <th>Showtime</th>
                        <th>Theater</th>
                        <th>Seats</th>
                        <th>Price</th>
                        <th>Created At</th>
                    </tr>
                </thead>
                <tbody>
                    <xsl:apply-templates select="//booking"/>
                </tbody>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="booking">
        <tr>
            <td><xsl:value-of select="bookingId"/></td>
            <td><xsl:value-of select="userId"/></td>
            <td><xsl:value-of select="username"/></td>
            <td><xsl:value-of select="movieTitle"/></td>
            <td><xsl:value-of select="showtime"/></td>
            <td><xsl:value-of select="theater"/></td>
            <td>
                <xsl:for-each select="seats/seat">
                    <xsl:value-of select="."/>
                    <xsl:if test="position() != last()">, </xsl:if>
                </xsl:for-each>
            </td>
            <td><xsl:value-of select="format-number(price, '#,##0.00')"/></td>
            <td><xsl:value-of select="createdAt"/></td>
        </tr>
    </xsl:template>

</xsl:stylesheet>
