<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="uri:xsl"
  xmlns:exa="http://www.importa"
  xmlns:bbb="http://www.importb">


  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="NaverWebtoon">
    <html>
      <head>
        <link href="https://fonts.googleapis.com/css?family=Nanum+Gothic" rel="stylesheet"></link>
      </head>
      <body style="font-family: 'Nanum Gothic', sans-serif;">
        <h1>
          <xsl:apply-templates select="mainTitle"/>
        </h1>
        <h3>
          <xsl:apply-templates select="header/madeby"/>
        </h3>
        <table border="2">

          <tr bgcolor="#e8e7ed">
            <th>번호</th>
            <th>제목</th>
            <th>작가</th>
            <th>썸네일</th>
            <th>장르</th>
            <th>줄거리</th>
            <th>연재 요일</th>
            <th>시작 날짜</th>
            <th>컷툰</th>
            <th>평점</th>
            <th>목록 보기</th>
          </tr>

          <xsl:for-each select="contents/webtoons">
            <tr>
              <td>
                <xsl:value-of select="@sid"/>
              </td>
              <td><xsl:value-of select="work/title"/></td>
              <td><xsl:value-of select="work/author"/></td>
              <td><xsl:apply-templates select="work/figure"/></td>
              <td><xsl:value-of select="work/genre"/></td>
              <td><xsl:value-of select="work/synopsis"/></td>
              <td><xsl:value-of select="publishing/day"/></td>
              <td><xsl:apply-templates select="publishing/date"/></td>
              <td><xsl:value-of select="publishing/cutToon"/></td>

              <xsl:if test="publishing/stars[. $ge$ 9.95]">
                <td style="color: blue">
                  <strong>
                    <xsl:apply-templates select="publishing/stars"/>
                  </strong>
                </td>
              </xsl:if>

              <xsl:if test="publishing/stars[. $lt$ 9.95]">
                <td style="color: red">
                  <xsl:apply-templates select="publishing/stars"/>
                </td>
              </xsl:if>

              <td>
                <xsl:apply-templates select="gotoWebsite"/>
              </td>
            </tr>
          </xsl:for-each>
        </table>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="mainTitle">
    <div style="color:white; background:#2db400; text-align:center; padding:20px; margin:10px">
      <xsl:value-of/>
    </div>
  </xsl:template>

  <xsl:template match="bbb:header/madeby">
    <div style="text-align:right; margin:10px;">
      <xsl:value-of/>
    </div>
  </xsl:template>

  <xsl:template match="work/figure">
    <div>
      <img>
        <xsl:attribute name="src">
          <xsl:value-of select="@src"/>
        </xsl:attribute>

        <xsl:attribute name="width">
          <xsl:value-of select="@width"/>
        </xsl:attribute>

        <xsl:attribute name="height">
          <xsl:value-of select="@height"/>
        </xsl:attribute>

        <xsl:attribute name="alt">
          <xsl:value-of select="@alt"/>
        </xsl:attribute>
      </img>
    </div>
  </xsl:template>

  <xsl:template match="publishing/date">
    <xsl:value-of/>
  </xsl:template>

  <xsl:template match="publishing/stars">

    <xsl:eval>print_stars(this)</xsl:eval>
  </xsl:template>

  <xsl:template match="gotoWebsite">
    <a href="{.}"><xsl:value-of select="."/></a>
  </xsl:template>

  <xsl:script>
    <![CDATA[
function print_stars(property){
return property.text + '점';
} ]]>
  </xsl:script>

</xsl:stylesheet>
