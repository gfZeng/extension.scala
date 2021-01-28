package extension

import java.util.concurrent.CompletionStage
import java.util.{Timer, TimerTask}
import scala.compat.java8.FutureConverters.CompletionStageOps
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

package object concurrent {

  implicit def javaFuture2Scala[T](s: CompletionStage[T]): Future[T] = s.toScala

  Thread.setDefaultUncaughtExceptionHandler { (_, e) =>
    e.printStackTrace()
    System.exit(1)
  }

  def thread(cb: => Unit): Thread = {
    val th = new Thread(() => cb)
    th.start()
    th
  }

  implicit class FutureOps[+T](f: Future[T]) {
    def await(duration: Duration = Duration.Inf): T = Await.result(f, duration)
  }

  lazy val timer = new Timer()

  def schedule(delay: Long, period: Long)(task: => Unit): Unit = {
    timer.schedule(
      new TimerTask {
        def run(): Unit = task
      },
      delay,
      period
    )
  }

}
