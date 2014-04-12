package com.dimitrisli.web.dailydeal

import org.jsoup.Jsoup
import com.gargoylesoftware.htmlunit.{BrowserVersion, WebClient}
import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.quartz.{JobExecutionContext, Job}


class DailyDeal extends Job {
  def execute(p1: JobExecutionContext) {
    //publishers
    val publishers = List(APress, Manning, Microsoft, OReilly, Springer)

    val emailify: Publisher=>String = (publisher:Publisher) => s"<strong>${publisher}</strong>: ${DailyDeal(publisher)}"

    send a new Mail(
      from = "your@email" -> "YourWebCrawlerName",
      to = "your@email,
      subject = "Tech Ebooks Daily Deals",
      message = "",
      richMessage = s"${publishers.map(emailify).mkString("<br />")}"
    )
  }
}

object DailyDeal {

  type WebScrappingStrategy = String => String

  case class WebScrap(strategy: WebScrappingStrategy) {
    def dailyDeal(url:String) = strategy( url )
  }

  def ManningDailyDeal = {

    val ManningWebScrappingStrategy: WebScrappingStrategy  =
      (url:String) =>
        Jsoup.parse( new WebClient(BrowserVersion.CHROME).getPage( url )
          .asInstanceOf[HtmlPage].asXml )
          .select("div.dotdbox b").text

    WebScrap( ManningWebScrappingStrategy ).dailyDeal( "http://www.manning.com" )
  }

  def OReillyDailyDeal = {

    val OReillyWebScrappingStrategy: WebScrappingStrategy = Jsoup.connect(_).get.select("a[href$=DEAL]").get(1).text

    WebScrap( OReillyWebScrappingStrategy ).dailyDeal( "http://oreilly.com" )
  }

  def MicrosoftDailyDeal = {

    val MicrosoftWebScrappingStrategy: WebScrappingStrategy = Jsoup.connect(_).get.select("a[href$=MSDEAL]").get(1).text

    WebScrap( MicrosoftWebScrappingStrategy ).dailyDeal( "http://oreilly.com" )
  }

  def APressDailyDeal = {

    val APressWebScrappingStrategy: WebScrappingStrategy = Jsoup.connect(_).get.select("div.block-dotd").get(0).select("a")
      .get(0).select("img").attr("alt")

    WebScrap( APressWebScrappingStrategy ).dailyDeal( "http://www.apress.com" )
  }

  def SpringerDailyDeal = {

    val SpringerWebScrappingStrategy: WebScrappingStrategy = Jsoup.connect(_).get.select("div.block-dotd").get(1).select("a")
      .get(0).select("img").attr("alt")

    WebScrap( SpringerWebScrappingStrategy ).dailyDeal( "http://www.apress.com" )
  }

  def apply(publisher: Publisher) = publisher match {
    case Manning => ManningDailyDeal
    case APress => APressDailyDeal
    case Springer => SpringerDailyDeal
    case OReilly => OReillyDailyDeal
    case Microsoft => MicrosoftDailyDeal
  }
}
