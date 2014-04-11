package com.dimitrisli.web.dailydeal

import org.quartz._
import org.quartz.impl.StdSchedulerFactory


object Main {

  def main(args:Array[String]){

    //scheduler settings
    val  sched:Scheduler = new StdSchedulerFactory().getScheduler()


    val job:JobDetail = JobBuilder.newJob(classOf[DailyDeal])
                          .withIdentity("jobDailyDeal", "groupDailyDeal")
                          .build()

    val trigger = TriggerBuilder
                    .newTrigger
                    .withIdentity("triggerDailyDeal", "groupDailyDeal")
                    .startNow
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(12,0))
                    .build

    sched.scheduleJob(job, trigger)
    sched.start
  }
}
