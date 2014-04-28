import miner.TrainingSetBuilder
import play.api.Application
import play.api.mvc.WithFilters
import trainer.Trainer

object Global extends WithFilters() {
  override def onStart(app: Application): Unit = {
    super.onStart(app)

    val dataSet = TrainingSetBuilder.build("tone/comment", 2000)

    val model = Trainer.train(dataSet)

  }
}
