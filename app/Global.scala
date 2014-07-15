import play.api._



object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Application has started")
//    PubNub.start
  }

  override def onStop(app: Application) {
    Logger.info("Application shutdown...")
//    PubNub.stop
  }
}

